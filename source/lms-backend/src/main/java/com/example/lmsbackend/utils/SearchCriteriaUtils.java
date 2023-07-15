package com.example.lmsbackend.utils;

import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.criteria.FilterCriterion;
import com.example.lmsbackend.criteria.PaginationCriterion;
import com.example.lmsbackend.criteria.SortCriterion;
import com.example.lmsbackend.exceptions.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.example.lmsbackend.constant.AppConstant.PARAM_DELIMITER;
import static com.example.lmsbackend.constant.AppConstant.VALUE_DELIMITER;
import static java.util.stream.Collectors.toList;

@Component
public class SearchCriteriaUtils {

    public BaseSearchCriteria buildSearchCriteria(String keyword, String filter, String sort, Integer page, Integer size) {
        var criteria = new BaseSearchCriteria();
        if (StringUtils.isNoneBlank(sort)) {
            criteria.setSorts(splitSortCriteria(sort));
        }
        if (StringUtils.isNoneBlank(filter)) {
            criteria.setFilters(splitFilterCriteria(filter));
        }
        if (StringUtils.isNoneBlank(keyword)) {
            criteria.setKeyword(keyword);
        }
        criteria.setPagination(new PaginationCriterion(page, size));
        return criteria;
    }

    public List<SortCriterion> splitSortCriteria(String sort) {
        var split = StringUtils.split(sort, PARAM_DELIMITER);
        if (split.length % 2 == 1) {
            throw new InvalidParameterException(sort);
        }
        return IntStream.range(0, split.length)
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> new SortCriterion(split[i], split[i + 1]))
                .collect(toList());
    }

    public List<FilterCriterion> splitFilterCriteria(String filter) {
        var split = StringUtils.split(filter, PARAM_DELIMITER);
        if (split.length % 2 == 1) {
            throw new InvalidParameterException(filter);
        }
        return IntStream.range(0, split.length)
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> new FilterCriterion(split[i], Arrays.asList(StringUtils.split(split[i + 1], VALUE_DELIMITER))))
                .collect(toList());
    }
}
