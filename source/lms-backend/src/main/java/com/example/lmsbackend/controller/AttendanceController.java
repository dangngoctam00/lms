package com.example.lmsbackend.controller;


import com.example.lmsbackend.dto.classes.*;
import com.example.lmsbackend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/classes/{classId}/overall_attendance")
    public ResponseEntity<OverallAttendanceDto> getOverallAttendance(@PathVariable Long classId) {
        return ResponseEntity.ok(attendanceService.getOverallAttendance(classId));
    }

    @PostMapping("/classes/{classId}/attendance/{sessionId}")
    public ResponseEntity<?> createNewAttendanceOfSession(@PathVariable Long classId, @PathVariable Long sessionId) {
        attendanceService.createNewAttendanceOfSession(classId, sessionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/classes/{classId}/attendance/session/{sessionId}/batch")
    public ResponseEntity<?> changeStudentAttendanceBatchBySession(@PathVariable Long classId,
                                                                   @PathVariable Long sessionId,
                                                                   @Valid @RequestBody StudentAttendanceStateDto dto) {
        attendanceService.changeStudentAttendanceBatchBySession(classId, sessionId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/classes/{classId}/attendance/session/{sessionId}/students/{studentId}")
    public ResponseEntity<?> changeStudentAttendanceBySession(@PathVariable Long classId,
                                                              @PathVariable Long sessionId,
                                                              @PathVariable Long studentId,
                                                              @Valid @RequestBody StudentAttendanceStateDto dto) {
        attendanceService.changeStudentAttendanceBySession(classId, sessionId, studentId, dto);
        return null;
    }

    @PostMapping("/classes/{classId}/attendance/session/{sessionId}/time/{timeId}/batch")
    public ResponseEntity<?> changeStudentAttendanceBatchByTimeAndSession(@PathVariable Long classId,
                                                                          @PathVariable Long sessionId,
                                                                          @PathVariable Long timeId,
                                                                          @Valid @RequestBody StudentAttendanceStateDto dto) {
        attendanceService.changeStudentAttendanceBatchByTimeAndSession(classId, sessionId, timeId, dto);
        return null;
    }

    @PostMapping("/classes/{classId}/attendance/session/{sessionId}/time/{timeId}/students/{studentId}")
    public ResponseEntity<?> changeStudentAttendanceByTimeAndSession(@PathVariable Long classId,
                                                                     @PathVariable Long sessionId,
                                                                     @PathVariable Long timeId,
                                                                     @PathVariable Long studentId,
                                                                     @Valid @RequestBody StudentAttendanceStateDto dto) {
        attendanceService.changeStudentAttendanceByTimeAndSession(classId, studentId, sessionId, timeId, dto);
        return null;
    }

    @GetMapping("/classes/{classId}/attendance/session/{sessionId}")
    public ResponseEntity<MeetingAttendanceDto> getMeetingAttendance(@PathVariable Long classId, @PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getMeetingAttendance(sessionId));
    }

    @DeleteMapping("/classes/{classId}/attendance/session/{sessionId}/time/{timeId}")
    public ResponseEntity<Long> deleteAttendanceTime(@PathVariable Long classId, @PathVariable Long sessionId, @PathVariable Long timeId) {
        attendanceService.deleteAttendanceTime(sessionId, timeId);
        return ResponseEntity.ok(timeId);
    }

    @PostMapping("/classes/{classId}/attendance/session/{sessionId}/strategy")
    public ResponseEntity<?> changeAttendanceSessionStrategy(@PathVariable Long classId, @PathVariable Long sessionId, @Valid @RequestBody AttendanceSessionStrategyDto dto) {
        attendanceService.changeAttendanceSessionStrategy(sessionId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/classes/{classId}/attendance/session/{sessionId}/note")
    public ResponseEntity<?> addNoteOfSessionAttendance(@PathVariable Long classId,
                                                        @PathVariable Long sessionId,
                                                        @Valid @RequestBody AttendanceSessionNoteDto dto) {

        attendanceService.addAttendanceSessionNote(sessionId, dto);
        return ResponseEntity.ok().build();
    }

}
