package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.resource.TextBookPagedDto;
import com.example.lmsbackend.dto.resource.TextbookDto;
import com.example.lmsbackend.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @GetMapping("/resources/textbook")
    public ResponseEntity<TextBookPagedDto> findTextbooks(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(resourceService.getTextbooks(keyword, page, size));
    }

    @GetMapping("/resources/textbook/{id}")
    public ResponseEntity<TextbookDto> findTextbook(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.findTextbook(id));
    }

    @PostMapping("/resources/textbook")
    public ResponseEntity<TextbookDto> createTextbook(@Valid @RequestBody TextbookDto dto) {
        return ResponseEntity.ok(resourceService.createTextbook(dto));
    }

    @PutMapping("/resources/textbook/{textbookId}")
    public ResponseEntity<TextbookDto> updateTextbook(@PathVariable Long textbookId, @Valid @RequestBody TextbookDto dto) {
        return ResponseEntity.ok(resourceService.updateTextbook(textbookId, dto));
    }

    @DeleteMapping("/resources/textbook/{textbookId}")
    public ResponseEntity<Long> deleteTextbook(@PathVariable Long textbookId) {
        return ResponseEntity.ok(resourceService.deleteTextbook(textbookId));
    }
}
