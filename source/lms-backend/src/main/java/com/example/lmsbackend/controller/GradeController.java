package com.example.lmsbackend.controller;

import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.dto.classes.ExportGradeDto;
import com.example.lmsbackend.dto.request.grade.GradeRequest;
import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.response.grade.GetGradeResultsResponse;
import com.example.lmsbackend.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping("/tags/grade")
    public ResponseEntity<BaseResponse> grade(@RequestBody GradeRequest request){
        gradeService.grade(request.getTagId());
        return ResponseEntity.ok(new BaseResponse());
    }

    @PostMapping("/tags/{tagId}/public/{isPublic}")
    public ResponseEntity<BaseResponse> publicGradeTag(@PathVariable("tagId") long tagId, @PathVariable("isPublic") boolean isPublic){
        gradeService.publicGradeTag(tagId, isPublic);
        return ResponseEntity.ok(new BaseResponse());
    }

    @GetMapping("/grades/result")
    public ResponseEntity<GetGradeResultsResponse> getGradeResults(@RequestParam(name = "scope") GradeTagScope scope, @RequestParam(name = "scopeId") Long scopeId){
        GetGradeResultsResponse response = gradeService.getGradeResult(scope, scopeId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<BaseResponse> deleteGradeTag(@PathVariable(name = "tagId") long tagId) {
        gradeService.deleteGradeTag(tagId);
        return ResponseEntity.ok(new BaseResponse());
    }

    @PostMapping(value = "/quizzes/grades", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportGrades(@RequestBody ExportGradeDto dto) throws JRException {
        return ResponseEntity.ok().body(gradeService.exportGrades(dto));
    }
}
