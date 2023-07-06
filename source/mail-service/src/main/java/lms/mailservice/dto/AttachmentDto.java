package lms.mailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.ByteBuffer;

@AllArgsConstructor
@Getter
public class AttachmentDto {

    private String name;
    private ByteBuffer content;
    private String contentType;
}
