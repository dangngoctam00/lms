package com.example.lmsbackend.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitPermission {
    private boolean limitByBranch;
    private boolean limitByTeaching;
    private boolean limitByDean;
    private boolean limitByManager;
    private boolean limitByLearn;
}
