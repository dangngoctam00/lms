package com.example.lmsbackend.mapper;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.dto.exam.QuestionAnswerPagedResponseDto;
import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperUtils {

    public static <T extends AbstractPagedDto> void mapPagedDto(T target, PagedList<?> pagedList) {
        target.setPage(pagedList.getPage());
        target.setPerPage(pagedList.getSize());
        target.setTotal(pagedList.getTotalSize());
        target.setTotalPages(pagedList.getTotalPages());
    }

    public static <T extends AbstractPagedDto> void mapPagedDto(T target, Page<?> page) {
        // +1 because result of jpa pagination is index-based 0
        target.setPage(page.getPageable().getPageNumber() + 1);
        target.setPerPage(page.getNumberOfElements());
        target.setTotal(page.getTotalElements());
        target.setTotalPages(page.getTotalPages());
    }

    public static <T extends AbstractPagedDto> void mapPagedDto(T target, com.example.lmsbackend.repository.custom.PagedList<?> pagedList) {
        target.setPage(pagedList.getPage());
        target.setPerPage(pagedList.getListData().size());
        target.setTotal(pagedList.getTotalSize());
        target.setTotalPages(pagedList.getTotalPages());
    }

    public static <T extends AbstractPagedDto> void getDefaultPagedDto(T target) {
        target.setPage(1);
        target.setPerPage(0);
        target.setTotal(0L);
        target.setTotalPages(1);
    }

    public static <T extends  AbstractPagedDto> void mapPagedDto(T target, QuestionAnswerPagedResponseDto pagedList) {
        target.setPage(pagedList.getPage());
        target.setPerPage(pagedList.getPerPage());
        target.setTotal(pagedList.getTotal());
        target.setTotalPages(pagedList.getTotalPages());
    }
}
