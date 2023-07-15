package com.example.lmsbackend.service;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.event.*;
import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.dto.calendar.EventDto;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.exceptions.aclass.ClassNotFoundException;
import com.example.lmsbackend.mapper.EventMapper;
import com.example.lmsbackend.repository.CalendarRepository;
import com.example.lmsbackend.repository.EventRepository;
import com.example.lmsbackend.repository.UserRepository;
import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.lmsbackend.utils.DateTimeUtils.getGoogleEventDateTime;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private CalendarRepository calendarRepository;
    private EventRepository eventRepository;
    private EventMapper eventMapper;
    private UserRepository userRepository;
    private ClassService classService;

    @Autowired
    public EventService(CalendarRepository calendarRepository,
                        EventRepository eventRepository,
                        EventMapper eventMapper,
                        UserRepository userRepository,
                        @Lazy ClassService classService) {
        this.calendarRepository = calendarRepository;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.classService = classService;
    }

    public CalendarEntity createCalendar(CalendarType type, Long typeId) {
        var calendarEntity = new CalendarEntity();
        calendarEntity.setType(type);
        calendarEntity.setTypeId(typeId);
        return calendarRepository.save(calendarEntity);
    }

    // TODO: only get events between two point of time
    public List<EventDto> getEventsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        // TODO: check existing of calendar
        var userCalendarId = getCalendarId(CalendarType.USER, userId).get();
        var events = eventRepository.findEventsByCalendar(userCalendarId);
        return events.stream()
                .filter(event -> !event.getHidden())
                .map(this::mapEventDto)
                .flatMap(List::stream)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<EventDto> getEventsByClass(Long classId) {
        var calendar = calendarRepository.findByTypeAndAndTypeId(CalendarType.CLASS, classId)
                .orElseThrow(() -> new ClassNotFoundException(classId));
        var events = eventRepository.findEventsByCalendar(calendar.getId());
        return events.stream()
                .filter(event -> !event.getHidden())
                .map(this::mapEventDto)
                .flatMap(List::stream)
                .collect(toList());
    }

    public List<EventDto> getEventsByUserAndClass(Long userId, List<Long> classIdList) {
        if (CollectionUtils.isEmpty(classIdList)) {
            classIdList = classService.getClassOfUser();
        }
        return Stream.of(getEventsByUser(userId), classIdList.stream()
                        .map(this::getEventsByClass)
                        .flatMap(List::stream)
                        .collect(toList()))
                .flatMap(List::stream)
                .collect(toList());
    }

    private List<EventDto> mapEventDto(EventEntity event) {
        var dto = new EventDto();
        var googleEventDto = new Event();
        dto.setEvent(googleEventDto);
        googleEventDto.setSummary(event.getSummary());
        googleEventDto.setDescription(event.getDescription());
        googleEventDto.setStart(getGoogleEventDateTime(event.getStartedAt()));
        if (event.getEndedAt() != null) {
            googleEventDto.setEnd(getGoogleEventDateTime(event.getEndedAt()));
        }
        var eventType = event.getId().getEventType();
        switch (eventType) {
            case CLASS_SESSION:
                dto.setEventType(EventType.CLASS_SESSION.toString());
                return List.of(dto);
            case QUIZ:
                dto.setEventType(EventType.QUIZ_OPEN.toString());
                if (event.getEndedAt() != null) {
                    var quizEnded = new EventDto();
                    quizEnded.setEventType(EventType.QUIZ_DEADLINE.toString());
                    quizEnded.setEvent(googleEventDto);
                    return List.of(dto, quizEnded);
                }
                return List.of(dto);
            default:
                throw new IllegalArgumentException("The event type " + eventType + "is not supported");
        }
    }

    @Transactional(readOnly = true)
    public Optional<Long> getCalendarId(CalendarType type, Long typeId) {
        var calendarOpt = calendarRepository.findByTypeAndAndTypeId(type, typeId);
        return calendarOpt.map(CalendarEntity::getId);
    }

    @Transactional(readOnly = true)
    public CalendarEntity getCalendar(CalendarType type, Long typeId) {
        return calendarRepository.findByTypeAndAndTypeId(type, typeId).get();
    }

    public void createOrUpdateQuizEvent(Long classId, QuizClassEntity quiz) {
        var eventOpt = eventRepository.findById(new EventKey(EventType.QUIZ, quiz.getId()));
        var event = new EventEntity();
        if (eventOpt.isEmpty()) {
            eventMapper.mapQuizEvent(quiz, event);
            addEventToCalendar(classId, event);
        } else {
            event = eventOpt.get();
            eventMapper.mapQuizEvent(quiz, event);
        }
        eventRepository.save(event);
    }

    private void addEventToCalendar(Long classId, EventEntity eventEntity) {
        var calendar = calendarRepository.findFetchEventsByTypeAndAndTypeId(CalendarType.CLASS, classId).get();
        eventEntity.setCalendar(calendar);
        log.debug("Add {} to calendar {}", eventEntity.getId(), calendar.getId());
    }

    public EventEntity createClassSessionEvent(ClassSessionEntity session) {
        var event = new EventEntity();
        eventMapper.mapClassSessionEvent(session, event);
        event.getId().setEventId(session.getId());
        event.getId().setEventType(EventType.CLASS_SESSION);
        event.setHidden(false);
        event.setStartedAt(session.getStartedAt());
        event.setEndedAt(session.getFinishedAt());
        addEventToCalendar(session.getClassEntity().getId(), event);
        eventRepository.save(event);
        return event;
    }

    public void updateClassSessionEvent(ClassSessionEntity session) {
        var event = eventRepository.findById(new EventKey(EventType.CLASS_SESSION, session.getId()))
                .orElse(new EventEntity());
        eventMapper.mapClassSessionEvent(session, event);
    }

    public void updateQuizEvent(QuizClassEntity quiz) {
        var event = eventRepository.findById(new EventKey(EventType.QUIZ, quiz.getId()))
                .orElse(new EventEntity());
        eventMapper.mapQuizEvent(quiz, event);
    }
}
