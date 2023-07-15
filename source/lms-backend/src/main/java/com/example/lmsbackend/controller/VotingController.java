package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.classes.VotingChoiceDto;
import com.example.lmsbackend.dto.classes.VotingDto;
import com.example.lmsbackend.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PREFIX)
public class VotingController {

    private final VotingService votingService;

    @PostMapping("/classes/{classId}/learning_content/chapters/{chapterId}/voting")
    public ResponseEntity<VotingDto> createVoting(@PathVariable Long classId,
                                                  @PathVariable Long chapterId,
                                                  @Valid @RequestBody VotingDto dto) {

        return ResponseEntity.ok(votingService.createVoting(classId, chapterId, dto));
    }

    @GetMapping("/classes/{classId}/learning_content/voting/{votingId}")
    public ResponseEntity<VotingDto> getVoting(@PathVariable Long classId,
                                               @PathVariable Long votingId) {
        return ResponseEntity.ok(votingService.getVoting(votingId));
    }

    @PutMapping("/classes/{classId}/learning_content/voting/{votingId}")
    public ResponseEntity<VotingDto> updateVoting(@PathVariable Long classId,
                                                  @PathVariable Long votingId,
                                                  @Valid @RequestBody VotingDto dto) {
        return ResponseEntity.ok(votingService.updateVoting(votingId, dto));
    }

    @DeleteMapping("/classes/{classId}/learning_content/voting/{votingId}")
    public ResponseEntity<Long> deleteVoting(@PathVariable Long classId,
                                             @PathVariable Long votingId) {
        return ResponseEntity.ok(votingService.deleteVoting(votingId));
    }

    @PatchMapping("/classes/{classId}/learning_content/voting/{voteId}/choices/{choiceId}")
    public ResponseEntity<VotingDto> choseVotingChoice(@PathVariable Long classId,
                                                       @PathVariable Long voteId,
                                                       @PathVariable Long choiceId) {
        return ResponseEntity.ok(votingService.choseVotingChoice(voteId, choiceId));
    }


    @PostMapping("/classes/{classId}/learning_content/voting/{votingId}/choices")
    public ResponseEntity<VotingDto> addVotingChoice(@PathVariable Long classId,
                                                     @PathVariable Long votingId,
                                                     @Valid @RequestBody VotingChoiceDto dto) {
        return ResponseEntity.ok(votingService.addVotingChoice(votingId, dto));
    }

    @PutMapping("/classes/{classId}/learning_content/voting/{votingId}/choices/{choiceId}")
    public ResponseEntity<VotingDto> updateVotingChoice(@PathVariable Long classId,
                                                        @PathVariable Long votingId,
                                                        @PathVariable Long choiceId,
                                                        @Valid @RequestBody VotingChoiceDto dto) {
        return ResponseEntity.ok(votingService.updateVotingChoice(votingId, choiceId, dto));
    }

    @DeleteMapping("/classes/{classId}/learning_content/voting/{votingId}/choices/{choiceId}")
    public ResponseEntity<VotingDto> deleteVotingChoice(@PathVariable Long classId,
                                                        @PathVariable Long votingId,
                                                        @PathVariable Long choiceId) {
        return ResponseEntity.ok(votingService.deleteVotingChoice(votingId, choiceId));
    }

}
