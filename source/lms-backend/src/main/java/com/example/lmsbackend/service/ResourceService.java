package com.example.lmsbackend.service;

import com.example.lmsbackend.dto.resource.TextBookPagedDto;
import com.example.lmsbackend.dto.resource.TextbookDto;
import com.example.lmsbackend.exceptions.resource.TextbookNotFoundException;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.TextBookMapper;
import com.example.lmsbackend.repository.TextbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final TextbookRepository textbookRepository;
    private final TextBookMapper textBookMapper;


    @Transactional(readOnly = true)
    @Cacheable("textbooks")
    public TextBookPagedDto getTextbooks(String keyword, Integer page, Integer size) {
        var textbooks = textbookRepository.getTextBooks(keyword, page, size);
        var textbooksPagedDto = new TextBookPagedDto();
        MapperUtils.mapPagedDto(textbooksPagedDto, textbooks);
        textbooksPagedDto.setListData(
                textbooks
                        .stream()
                        .map(textBookMapper::mapToTextBookDto)
                        .collect(toList())
        );
        return textbooksPagedDto;
    }

    @Transactional
    public TextbookDto createTextbook(TextbookDto dto) {
        var entity = textBookMapper.mapToTextBookEntity(dto);
        textbookRepository.save(entity);

        dto.setId(entity.getId());
        return dto;
    }

    @Transactional
    public TextbookDto updateTextbook(Long textbookId, TextbookDto dto) {
        var entity = textbookRepository.findById(textbookId)
                .orElseThrow(() -> new TextbookNotFoundException(textbookId));
        textBookMapper.mapToTextBookEntity(entity, dto);
        return dto;
    }

    @Transactional
    public Long deleteTextbook(Long textbookId) {
        var textbook = textbookRepository.findById(textbookId);
        if (textbook.isPresent()) {
            textbookRepository.deleteById(textbookId);
        }
        return textbookId;
    }

    @Transactional(readOnly = true)
    @Cacheable("textbook")
    public TextbookDto findTextbook(Long id) {
        var textbook = textbookRepository.findById(id)
                .orElseThrow(() -> new TextbookNotFoundException(id));
        return textBookMapper.mapToTextBookDto(textbook);
    }
}
