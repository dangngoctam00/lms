package com.example.lmsbackend.config.security.aop.rule;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.PermissionSecurity;
import com.example.lmsbackend.constant.LimitValue;
import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.enums.ResourceType;
import com.example.lmsbackend.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StaffRule implements Rule {
    private final StaffRepository staffRepository;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.STAFF;
    }

    @Override
    public boolean authorize(CustomUserDetails currUser, PermissionSecurity permission, long staffId) {
        if (permission.getIsLimitByManager() == LimitValue.LIMIT
                && !checkLimitByManager(currUser, staffId)
        ) return false;
        return true;
    }

    private boolean checkLimitByManager(CustomUserDetails currUser, long staffId) {
        Optional<StaffEntity> staffEntityOptional = staffRepository.findById(staffId);
        return staffEntityOptional.isPresent()
                && staffEntityOptional.get().getManager().getId().equals(currUser.getId());
    }
}
