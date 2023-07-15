package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.request.staff.AllocationAccountRequest;
import com.example.lmsbackend.dto.response.staff.*;
import com.example.lmsbackend.dto.staff.StaffDTO;
import com.example.lmsbackend.dto.staff.StaffPagedResponse;
import com.example.lmsbackend.dto.staff.StaffSimple;
import com.example.lmsbackend.service.StaffService;
import com.example.lmsbackend.utils.SearchCriteriaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;
import static com.example.lmsbackend.enums.PermissionEnum.ALLOCATE_ACCOUNT_STAFF;

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;
    private final SearchCriteriaUtils searchCriteriaUtils;

    @GetMapping("/staffs")
    public ResponseEntity<StaffPagedResponse> getAllStaffs(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                           @RequestParam(value = "filter", required = false) String filter,
                                                           @RequestParam(value = "keyword", required = false) String keyword,
                                                           @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok(staffService.getAllStaffs(searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }

    @GetMapping("/staffs/{staffId}")
    public ResponseEntity<GetStaffDetailsResponse> getStaff(@PathVariable(name = "staffId") Long staffId) {
        StaffDTO staff = staffService.getStaff(staffId, staffId);
        GetStaffDetailsResponse response = new GetStaffDetailsResponse();
        response.setStaff(staff);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/staffs")
    public ResponseEntity<CreateStaffResponse> createStaff(@RequestBody StaffDTO staff) {
        Long staffId = staffService.createStaff(staff);
        CreateStaffResponse response = new CreateStaffResponse();
        response.setStaffId(staffId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/staffs")
    public ResponseEntity<CreateStaffResponse> updateStaff(@RequestBody StaffDTO staff) {
        Long staffId = staffService.updateStaff(staff, staff.getId());
        CreateStaffResponse response = new CreateStaffResponse();
        response.setStaffId(staffId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/staffs/allocateAccount")
    public ResponseEntity<AllocateAccountResponse> allocateAccount(@RequestBody AllocationAccountRequest request) {
        Long staffId = staffService.allocateAccount(request, request.getStaffId());
        AllocateAccountResponse response = new AllocateAccountResponse();
        response.setStaffId(staffId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/staffs/{staffId}/deleteAccount")
    public ResponseEntity<DeleteAccountResponse> deleteAccount(@PathVariable(name = "staffId") Long staffId) {
        Long deletedId = staffService.deleteAccount(staffId);
        DeleteAccountResponse response = new DeleteAccountResponse();
        response.setStaffId(deletedId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staffsHaveAccount")
    public ResponseEntity<GetStaffsHaveAccountResponse> getStaffsHaveAccount() {
        List<StaffSimple> staffs = staffService.getStaffsHaveAccount();
        GetStaffsHaveAccountResponse response = new GetStaffsHaveAccountResponse();
        response.setStaffs(staffs);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/staffs/{staffId}")
    public ResponseEntity<DeleteStaffResponse> deleteStaff(@PathVariable(name = "staffId") Long staffId) {
        Long returnStaffId = staffService.deleteStaff(staffId);
        DeleteStaffResponse response = new DeleteStaffResponse(returnStaffId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/staffs/import", produces = {"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
    public void importStaffs(HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        XSSFWorkbook report = staffService.importStaffs(file);

        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=report_import_students_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        report.write(outputStream);
        report.close();

        outputStream.close();
    }
}
