package com.example.lmsbackend.service;

import com.example.lmsbackend.config.AppConfig;
import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.criteria.SortCriterion;
import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.UserRoleEntity;
import com.example.lmsbackend.domain.event.CalendarType;
import com.example.lmsbackend.dto.students.StudentDTO;
import com.example.lmsbackend.dto.students.StudentInfo;
import com.example.lmsbackend.dto.students.StudentPagedDTO;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.enums.RoleEnum;
import com.example.lmsbackend.exceptions.EmailUsedException;
import com.example.lmsbackend.exceptions.UnexpectedException;
import com.example.lmsbackend.exceptions.files.ReadFileHasException;
import com.example.lmsbackend.exceptions.student.StudentNotFoundException;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.StudentMapper;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.example.lmsbackend.repository.GradeTagStudentRepository;
import com.example.lmsbackend.repository.StudentRepository;
import com.example.lmsbackend.repository.UserRoleRepository;
import com.example.lmsbackend.repository.specification.StudentSpecification;
import com.example.lmsbackend.utils.MailUtils;
import com.example.lmsbackend.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final EntityManager entityManager;
    private final MailUtils mailUtils;
    private final PasswordEncoder passwordEncoder;
    private final RandomStringGenerator randomStringGenerator;
    private final EventService eventService;
    private final AppConfig appConfig;
    private final GradeTagStudentRepository gradeTagStudentRepository;
    private final UserRoleRepository userRoleRepository;

    private final PlatformTransactionManager transactionManager;
    private final UtilsService utilsService;

    public static final String DEFAULT_AVATAR = "https://robohash.org/afef74884da855b084ce6e8551cfc245?set=set4&bgset=&size=400x400";


    @Cacheable("students")
    @Auth(permission = PermissionEnum.VIEW_LIST_STUDENT)
    public StudentPagedDTO getStudents(BaseSearchCriteria criteria) {
        var filterSpecification = StudentSpecification.filter(criteria.getKeyword(), criteria.getFilters());
        List<Sort.Order> orders = new ArrayList<>();
        boolean isOrderById = false;
        for (SortCriterion sortCriterion : criteria.getSorts()) {
            if (Objects.equals(sortCriterion.getField(), "id")) isOrderById = true;
            orders.add(new Sort.Order(Sort.Direction.valueOf(StringUtils.upperCase(sortCriterion.getDirection())), sortCriterion.getField()));
        }
        if (!isOrderById) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "id"));
        }
        var pagination = PageRequest.of(criteria.getPagination().getPage() - 1, criteria.getPagination().getSize(), Sort.by(orders));
        var students = studentRepository.findAll(filterSpecification, pagination);
        var studentPagedDTO = new StudentPagedDTO();
        MapperUtils.mapPagedDto(studentPagedDTO, students);
        studentPagedDTO.setListData(
                students.stream()
                        .map(studentMapper::mapToStudentDTO)
                        .collect(toList())
        );
        return studentPagedDTO;
    }

    @Transactional
    @Auth(permission = PermissionEnum.ADD_STUDENT)
    public Long createStudent(StudentDTO studentDTO) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new EmailUsedException();
        }

        StudentEntity entity = studentMapper.mapToStudentEntity(studentDTO);
        entity.setAvatar(DEFAULT_AVATAR);
        entity.setUsername(studentDTO.getEmail());
        String password = randomStringGenerator.generate(10);
        entity.setPassword(passwordEncoder.encode(password));
        entity.setAccountType(AccountTypeEnum.STUDENT);
        entity.setCreatedAt(LocalDateTime.now());
        entity = studentRepository.save(entityManager.merge(entity));
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.getId().setUserId(entity.getId());
        userRoleEntity.getId().setRoleId(RoleEnum.STUDENT.getId());
        userRoleRepository.save(entityManager.merge(userRoleEntity));
        eventService.createCalendar(CalendarType.USER, entity.getId());
        entityManager.flush();
        String mailSubject = "Chào mừng bạn đến với trung tâm";
        String mailContent = generateMailContent(studentDTO, password);
        var tenantName = utilsService.getTenantName();

        mailUtils.sendMail(tenantName, studentDTO.getEmail(), mailSubject, mailContent);
        return entity.getId();
    }

    private String generateMailContent(StudentDTO studentDTO, String password) {
        String loginUrl = "http://".concat(TenantContext.getTenantId()).concat(".").concat(appConfig.getDomain()).concat("/");
        return "<p>Chào ".concat(studentDTO.getFirstName()).concat(",</p>")
                .concat("<p>Cảm ơn bạn đã tin tưởng và đăng ký ở trung tâm.</p>")
                .concat("<p>Bạn có thể đăng nhập tại <a href='").concat(loginUrl).concat("'>đây</a> với tài khoản và mật khảu được cung cấp bên dưới.</p>")
                .concat("<p><b>Tên đăng nhập</b>: ").concat(studentDTO.getEmail()).concat("</p>")
                .concat("<p><b>Mật khẩu</b>: ").concat(password).concat("</p>")
                .concat("<p>Chúc bạn học tập tốt.</p>");
    }

    public StudentDTO getStudent(Long studentId) {
        Optional<StudentEntity> studentEntityOptional = studentRepository.findById(studentId);
        if (studentEntityOptional.isPresent()) {
            return studentMapper.mapToStudentDTO(studentEntityOptional.get());
        } else {
            throw new StudentNotFoundException();
        }
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_STUDENT)
    public Long updateStudent(StudentDTO studentDTO) {
        Optional<StudentEntity> studentEntityOptional = studentRepository.findById(studentDTO.getId());
        if (studentEntityOptional.isPresent()) {
            StudentEntity studentEntity = studentEntityOptional.get();
            studentEntity.setFirstName(studentDTO.getFirstName());
            studentEntity.setLastName(studentDTO.getLastName());
            studentEntity.setEmail(studentDTO.getEmail());
            studentEntity.setPhone(studentDTO.getPhone());
            studentEntity.setAddress(studentDTO.getAddress());
            studentRepository.save(studentEntity);
            return studentEntity.getId();
        } else {
            throw new StudentNotFoundException();
        }
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_STUDENT)
    public Long deleteStudent(Long studentId) {
        if (studentRepository.existsById(studentId)) {
            try {
                gradeTagStudentRepository.deleteAllByStudentId(studentId);
                studentRepository.deleteById(studentId);
                return studentId;
            } catch (Exception exception) {
                log.error("Can not delete student", exception);
                throw new UnexpectedException();
            }
        } else throw new StudentNotFoundException();
    }

    public List<StudentInfo> getStudentsNotInClass(String keyword, Long classId) {
        return studentRepository.findAllStudentNotInClass(keyword, classId)
                .stream().map(studentMapper::mapToStudentInfo)
                .collect(toList());
    }

    public XSSFWorkbook importStudents(MultipartFile file) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

            XSSFSheet worksheet = workbook.getSheetAt(0);
            XSSFRow header = worksheet.getRow(0);
            header.createCell(6);
            header.getCell(6).setCellValue("Kết quá");

//            List<CompletableFuture<Long>> createStudentFutures = new ArrayList<>();

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = worksheet.getRow(i);
                StudentDTO studentDTO = new StudentDTO();

                try {
                    studentDTO.setFirstName(row.getCell(1).getStringCellValue());
                    studentDTO.setLastName(row.getCell(2).getStringCellValue());
                    studentDTO.setEmail(row.getCell(3).getStringCellValue());
                    studentDTO.setPhone(row.getCell(4).getStringCellValue());
                    studentDTO.setAddress(row.getCell(5).getStringCellValue());
                } catch (NullPointerException e) {
                    break;
                }

                row.createCell(6);
//                createStudentFutures.add(
//                        CompletableFuture.supplyAsync(() -> {
//                            TransactionDefinition txDef = new DefaultTransactionDefinition();
//                            TransactionStatus txStatus = transactionManager.getTransaction(txDef);
                            try {
                                Long studentId = createStudent(studentDTO);
                                row.getCell(6).setCellValue("Thành công");
//                                transactionManager.commit(txStatus);
//                                return studentId;
                            } catch (EmailUsedException exception) {
//                                transactionManager.rollback(txStatus);
                                row.getCell(6).setCellValue("Email đã được sử dụng");
//                                return 0L;
                            }
//                        })
//                );
            }
//            createStudentFutures.stream().map(CompletableFuture::join)
//                    .collect(toList());
            return workbook;
        } catch (Exception e) {
            log.error("Lỗi", e);
            throw new ReadFileHasException();
        }
    }
}
