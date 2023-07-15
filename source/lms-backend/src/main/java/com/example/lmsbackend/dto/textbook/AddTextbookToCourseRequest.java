package com.example.lmsbackend.dto.textbook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddTextbookToCourseRequest {

    private List<Long> idList;
}
