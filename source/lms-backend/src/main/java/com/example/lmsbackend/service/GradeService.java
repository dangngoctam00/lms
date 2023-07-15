package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.classmodel.ClassStudentEntity;
import com.example.lmsbackend.domain.exam.GradeFormulaEntity;
import com.example.lmsbackend.domain.exam.GradeTag;
import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.domain.exam.GradeTagStudentEntity;
import com.example.lmsbackend.dto.classes.ExportGradeDto;
import com.example.lmsbackend.dto.response.grade.GetGradeResultsResponse;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.GradeTagIsPrimitiveException;
import com.example.lmsbackend.exceptions.aclass.ClassNotFoundException;
import com.example.lmsbackend.exceptions.aclass.GradeTagNotFoundException;
import com.example.lmsbackend.exceptions.grade_formula.NotEnoughGradeException;
import com.example.lmsbackend.exceptions.grade_formula.NotFoundGradeFormulaException;
import com.example.lmsbackend.exceptions.grade_formula.PrimitiveTagException;
import com.example.lmsbackend.mapper.GradeTagMapper;
import com.example.lmsbackend.repository.ClassRepository;
import com.example.lmsbackend.repository.ClassStudentRepository;
import com.example.lmsbackend.repository.GradeTagRepository;
import com.example.lmsbackend.repository.GradeTagStudentRepository;
import com.example.lmsbackend.service.export.DynamicColumnDataSource;
import com.example.lmsbackend.service.export.DynamicReportBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.lmsbackend.service.export.DynamicReportBuilder.DETAIL_BAND_HEIGHT;
import static com.example.lmsbackend.service.export.DynamicReportBuilder.HEADER_BAND_HEIGHT;
import static java.util.stream.Collectors.*;
import static net.sf.jasperreports.engine.design.JasperDesign.PROPERTY_PAGE_HEIGHT;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeService {
    private final GradeTagMapper gradeTagMapper;
    private final GradeTagRepository gradeTagRepository;
    private final GradeTagStudentRepository gradeTagStudentRepository;
    private final EntityManager entityManager;
    private final ClassRepository classRepository;
    private final ClassStudentRepository classStudentRepository;

    private final UserService userService;

    @Transactional
    @Auth(permission = {PermissionEnum.CALCULATE_GRADE_TAG})
    public void grade(Long resourceId) {
        GradeTag gradeTag = gradeTagRepository.findById(resourceId).orElseThrow(NotFoundGradeFormulaException::new);
        if (gradeTag.isPrimitive()) {
            gradeTag.setGradedAt(Timestamp.valueOf(LocalDateTime.now()));
            gradeTag.setHasGraded(true);
        } else {
            GradeFormulaEntity gradeFormula = gradeTag.getGradeFormula();

            Set<String> usedTags = gradeFormula.getUseTags().stream().map(tag -> "tag_".concat(tag.getId().toString())).collect(Collectors.toSet());

            List<GradeTag> gradeTagsNotGrade = gradeFormula.getUseTags().stream().filter(tag -> !tag.isHasGraded()).collect(toList());

            if (!gradeTagsNotGrade.isEmpty()) {
                throw new NotEnoughGradeException(gradeTagsNotGrade.stream().map(GradeTag::getTitle).collect(toList()));
            }

            Expression expression = new ExpressionBuilder(gradeFormula.getExpression())
                    .variables(usedTags)
                    .build();

            /*
             * {
             *   studentId: {
             *       tag_id: grade
             *   }
             * }
             * */
            Map<Long, Map<String, Double>> gradesMap = buildGradesMap(gradeFormula);

            List<GradeTagStudentEntity> results = new ArrayList<>();
            gradesMap.forEach((studentId, gradeMap) -> {
                expression.setVariables(gradeMap);
                results.add(entityManager.merge(
                        GradeTagStudentEntity.builder()
                                .student(entityManager.getReference(StudentEntity.class, studentId))
                                .grade(expression.evaluate())
                                .tag(gradeTag)
                                .build()
                ));
            });
            gradeTagStudentRepository.saveAll(results);
            gradeTag.setGradedAt(Timestamp.valueOf(LocalDateTime.now()));
            gradeTag.setHasGraded(true);
            gradeTagRepository.save(gradeTag);
        }
    }

    private Map<Long, Map<String, Double>> buildGradesMap(GradeFormulaEntity gradeFormula) {
        Map<Long, Map<String, Double>> gradesMap = new HashMap<>();
        gradeFormula.getUseTags().forEach(tag -> {
            List<GradeTagStudentEntity> gradeTagStudents = gradeTagStudentRepository.findAllByTag_Id(tag.getId());
            gradeTagStudents.forEach(gradeTagStudentEntity -> {
                if (!gradesMap.containsKey(gradeTagStudentEntity.getStudent().getId())) {
                    gradesMap.put(gradeTagStudentEntity.getStudent().getId(), new HashMap<>());
                }
                gradesMap.get(gradeTagStudentEntity.getStudent().getId())
                        .put("tag_".concat(tag.getId().toString()), gradeTagStudentEntity.getGrade());
            });
        });
        return gradesMap;
    }

    @Transactional
    @Auth(permission = {PermissionEnum.VIEW_GRADE_RESULT_IN_CLASS})
    @Cacheable("grade_result")
    public GetGradeResultsResponse getGradeResult(GradeTagScope scope, Long resourceId) {
        UserEntity currentUser = userService.getCurrentUser();

        Map<Long, List<GetGradeResultsResponse.Grade>> studentGradesMap = new HashMap<>();
        Map<Long, StudentEntity> studentsMap = new HashMap<>();
        ClassEntity classEntity = classRepository.findById(resourceId)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));

        List<ClassStudentEntity> classStudentEntities = classStudentRepository.getStudentsByClassId(resourceId);
        List<Long> studentIds = classStudentEntities.stream().map(classStudentEntity -> classStudentEntity.getStudent().getId()).collect(toList());

        List<GradeTag> gradeTags;
        if (currentUser.getAccountType() == AccountTypeEnum.STUDENT){
            gradeTags = gradeTagRepository.getAllGradeTagShowedInClass(resourceId, classEntity.getCourse().getId());
        } else {
            gradeTags = gradeTagRepository.getAllGradeTagInClass(resourceId, classEntity.getCourse().getId());
        }
        var studentIdList = classStudentRepository.getStudentsIdByClassId(classEntity.getId());
        gradeTags.forEach(gradeTag -> {

            List<GradeTagStudentEntity> gradeTagStudentEntities;

            if (currentUser.getAccountType() == AccountTypeEnum.STUDENT) {
                gradeTagStudentEntities = gradeTagStudentRepository.findAllByTag_IdAndStudent_Id(gradeTag.getId(), currentUser.getId());
            } else {
                gradeTagStudentEntities = gradeTagStudentRepository.findAllByStudent_IdInAndTag_Id(studentIds, gradeTag.getId());
            }
            for (GradeTagStudentEntity gradeTagStudentEnt : gradeTagStudentEntities) {
                if (!studentGradesMap.containsKey(gradeTagStudentEnt.getStudent().getId())) {
                    studentGradesMap.put(gradeTagStudentEnt.getStudent().getId(), new ArrayList<>());
                    studentsMap.put(gradeTagStudentEnt.getStudent().getId(), gradeTagStudentEnt.getStudent());
                }
                studentGradesMap.get(gradeTagStudentEnt.getStudent().getId())
                        .add(
                                GetGradeResultsResponse.Grade.builder()
                                        .tagId(gradeTagStudentEnt.getTag().getId())
                                        .tagTitle(gradeTagStudentEnt.getTag().getTitle())
                                        .grade(gradeTagStudentEnt.getGrade())
                                        .build()
                        );
            }
        });
        List<GetGradeResultsResponse.GradeResult> results = new ArrayList<>();
        studentsMap.forEach((studentId, studentEntity) -> results.add(
                GetGradeResultsResponse.GradeResult.builder()
                        .studentId(studentId)
                        .studentName(studentEntity.getLastName().concat(" ").concat(studentEntity.getFirstName()))
                        .avatar(studentEntity.getAvatar())
                        .grades(studentGradesMap.get(studentId))
                        .build()
        ));
        GetGradeResultsResponse response = new GetGradeResultsResponse();
        response.setGradeResults(results);
        response.setTags(gradeTags.stream().map(gradeTagMapper::mapToTagDTO).collect(toList()));
        return response;
    }

    public void publicGradeTag(long tagId, boolean isPublic) {
        GradeTag tagEntity = gradeTagRepository.findById(tagId)
                .orElseThrow(GradeTagNotFoundException::new);

        tagEntity.setPublic(isPublic);
        gradeTagRepository.save(tagEntity);
    }

    @Transactional
    @Auth(permission = {PermissionEnum.DELETE_GRADE_TAG})
    public void deleteGradeTag(long tagId) {
        GradeTag tagEntity = gradeTagRepository.findById(tagId)
                .orElseThrow(GradeTagNotFoundException::new);
        if (tagEntity.isPrimitive()) {
            throw new GradeTagIsPrimitiveException();
        }
        gradeTagStudentRepository.deleteAllByStudentId(tagId);
        gradeTagRepository.delete(tagEntity);
    }

    @Transactional(readOnly = true)
    public byte[] exportGrades(ExportGradeDto dto) throws JRException {
        var tags = gradeTagRepository.findAllById(dto.getGradeTagsId());
        var tagsTitle = tags.stream()
                .map(GradeTag::getTitle)
                .collect(toList());
        var gradeTagStudents = gradeTagStudentRepository.findAllByTag_IdIn(dto.getGradeTagsId());
        List<List<String>> grades = new ArrayList<>(gradeTagStudents.stream()
                .collect(groupingBy(t -> t.getStudent().getId(), collectingAndThen(toList(), list -> {
                    var sorted = list.stream()
                            .sorted((a, b) -> (int) (a.getTag().getId() - b.getTag().getId()))
                            .map(x -> x.getGrade() != null ? x.getGrade().toString() : StringUtils.EMPTY)
                            .collect(toList());
                    var transformed = new ArrayList<String>();
                    StudentEntity student = list.get(0).getStudent();
                    transformed.add("1");
                    transformed.add(String.format("%s %s", student.getLastName(), student.getFirstName()));
                    transformed.addAll(sorted);
                    return transformed;
                })))
                .values());


        InputStream reportStream = getClass().getResourceAsStream("/templates/reports/grades" + ".jrxml");
        JasperDesign jasperReportDesign = JRXmlLoader.load(reportStream);
        List<String> transformedTitle = new ArrayList<>();
        transformedTitle.add("STT");
        transformedTitle.add("Họ và tên");
        transformedTitle.addAll(tagsTitle);

        DynamicReportBuilder reportBuilder = new DynamicReportBuilder(jasperReportDesign, transformedTitle.size());
        reportBuilder.addDynamicColumns();

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportDesign);

        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Sample Dynamic Columns Report");
        params.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
        params.put(PROPERTY_PAGE_HEIGHT, HEADER_BAND_HEIGHT + grades.size() * DETAIL_BAND_HEIGHT);
        DynamicColumnDataSource pdfDataSource = new DynamicColumnDataSource(transformedTitle, grades);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, pdfDataSource);
        jasperPrint.setPageHeight(HEADER_BAND_HEIGHT + grades.size() * DETAIL_BAND_HEIGHT);

        byte[] bytes = null;
        try (var byteArray = new ByteArrayOutputStream()) {
            var output = new SimpleOutputStreamExporterOutput(byteArray);
            var exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(output);
            exporter.exportReport();
            bytes = byteArray.toByteArray();
            output.close();
        } catch (IOException e) {
            log.error("Exception when preparing export for tags {}, message: {}", dto.getGradeTagsId() , e);
        }
        return bytes;
    }
}
