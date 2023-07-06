package lms.mailservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class SendEmailDto {
    private PersonDto from;
    private List<PersonDto> to;
    private String subject;
    private String content;
    private List<String> attachments;
}
