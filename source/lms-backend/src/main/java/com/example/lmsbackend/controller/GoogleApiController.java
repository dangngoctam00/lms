package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.calendar.EventDto;
import com.example.lmsbackend.exceptions.googe.calendar.BadEventShapeException;
import com.example.lmsbackend.service.EventService;
import com.example.lmsbackend.service.GoogleCalendarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;
import static com.example.lmsbackend.constant.AppConstant.PARAM_DELIMITER;
import static java.util.stream.Collectors.toList;

/*
 * https://stackoverflow.com/questions/57972607/what-is-the-alternative-to-the-deprecated-googlecredential
 */

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class GoogleApiController {

    private final GoogleCalendarService service;

    private final EventService eventService;

    private final ObjectMapper objectMapper;


    // ? List all calendar: For admin account only! (Not yet implemented authorization mechanism)
    @GetMapping("/calendars")
    public ResponseEntity<List<CalendarListEntry>> listAllCalendarIds() throws IOException {
        return ResponseEntity.ok(service.listAllCalendar());
    }

    @GetMapping("/calendars/{id}")
    public ResponseEntity<Calendar> getCalendarDetail(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.getCalendar(id));
    }

    @PostMapping("/calendars")
    public ResponseEntity<Calendar> createCalendar(@RequestBody Calendar body) throws IOException {
        log.info("Calendar passed in is: " + body.toPrettyString());
        return ResponseEntity.ok(service.createNewCalendar(body));
    }

    @PutMapping("/calendars/{id}")
    public ResponseEntity<Calendar> updateCalendarMeta(@RequestBody Calendar body) throws IOException {
        return ResponseEntity.ok(service.updateCalendarMeta(body));
    }

    @DeleteMapping("/calendars/{id}")
    public ResponseEntity.BodyBuilder deleteCalendar(@PathVariable("id") String id) {
        try {
            service.deleteCalendar(id);
            return ResponseEntity.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError();
        }
    }

    @DeleteMapping("/calendars/{id}/events")
    public ResponseEntity<String> deleteEventsByCalendar(@PathVariable("id") String id) {
        try {
            service.deleteEventFromCalendar(id);
            return ResponseEntity.ok("Oke");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Not oke");
        }
    }

    @PostMapping("/calendars/{id}/clear")
    public ResponseEntity.BodyBuilder clearCalendar(@PathVariable("id") String calendarID) {
        try {
            service.clearCalendar(calendarID);
            return ResponseEntity.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError();
        }
    }

    // * Events related routes
    @GetMapping("/calendars/{calendar_id}/events")
    public ResponseEntity<List<Event>> listEventsFromCalendar(@PathVariable("calendar_id") String calendarID) throws IOException {
        return ResponseEntity.ok(service.getAllCalendarEvents(calendarID));
    }

    @ApiResponses(
            value = @ApiResponse(code = 400, message = "Exception code: CLASS_NOT_FOUND")
    )
    @GetMapping("/class/{classId}/calendars")
    public ResponseEntity<List<EventDto>> getEventsByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(eventService.getEventsByClass(classId));
    }

    @GetMapping("/calendars/{calendar_id}/events/{event_id}")
    public ResponseEntity<Event> getEventFromCalendar(@PathVariable("calendar_id") String calendarID, @PathVariable("event_id") String eventID) {
        return ResponseEntity.ok(service.getEventFromCalendar(calendarID, eventID));
    }

    @PostMapping("/calendars/{calendar_id}/events")
    public ResponseEntity<Event> createNewEvent(@PathVariable("calendar_id") String calendarID, @RequestBody String event) throws BadEventShapeException, IOException {
        Event eventTest = objectMapper.readValue(event, Event.class);
        log.debug("Converted event is: " + eventTest);
        return ResponseEntity.ok(service.addEventToCalendar(calendarID, eventTest));
    }

    // ? This method maybe used to inherit events from one calendar to others
//    @PostMapping("/calendars/{calendar_id}/events/")
//    public ResponseEntity<Event> importEventToCalendar(@PathVariable("calendar_id") String calendarID, @RequestBody Event event) throws BadEventShapeException, IOException {
//        return ResponseEntity.ok(service.addEventToCalendsar(calendarID, event));
//    }

    @PutMapping("/calendars/{calendar_id}/events/{event_id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("calendar_id") String calendarID, @PathVariable("event_id") String eventID, @RequestBody String eventJsonString) throws BadEventShapeException, IOException {
        Event event = objectMapper.readValue(eventJsonString, Event.class);
        return ResponseEntity.ok(service.updateEventFromCalendar(calendarID, eventID, event));
    }


    @DeleteMapping("/calendars/{calendar_id}/events/{event_id}")
    public ResponseEntity<String> removeEventFromCalendar(@PathVariable("calendar_id") String calendarID, @PathVariable("event_id") String eventID) {
        service.deleteEventFromCalendar(calendarID, eventID);
        return ResponseEntity.ok("Ok");
    }
}


//
//    // * Google Calendar Controller
//    @GetMapping("/get_events")
//    public ResponseEntity<ApiCredentialDto> getEvents() throws IOException {
//        DateTime now = new DateTime(System.currentTimeMillis());
//        // * Get the events from the calendar
//        List<Event> events = service.getAllCalendarEvents("primary");
//        if (events != null) {
//            if (events.isEmpty()) {
//                System.out.println("No such events");
//            } else {
//                System.out.println("Upcoming events");
//                for (Event event : events) {
//                    DateTime start = event.getStart().getDateTime();
//                    if (start == null) {
//                        start = event.getStart().getDate();
//                    }
//                    System.out.printf("%s (%s)\n", event.getSummary(), start);
//                }
//            }
//        }
//        ApiCredentialDto response = new ApiCredentialDto()      ;
//        response.setApi_key("OK got events");
//        return ResponseEntity.ok(response);
//    }