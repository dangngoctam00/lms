package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.response.student.*;
import com.example.lmsbackend.dto.students.StudentDTO;
import com.example.lmsbackend.dto.students.StudentInfo;
import com.example.lmsbackend.dto.students.StudentPagedDTO;
import com.example.lmsbackend.service.StudentService;
import com.example.lmsbackend.utils.SearchCriteriaUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final SearchCriteriaUtils searchCriteriaUtils;

    @GetMapping("/students")
    public ResponseEntity<GetStudentsResponse> getAllStudents(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          @RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "filter", required = false) String filter,
                                                          @RequestParam(value = "sort", required = false) String sort){
        StudentPagedDTO studentPagedDTO = studentService.getStudents(searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size));
        GetStudentsResponse response = new GetStudentsResponse();
        response.setData(studentPagedDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/students")
    public ResponseEntity<CreateStudentResponse> createStudent(@RequestBody StudentDTO studentDTO){
        Long studentId = studentService.createStudent(studentDTO);
        CreateStudentResponse response = new CreateStudentResponse();
        response.setStudentId(studentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<GetStudentDetailsResponse> getStudent(@PathVariable(name = "studentId") Long studentId){
        StudentDTO studentDTO = studentService.getStudent(studentId);
        GetStudentDetailsResponse response = new GetStudentDetailsResponse();
        response.setStudent(studentDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/students")
    public ResponseEntity<UpdateStudentResponse> updateStudent(@RequestBody StudentDTO studentDTO){
        Long studentId = studentService.updateStudent(studentDTO);
        UpdateStudentResponse response = new UpdateStudentResponse();
        response.setStudentId(studentId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<DeleteStudentResponse> deleteStudent(@PathVariable(name = "studentId") Long studentId) {
        Long returnStudentId = studentService.deleteStudent(studentId);
        DeleteStudentResponse response = new DeleteStudentResponse();
        response.setStudentId(returnStudentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/students/getStudentsNotInClass")
    public ResponseEntity<GetStudentsNotInClassResponse> getStudentsNotInClass(@RequestParam(name = "q") String keyword, @RequestParam(name = "classId") Long classId){
        List<StudentInfo> studentInfos = studentService.getStudentsNotInClass(keyword, classId);
        GetStudentsNotInClassResponse response = new GetStudentsNotInClassResponse();
        response.setStudentInfos(studentInfos);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/students/import", produces = {"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
    public void importStudents(HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        XSSFWorkbook report = studentService.importStudents(file);

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
