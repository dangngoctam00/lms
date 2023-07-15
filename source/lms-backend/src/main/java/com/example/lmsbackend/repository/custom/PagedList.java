package com.example.lmsbackend.repository.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PagedList<T> {
    private List<T> listData;
    private final long totalSize;
    private final int page;
    private final int totalPages;
    private final int firstResult;
    private final int maxResults;

    public PagedList(List<T> listData, long totalSize, int firstResult, int maxResults) {
        this.listData = listData;
        this.totalSize = totalSize;
        this.page = (int)Math.floor((firstResult == -1 ? 0 : firstResult) * 1.0D / (double)maxResults) + 1; //NOSONAR
        this.totalPages = totalSize < 1L ? 0 : (int)Math.ceil(totalSize * 1.0D / (double)maxResults); //NOSONAR
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }
}
