package com.example.lmsbackend.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRoomRequest {
    @Size(min = 3)
    List<Long> usersId;
    String name;

    @Pattern(regexp = "USER | GROUP")
    String type;
}
