package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.calendar.EventDto;
import com.example.lmsbackend.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;
import static com.example.lmsbackend.constant.AppConstant.PARAM_DELIMITER;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class CalendarController {

    private final EventService eventService;

    @GetMapping("/calendars/user/{userId}")
    public ResponseEntity<List<EventDto>> getEventsByUserAndClass(@PathVariable Long userId, @RequestParam(value = "class", defaultValue = "") String classIdParam) {
        var classIdList = Arrays.stream(StringUtils.split(classIdParam, PARAM_DELIMITER))
                .map(Long::valueOf)
                .collect(toList());
        return ResponseEntity.ok(eventService.getEventsByUserAndClass(userId, classIdList));
    }

    @GetMapping("/classes/{classId}/calendars")
    public ResponseEntity<List<EventDto>> getEventsByAndClass(@PathVariable Long classId) {
        return ResponseEntity.ok(eventService.getEventsByClass(classId));
    }
}
