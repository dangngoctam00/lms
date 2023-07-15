package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.request.program.CreateProgramDto;
import com.example.lmsbackend.dto.request.program.UpdateProgramDto;
import com.example.lmsbackend.dto.response.program.ProgramDto;
import com.example.lmsbackend.dto.response.program.ProgramPagedDto;
import com.example.lmsbackend.service.ProgramService;
import com.example.lmsbackend.utils.SearchCriteriaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;
    private final SearchCriteriaUtils searchCriteriaUtils;

    @GetMapping("/programs")
    public ResponseEntity<ProgramPagedDto> getPrograms(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "filter", required = false) String filter,
                                                       @RequestParam(value = "sort", required = false) String sort) {

        return ResponseEntity.ok(programService.getPrograms(searchCriteriaUtils.buildSearchCriteria(keyword, filter, sort, page, size)));
    }

    @GetMapping("/programs/{id}")
    public ResponseEntity<ProgramDto> getProgram(@PathVariable("id") Long id) {
        return ResponseEntity.ok(programService.getProgram(id));
    }

    @PostMapping("/programs")
    public ResponseEntity<ProgramDto> createProgram(@RequestBody CreateProgramDto dto) {
        return ResponseEntity.ok(programService.createProgram(dto));
    }

    @PutMapping("/programs/{id}")
    public ResponseEntity<ProgramDto> updateProgram(@RequestBody UpdateProgramDto dto) {
        return ResponseEntity.ok(programService.updateProgram(dto));
    }

    @DeleteMapping("/programs/{id}")
    public ResponseEntity<Long> deleteProgram(@PathVariable("id") Long id) {
        return ResponseEntity.ok(programService.deleteProgram(id));
    }
}
