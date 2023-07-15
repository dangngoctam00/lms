package com.example.lmsbackend.websocket.controller;

import com.example.lmsbackend.criteria.AnnouncementCriteria;
import com.example.lmsbackend.criteria.PaginationCriterion;
import com.example.lmsbackend.dto.notification.AnnouncementDto;
import com.example.lmsbackend.dto.notification.AnnouncementPagedDto;
import com.example.lmsbackend.dto.notification.CreateAnnouncementRequest;
import com.example.lmsbackend.service.announcement.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("notification/{id}")
    public ResponseEntity<AnnouncementDto> getAnnouncementById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(id));
    }

    @PostMapping("/classes/{classId}/announcement")
    public ResponseEntity<AnnouncementDto> createAnnouncement(@PathVariable Long classId, @Valid @RequestBody CreateAnnouncementRequest dto) {
        return ResponseEntity.ok(announcementService.createAnnouncement(classId, dto));
    }

    @GetMapping("/classes/{classId}/announcement/received")
    public ResponseEntity<AnnouncementPagedDto> getReceivedAnnouncementByClass(@PathVariable Long classId,
                                                                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                               @RequestParam(value = "keyword", required = false) String keyword) {
        var criteria = new AnnouncementCriteria();
        criteria.setKeyword(keyword);
        criteria.setPagination(new PaginationCriterion(page, size));
        return ResponseEntity.ok(announcementService.getReceivedAnnouncementByClass(classId, criteria));
    }

    @GetMapping("/classes/{classId}/announcement/sent")
    public ResponseEntity<AnnouncementPagedDto> getSentAnnouncementByClass(@PathVariable Long classId,
                                                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                           @RequestParam(value = "keyword", required = false) String keyword) {
        var criteria = new AnnouncementCriteria();
        criteria.setKeyword(keyword);
        criteria.setPagination(new PaginationCriterion(page, size));
        return ResponseEntity.ok(announcementService.getSentAnnouncementByClass(classId, criteria));
    }

    @DeleteMapping("/announcement/received/{announcementId}")
    public ResponseEntity<Long> deleteReceivedAnnouncementByUser(@PathVariable Long announcementId) {
        return ResponseEntity.ok(announcementService.deleteReceivedAnnouncement(announcementId));
    }

    @DeleteMapping("/announcement/sent/{announcementId}")
    public ResponseEntity<Long> deleteSentAnnouncementByUser(@PathVariable Long announcementId) {
        return ResponseEntity.ok(announcementService.deleteSentAnnouncement(announcementId));
    }

    @PostMapping("/announcement/seen/{announcementId}")
    public ResponseEntity<AnnouncementDto> seenAnnouncement(@PathVariable Long announcementId) {
        return ResponseEntity.ok(announcementService.seenAnnouncement(announcementId));
    }

    @PostMapping("/announcement/unseen/{announcementId}")
    public ResponseEntity<AnnouncementDto> unseenAnnouncement(@PathVariable Long announcementId) {
        return ResponseEntity.ok(announcementService.unseenAnnouncement(announcementId));
    }
}
