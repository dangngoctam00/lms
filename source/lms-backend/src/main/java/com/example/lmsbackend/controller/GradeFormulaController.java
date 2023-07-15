package com.example.lmsbackend.controller;

import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.dto.grade_formula.GradeTagDetailsDTO;
import com.example.lmsbackend.dto.request.grade_formula.CreateGradeFormulaRequest;
import com.example.lmsbackend.dto.request.grade_formula.UpdateGradeFormulaRequest;
import com.example.lmsbackend.dto.response.grade_formula.*;
import com.example.lmsbackend.dto.tag.TagDTO;
import com.example.lmsbackend.service.GradeFormulaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class GradeFormulaController {
    private final GradeFormulaService gradeFormulaService;

    @PostMapping("/gradeFormula")
    public ResponseEntity<CreateGradeFormulaResponse> createGradeFormula(@RequestBody CreateGradeFormulaRequest request) {
        Long tagId = gradeFormulaService.createGradeFormula(request, request.getScopeId());
        CreateGradeFormulaResponse response = new CreateGradeFormulaResponse();
        response.setTagId(tagId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/gradeFormula/{tagId}")
    public ResponseEntity<GetGradeFormulaResponse> getGradeFormula(@PathVariable Long tagId) {
        GetGradeFormulaResponse response = gradeFormulaService.getGradeFormula(tagId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tag")
    public ResponseEntity<GetAllTagsInScopeResponse> getAllTagsInScope(@RequestParam(name = "scope") GradeTagScope scope, @RequestParam(name = "scopeId") Long scopeId) {
        List<TagDTO> tags = gradeFormulaService.getAllTagsInScope(scope, scopeId);
        GetAllTagsInScopeResponse response = new GetAllTagsInScopeResponse();
        response.setTags(tags);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/gradeFormula")
    public ResponseEntity<UpdateGradeFormulaResponse> updateGradeFormula(@RequestBody UpdateGradeFormulaRequest request) {
        Long tagId = gradeFormulaService.updateGradeFormula(request, request.getId());
        UpdateGradeFormulaResponse response = new UpdateGradeFormulaResponse();
        response.setTagId(tagId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/grade-tags-details")
    public ResponseEntity<GetAllGradeTagsDetailsResponse> getAllGradeTagsDetails(@RequestParam(name = "scope") GradeTagScope scope, @RequestParam(name = "scopeId") Long scopeId){
        List<GradeTagDetailsDTO> gradeTags = gradeFormulaService.getAllGradeTagsDetails(scope, scopeId);
        GetAllGradeTagsDetailsResponse response = new GetAllGradeTagsDetailsResponse();
        response.setTags(gradeTags);
        return ResponseEntity.ok(response);
    }
}
