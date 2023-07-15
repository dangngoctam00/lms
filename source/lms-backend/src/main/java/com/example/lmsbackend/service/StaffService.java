package com.example.lmsbackend.service;


import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.dto.request.staff.AllocationAccountRequest;
import com.example.lmsbackend.dto.staff.StaffDTO;
import com.example.lmsbackend.dto.staff.StaffPagedResponse;
import com.example.lmsbackend.dto.staff.StaffSimple;
import com.example.lmsbackend.dto.students.StudentDTO;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.EmailUsedException;
import com.example.lmsbackend.exceptions.UsernameExistedException;
import com.example.lmsbackend.exceptions.files.ReadFileHasException;
import com.example.lmsbackend.exceptions.staff.StaffNotFoundException;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.StaffEntityMapper;
import com.example.lmsbackend.mapper.StaffMapper;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.example.lmsbackend.repository.StaffRepository;
import com.example.lmsbackend.repository.TenantConfigRepository;
import com.example.lmsbackend.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.lmsbackend.constant.AppConstant.REALLOCATE_ACCOUNT_TEMPLATE_FILE;
import static com.example.lmsbackend.enums.PermissionEnum.*;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StaffService {
    public static final String DEFAULT_AVATAR = "https://robohash.org/afef74884da855b084ce6e8551cfc245?set=set4&bgset=&size=400x400";
    private final StaffRepository staffRepository;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final MailUtils mailUtils;
    private final TenantConfigRepository tenantConfigRepository;
    private final PlatformTransactionManager transactionManager;
    private final UtilsService utilsService;

    @Auth(permission = PermissionEnum.VIEW_ALL_STAFF)
    @Cacheable("staffs")
    public StaffPagedResponse getAllStaffs(BaseSearchCriteria criteria) {
        var staffs = staffRepository.findAllStaffs(criteria);
        var dto = new StaffPagedResponse();
        MapperUtils.mapPagedDto(dto, staffs);
        dto.setListData(staffs.stream()
                .filter(staffEntity -> staffEntity.getId() != 1)
                .map(StaffMapper.INSTANCE::mapFromStaffEntityToStaffDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    @Auth(permission = PermissionEnum.VIEW_DETAIL_STAFF)
    @Cacheable("staff")
    public StaffDTO getStaff(Long staffId, Long resourceId) {
        Optional<StaffEntity> staffEntityOptional = staffRepository.findById(staffId);
        if (staffEntityOptional.isPresent()) {
            return StaffMapper.INSTANCE.mapFromStaffEntityToStaffDTO(staffEntityOptional.get());
        } else throw new StaffNotFoundException();
    }

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_STAFF)
    public Long createStaff(StaffDTO staff) {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (staffRepository.existsByEmail(staff.getEmail())) {
            throw new EmailUsedException();
        }
        StaffEntity entity = StaffEntityMapper.INSTANCE.mapFromStaffDTO(staff);
        entity.setAvatar(DEFAULT_AVATAR);
        entity.setManager(entityManager.getReference(StaffEntity.class, currUser.getId()));
        entity.setAccountType(AccountTypeEnum.STAFF);
        staffRepository.save(entityManager.merge(entity));
        return entity.getId();
    }

    @Auth(permission = PermissionEnum.UPDATE_STAFF, selfCheck = true)
    public Long updateStaff(StaffDTO staff, Long resourceId) {
        if (staffRepository.existsByEmailAndIdNot(staff.getEmail(), staff.getId())) {
            throw new EmailUsedException();
        }
        Optional<StaffEntity> staffEntityOptional = staffRepository.findById(staff.getId());
        if (staffEntityOptional.isPresent()) {
            StaffEntity staffEntity = staffEntityOptional.get();
            staffEntity.setFirstName(staff.getFirstName());
            staffEntity.setLastName(staff.getLastName());
            staffEntity.setEmail(staff.getEmail());
            staffEntity.setPhone(staff.getPhone());
            staffRepository.save(staffEntity);
            return staffEntity.getId();
        } else throw new StaffNotFoundException();
    }


    @Auth(permission = {ALLOCATE_ACCOUNT_STAFF})
    public Long allocateAccount(AllocationAccountRequest request, Long resourceId) {
        Optional<StaffEntity> entity = staffRepository.findByUsernameAndIdNot(request.getUsername(), request.getStaffId());
        if (entity.isPresent()) {
            log.info("AuthService.register: username is used, request {}", request);
            throw new UsernameExistedException(request.getUsername());
        }
        Optional<StaffEntity> staffEntityOptional = staffRepository.findById(request.getStaffId());
        if (staffEntityOptional.isPresent()) {
            StaffEntity staffEntity = staffEntityOptional.get();
            staffEntity.setUsername(request.getUsername());
            staffEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            staffRepository.save(staffEntity);
            notifyUserByEmail(staffEntity, request.getUsername(), request.getPassword());
            return staffEntity.getId();
        } else throw new StaffNotFoundException();
    }

    private void notifyUserByEmail(StaffEntity staff, String username, String password) {
        var tenantConfig = tenantConfigRepository.findAll();
        var subject = "Thông báo mới từ trung tâm";
        if (CollectionUtils.isNotEmpty(tenantConfig)) {
            subject = subject + " " + StringUtils.capitalize(tenantConfig.get(0).getName());
        }
        var template = buildEmailTemplate(staff.getLastName() + " " + staff.getFirstName(), username, password);
        var tenant = utilsService.getTenantName();
        mailUtils.sendMailWithTemplate(tenant,
                staff.getEmail(),
                subject,
                template,
                REALLOCATE_ACCOUNT_TEMPLATE_FILE);
    }

    private HashMap<String, Object> buildEmailTemplate(String name, String username, String password) {
        var map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("username", username);
        map.put("password", password);
        return map;
    }

    public List<StaffSimple> getStaffsHaveAccount() {
        List<StaffEntity> staffEntities = staffRepository.findAllByUsernameIsNotNullAndIdNot(1L);
        return staffEntities.stream()
                .map(StaffMapper.INSTANCE::mapFromStaffEntityToStaffSimple)
                .collect(Collectors.toList());

    }

    @Auth(permission = {DELETE_ACCOUNT_STAFF})
    public Long deleteAccount(Long resourceId) {
        Optional<StaffEntity> staffEntityOptional = staffRepository.findById(resourceId);
        if (staffEntityOptional.isPresent()) {
            StaffEntity staffEntity = staffEntityOptional.get();
            staffEntity.setPassword(null);
            staffEntity.setUsername(null);
            staffRepository.save(staffEntity);
            return staffEntity.getId();
        } else throw new StaffNotFoundException();
    }

    @Auth(permission = {DELETE_STAFF})
    public Long deleteStaff(Long staffId) {
        if (staffRepository.existsById(staffId)) {
            staffRepository.deleteById(staffId);
            return staffId;
        } else throw new StaffNotFoundException();
    }

    @Auth(permission = {CREATE_STAFF})
    public XSSFWorkbook importStaffs(MultipartFile file) {
        CustomUserDetails currUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

            XSSFSheet worksheet = workbook.getSheetAt(0);
            XSSFRow header = worksheet.getRow(0);
            header.createCell(6);
            header.getCell(6).setCellValue("Kết quá");

            List<CompletableFuture<Long>> createStaffFutures = new ArrayList<>();

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = worksheet.getRow(i);
                StaffDTO staffDTO = new StaffDTO();
                try {
                    staffDTO.setFirstName(row.getCell(1).getStringCellValue());
                    staffDTO.setLastName(row.getCell(2).getStringCellValue());
                    staffDTO.setEmail(row.getCell(3).getStringCellValue());
                    staffDTO.setPhone(row.getCell(4).getStringCellValue());
                    staffDTO.setDescription(row.getCell(5).getStringCellValue());
                } catch (NullPointerException e) {
                    break;
                }
                row.createCell(6);

                createStaffFutures.add(
                        CompletableFuture.supplyAsync(() -> {
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currUser, null, new ArrayList<>());
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                            TransactionDefinition txDef = new DefaultTransactionDefinition();
                            TransactionStatus txStatus = transactionManager.getTransaction(txDef);
                            try {
                                Long staffId = createStaff(staffDTO);
                                row.getCell(6).setCellValue("Thành công");
                                transactionManager.commit(txStatus);
                                return staffId;
                            } catch (EmailUsedException exception) {
                                transactionManager.rollback(txStatus);
                                row.getCell(6).setCellValue("Email đã được sử dụng");
                                return 0L;
                            }
                        })
                );
            }
            createStaffFutures.stream().map(CompletableFuture::join)
                    .collect(toList());
            return workbook;
        } catch (Exception e) {
            throw new ReadFileHasException();
        }
    }
}
