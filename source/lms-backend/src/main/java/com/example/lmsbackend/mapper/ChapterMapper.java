package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.classmodel.ChapterClassEntity;
import com.example.lmsbackend.domain.coursemodel.ChapterCourseEntity;
import com.example.lmsbackend.dto.response.course.ChapterDto;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        componentModel = "spring",
        uses = {
                QuizMapper.class,
                UnitMapper.class
        }
)
public interface ChapterMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    @Mapping(nullValuePropertyMappingStrategy = IGNORE, target = "id", qualifiedByName = "mapChapterId")
    ChapterCourseEntity mapToChapterEntity(ChapterDto dto);

    @Mapping(target = "id", ignore = true)
    ChapterClassEntity mapToChapterClassEntity(ChapterDto dto);

    @Mapping(target = "quizzes", ignore = true)
    @Mapping(target = "units", ignore = true)
    ChapterDto mapToChapterDto(ChapterCourseEntity chapterCourseEntity);

    ChapterDto mapToChapterDto(ChapterClassEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actions", source = "actions", ignore = true)
    ChapterClassEntity mapToChapterClass(ChapterCourseEntity entity);

    @Named(value = "mapChapterId")
    default Long mapChapterId(Long id) {
        if (id == null || id <= 0) return null;
        return id;
    }
}