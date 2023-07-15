package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.exam.GradeFormulaEntity;
import com.example.lmsbackend.domain.exam.GradeTag;
import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.dto.grade_formula.GradeTagDetailsDTO;
import com.example.lmsbackend.dto.request.grade_formula.CreateGradeFormulaRequest;
import com.example.lmsbackend.dto.request.grade_formula.UpdateGradeFormulaRequest;
import com.example.lmsbackend.dto.response.grade_formula.GetGradeFormulaResponse;
import com.example.lmsbackend.dto.tag.TagDTO;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.aclass.ClassNotFoundException;
import com.example.lmsbackend.exceptions.grade_formula.NotFoundGradeFormulaException;
import com.example.lmsbackend.exceptions.grade_formula.PrimitiveTagException;
import com.example.lmsbackend.mapper.GradeFormulaMapper;
import com.example.lmsbackend.mapper.GradeTagMapper;
import com.example.lmsbackend.repository.ClassRepository;
import com.example.lmsbackend.repository.GradeTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeFormulaService {
    private final GradeTagMapper gradeTagMapper;
    private final GradeFormulaMapper gradeFormulaMapper;
    private final EntityManager entityManager;
    private final GradeTagRepository gradeTagRepository;

    private final ClassRepository classRepository;


    @Transactional
    @Auth(permission = {PermissionEnum.CREATE_GRADE_TAG_IN_CLASS})
    public Long createGradeFormula(CreateGradeFormulaRequest request, long resourceId) {
        GradeTag gradeTagResult = gradeTagMapper.mapFromCreateGradeFormulaRequest(request);
        GradeFormulaEntity gradeFormula = gradeFormulaMapper.mapFromCreateGradeFormulaRequest(request);
        gradeFormula.setUseTags(
                request.getUseTags().stream()
                        .map(tagId -> entityManager.getReference(GradeTag.class, tagId))
                        .collect(Collectors.toList())
        );
        gradeTagResult = entityManager.merge(gradeTagResult);
        gradeFormula.setTagResult(gradeTagResult);
        gradeTagResult.setGradeFormula(entityManager.merge(gradeFormula));
        gradeTagResult.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        gradeTagRepository.save(gradeTagResult);
        return gradeTagResult.getId();
    }

    @Transactional
    @Auth(permission = {PermissionEnum.VIEW_DETAIL_GRADE_TAG})
    public List<TagDTO> getAllTagsInScope(GradeTagScope scope, Long resourceId) {
        List<GradeTag> gradeTagEntities = gradeTagRepository.getAllTagsInScope(scope, resourceId);
        if (scope == GradeTagScope.CLASS) {
            ClassEntity classEntity = classRepository.getById(resourceId);
            List<GradeTag> gradeTagEntitiesInCourse = gradeTagRepository.getAllTagsInScope(GradeTagScope.COURSE, classEntity.getCourse().getId());
            gradeTagEntities.addAll(gradeTagEntitiesInCourse);
        }
        return gradeTagEntities.stream().map(gradeTagMapper::mapToTagDTO).collect(Collectors.toList());
    }

    @Transactional
    @Auth(permission = {PermissionEnum.VIEW_DETAIL_GRADE_TAG})
    public GetGradeFormulaResponse getGradeFormula(Long resourceId) {
        Optional<GradeTag> gradeTagOptional = gradeTagRepository.findById(resourceId);
        if (gradeTagOptional.isPresent()) {
            GradeTag gradeTag = gradeTagOptional.get();
            if (gradeTag.isPrimitive()) {
                throw new PrimitiveTagException();
            }
            GetGradeFormulaResponse response = new GetGradeFormulaResponse();
            response.setId(gradeTag.getId());
            response.setFormula(gradeTag.getGradeFormula().getFormula());
            response.setTagTitle(gradeTag.getTitle());
            response.setPublic(gradeTag.isPublic());
            response.setUseTags(gradeTag.getGradeFormula().getUseTags().stream().map(GradeTag::getId).collect(Collectors.toList()));
            return response;
        } else {
            throw new NotFoundGradeFormulaException();
        }
    }

    @Transactional
    @Auth(permission = {PermissionEnum.UPDATE_GRADE_TAG})
    public Long updateGradeFormula(UpdateGradeFormulaRequest request, long resourceId) {
        Optional<GradeTag> gradeTagOptional = gradeTagRepository.findById(request.getId());
        if (gradeTagOptional.isPresent()) {
            GradeTag gradeTag = gradeTagOptional.get();
            if (gradeTag.isPrimitive()) {
                throw new PrimitiveTagException();
            }
            gradeTag.setTitle(request.getTagTitle());
            gradeTag.getGradeFormula().setUseTags(
                    request.getUseTags().stream()
                            .map(tagId -> entityManager.getReference(GradeTag.class, tagId))
                            .collect(Collectors.toList())
            );
            gradeTag.getGradeFormula().setFormula(request.getFormula());
            gradeTag.getGradeFormula().setExpression(request.getExpression());
            gradeTag.setPublic(request.getIsPublic());
            gradeTagRepository.save(gradeTag);
            return gradeTag.getId();
        } else {
            throw new NotFoundGradeFormulaException();
        }
    }

    @Auth(permission = {PermissionEnum.VIEW_LIST_GRADE_TAG_IN_CLASS})
    public List<GradeTagDetailsDTO> getAllGradeTagsDetails(GradeTagScope scope, Long resourceId) {
        ClassEntity classEntity = classRepository.findById(resourceId)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));

        List<GradeTag> gradeTags = gradeTagRepository.getAllGradeTagInClass(resourceId, classEntity.getCourse().getId());
        return gradeTags.stream().map(gradeTagMapper::mapToGrateTagDetailsDTO).collect(Collectors.toList());
    }
}
