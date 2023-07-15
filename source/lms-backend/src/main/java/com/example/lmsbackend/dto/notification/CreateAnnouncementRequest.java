package com.example.lmsbackend.dto.notification;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CreateAnnouncementRequest {
    @NotNull(message = "This a is required field")
    private Long senderId;
    private String subject;
    @NotBlank(message = "This a is required field")
    private String content;
    private String attachment;
    private Boolean sendMailAsCopy;
    private String tags;
    // if empty -> send to all
    private List<Long> receiversId;
}
