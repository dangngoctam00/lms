package com.example.lmsbackend.service;

import com.example.lmsbackend.criteria.BaseSearchCriteria;
import com.example.lmsbackend.domain.CourseProgramEntity;
import com.example.lmsbackend.domain.CourseProgramKey;
import com.example.lmsbackend.dto.request.program.CourseInProgramDto;
import com.example.lmsbackend.dto.request.program.CreateProgramDto;
import com.example.lmsbackend.dto.request.program.UpdateProgramDto;
import com.example.lmsbackend.dto.response.program.ProgramDto;
import com.example.lmsbackend.dto.response.program.ProgramPagedDto;
import com.example.lmsbackend.exceptions.course.CourseNotFoundException;
import com.example.lmsbackend.exceptions.program.ProgramCodeAlreadyExistsException;
import com.example.lmsbackend.exceptions.program.ProgramNotFoundException;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.ProgramMapper;
import com.example.lmsbackend.repository.CourseProgramRepository;
import com.example.lmsbackend.repository.CourseRepository;
import com.example.lmsbackend.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;
    private final CourseRepository courseRepository;
    private final CourseProgramRepository courseProgramRepository;
    private final EntityManager entityManager;

    public ProgramPagedDto getPrograms() {
        var programs = programRepository.findAll();
        var programsDto = new ProgramPagedDto();
        programsDto.setListData(programs
                .stream()
                .map(programMapper::mapToProgramInListDto)
                .collect(toList()));
        return programsDto;
    }

    @Transactional
    public ProgramDto createProgram(CreateProgramDto dto) {
        if (programRepository.findByCode(dto.getCode()).isPresent()) {
            throw new ProgramCodeAlreadyExistsException(dto.getCode());
        }
        var programEntity = programMapper.mapToProgramEntity(dto);
        var coursesId = dto.getCourses()
                .stream()
                .map(CourseInProgramDto::getId)
                .collect(toList());
        var courses = courseRepository.findCourseEntityByIdIn(coursesId);

        dto.getCourses()
                .forEach(courseDto -> courses.stream()
                        .filter(course -> course.getId().equals(courseDto.getId()))
                        .findAny()
                        .orElseThrow(() -> new CourseNotFoundException(courseDto.getId())));

        var coursesPrograms = courses.stream()
                .map(course -> {
                    var courseProgram = new CourseProgramEntity();
                    programEntity.addCourse(courseProgram);
                    course.addProgram(courseProgram);
                    return courseProgram;
                }).collect(toList());

        setOrder(coursesPrograms, dto);
        programRepository.save(programEntity);
        log.info("Create program {} successfully", programEntity.getName());
        return programMapper.mapToProgramDto(programEntity);
    }

    public ProgramDto getProgram(Long id) {
        var program = programRepository.findById(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));
        return programMapper.mapToProgramDto(program);
    }

    @Transactional
    public ProgramDto updateProgram(UpdateProgramDto dto) {
        var program = programRepository.findById(dto.getId())
                .orElseThrow(() -> new ProgramNotFoundException(dto.getId()));

        var coursesId = dto.getCourses()
                .stream().map(CourseInProgramDto::getId)
                .collect(toList());

        var courses = courseRepository.findAllById(coursesId);

        programMapper.mapUpdatedProgram(program, dto);
        var courseProgramEntities =  dto.getCourses()
                .stream()
                .map(courseDto -> {
                    var courseProgram = new CourseProgramEntity();
                    courseProgram.setProgram(program);
                    var course = courses
                            .stream()
                            .filter(c -> c.getId().equals(courseDto.getId()))
                            .findAny()
                            .orElseThrow(() -> new CourseNotFoundException(courseDto.getId()));
                    courseProgram.setCourse(course);
                    courseProgram.setId(new CourseProgramKey(course.getId(), program.getId()));
                    return courseProgram;
                })
                .collect(toSet());

        setOrder(courseProgramEntities, dto);
        program.setCourses(courseProgramEntities);
        programRepository.save(entityManager.merge(program));
        log.info("Update program: {} successfully", dto.getId());
        return programMapper.mapToProgramDto(program);
    }

    private void setOrder(Collection<CourseProgramEntity> courseProgramEntities, CreateProgramDto dto) {
        courseProgramEntities
                .forEach(courseProgram -> {
                    var course = dto.getCourses()
                            .stream()
                            .filter(courseDto -> courseProgram.getCourse().getId().equals(courseDto.getId())).findFirst()
                            .orElseThrow(() -> new CourseNotFoundException(courseProgram.getCourse().getId()));
                    var index = dto.getCourses().indexOf(course);
                    courseProgram.setOrder(index + 1);
                });
    }

    public ProgramPagedDto getPrograms(BaseSearchCriteria criteria) {
        var programs = programRepository.getPrograms(criteria);
        var programsDto = new ProgramPagedDto();
        MapperUtils.mapPagedDto(programsDto, programs);
        programsDto.setListData(
                programs.getListData()
                        .stream()
                        .map(programMapper::mapToProgramInListDto)
                        .collect(toList())
        );
        return programsDto;
    }

    public Long deleteProgram(Long id) {
        var program = programRepository.findById(id)
                .orElseThrow(() -> new ProgramNotFoundException(id));
        programRepository.delete(program);
        return id;
    }
}
