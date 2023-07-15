package com.example.lmsbackend.criteria;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class BaseSearchCriteria {
    private String keyword;
    private List<FilterCriterion> filters = new ArrayList<>();
    private List<SortCriterion> sorts = new ArrayList<>();
    private PaginationCriterion pagination;

    public BaseSearchCriteria() {
        pagination = new PaginationCriterion(0, 0);
    }

    @Override
    public String toString() {
        String result = "keyword="
                .concat(keyword != null ? keyword : "");
        if (filters != null) {
            result = result.concat("&filter")
                    .concat(filters.stream().map(filterCriterion -> filterCriterion.getField().concat("=").concat(String.join(",", filterCriterion.getValues()))).collect(Collectors.joining("&")));
        }
        if (sorts != null) {
            result = result.concat("&sorts")
                    .concat(sorts.stream().map(sortCriterion -> sortCriterion.getField().concat("=").concat(sortCriterion.getDirection())).collect(Collectors.joining("&")));
        }
        if (pagination != null) {
            result = result.concat("&page=").concat(pagination.getPage().toString())
                    .concat("&size=").concat(pagination.getSize().toString());
        }
        return result;
    }
}
