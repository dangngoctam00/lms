package com.example.lmsbackend.service;

import com.example.lmsbackend.domain.exam.GradeTag;
import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.example.lmsbackend.repository.GradeTagRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class UtilsService {

    private final GradeTagRepository gradeTagRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public GradeTag persistScoreTag(String title, Long scopeId, GradeTagScope scope) {
        var tag = new GradeTag();
        tag.setTitle(title);
        tag.setScopeId(scopeId);
        tag.setScope(scope);
        tag.setHasGraded(false);
        tag.setPrimitive(true);
        tag.setPublic(false);
        gradeTagRepository.save(tag);
        return tag;
    }

    public String getTenantName() {
        var tenantId = TenantContext.getTenantId();
        var tenantCustomizeNameList = entityManager.createNativeQuery("SELECT p.name FROM public.tenant_customize p WHERE p.tenant_id = :tenantId")
                .setParameter("tenantId", tenantId)
                .getResultList();
        if (CollectionUtils.isNotEmpty(tenantCustomizeNameList)) {
            return (String) tenantCustomizeNameList.get(0);
        }
        return "BKLMS";
    }
}
