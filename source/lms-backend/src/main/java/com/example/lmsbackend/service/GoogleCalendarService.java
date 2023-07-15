package com.example.lmsbackend.service;

import com.example.lmsbackend.exceptions.ThirdPartyException;
import com.example.lmsbackend.exceptions.googe.calendar.BadEventShapeException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.Strings;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalendarService {

    private final Calendar upStreamGoogleCalendarService;

    // * Google Calendar Service
    // ** Calendar Stuffs
    public List<CalendarListEntry> listAllCalendar() throws IOException {
        String pageToken = null;
        List<CalendarListEntry> list = new ArrayList<>();
        do {
            CalendarList calendarList = upStreamGoogleCalendarService.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                System.out.println(calendarListEntry.getSummary());
                list.add(calendarListEntry);
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
        return list;
    }

    public com.google.api.services.calendar.model.Calendar createNewCalendar(
            com.google.api.services.calendar.model.Calendar calendarDto) {
        try {
            // calendar.setTimeZone()
            List<String> allowedMeetingTypes = new ArrayList<>();
            allowedMeetingTypes.add("hangoutsMeet");
            ConferenceProperties confProps = new ConferenceProperties();
            confProps.setAllowedConferenceSolutionTypes(allowedMeetingTypes);
            calendarDto.setConferenceProperties(confProps);
            return upStreamGoogleCalendarService.calendars().insert(calendarDto).execute();
        } catch (Exception e) {
            log.warn("Error is thrown when creating new calendar {}", e.getMessage());
            throw new ThirdPartyException("Error when creating new calendar", e);
        }
    }

    public void deleteCalendar(String calendarID) throws IOException {
        upStreamGoogleCalendarService.calendars().delete(calendarID).execute();
    }

    // * Truncates all the data on an calendar
    public void clearCalendar(String calendarID) {
        String calendarToClear = Strings.isNullOrEmpty(calendarID) ? "primary" : calendarID;
        try {
            upStreamGoogleCalendarService.calendars().clear(calendarToClear).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public com.google.api.services.calendar.model.Calendar getCalendar(String calendarID) {
        String calendarToRetrieve = Strings.isNullOrEmpty(calendarID) ? "primary" : calendarID;
        // * The async update may not finished yet on the cloud
        try {

            return upStreamGoogleCalendarService.calendars().get(calendarID).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // https://developers.google.com/calendar/api/v3/reference/calendars/update
    public com.google.api.services.calendar.model.Calendar updateCalendarMeta(com.google.api.services.calendar.model.Calendar body) throws IOException {
        var transientUpdatedCalendar = upStreamGoogleCalendarService.calendars().get(body.getId()).execute();
        transientUpdatedCalendar.setTimeZone(body.getTimeZone());
        transientUpdatedCalendar.setSummary(body.getSummary());
        transientUpdatedCalendar.setDescription(body.getDescription());
        transientUpdatedCalendar.setEtag(body.getEtag());
        transientUpdatedCalendar.setConferenceProperties(body.getConferenceProperties());
        transientUpdatedCalendar.setKind(body.getKind());
        transientUpdatedCalendar.setLocation(body.getLocation());
        // * Push changes to api
        return upStreamGoogleCalendarService.calendars().update(body.getId(), transientUpdatedCalendar).execute();
    }

    // ** Calendar Stuffs -- Events related methods
    // **** Imports an event. This operation is used to add a private copy of an existing event to a calendar.
    public List<Event> getAllCalendarEvents(String calendarID) {
        // Iterate over the events in the specified calendar
        String pageToken = null;
        ArrayList<Event> result = new ArrayList<>();
        try {
            do {
                Events events;
                events = upStreamGoogleCalendarService.events().list(calendarID).setPageToken(pageToken).execute();

                List<Event> items = events.getItems();
                for (Event event : items) {
                    System.out.println(event.getSummary());
                    result.add(event);
                }
                pageToken = events.getNextPageToken();
            } while (pageToken != null);
        } catch (IOException e) {
            var message = String.format("Error when get list events: %s", e.getMessage());
            log.warn(message);
            throw new ThirdPartyException(message, e);
        }
        return result;
    }

    // * Key: Token use to retrive nextPage, Events: List of Events for this page
    public HashMap<String, List<Event>> getCalendarEventsPaginatedWithToken(String calendarID, String pageToken) throws IOException {
        Events events = upStreamGoogleCalendarService.events().list(calendarID).setPageToken(pageToken).execute();
        List<Event> items = events.getItems();
        for (Event event : items) {
            System.out.println(event.getSummary());
        }
        HashMap<String, List<Event>> map = new HashMap<>();
        map.put(pageToken, items);
        return map;
    }

    public void importEventToCalendar(String calendarID, Event event) {
        try {
            upStreamGoogleCalendarService.events().calendarImport(calendarID, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEventFromCalendar(String calendarId) {
        getAllCalendarEvents(calendarId)
                .forEach(event -> deleteEventFromCalendar(calendarId, event.getId()));
    }

    public void deleteEventFromCalendar(String calendarID, String eventID) {
        try {
            upStreamGoogleCalendarService.events().delete(calendarID, eventID).execute();
        } catch (Exception e) {
            log.warn("Delete event {} from calendar {} error: {}", eventID, calendarID, e.getMessage());
            var error = ((GoogleJsonResponseException) e).getDetails();
            if (error != null && ((!Objects.equals(error.get("code"), 410) || !Objects.equals("Resource has been deleted", error.get("message"))))) {
                throw new ThirdPartyException("Delete event from calendar error", e.getCause());
            }
        }
    }

    public Event getEventFromCalendar(String calendarID, String eventID) {
        Event event = null;
        try {
            event = upStreamGoogleCalendarService.events().get(calendarID, eventID).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    public Event updateEventFromCalendar(String calendarID, String eventIdToUpdate, Event updatedEvent) {
        // Get the Event first
        var event = getEventFromCalendar(calendarID, eventIdToUpdate);
        // Compare the diff between the old event and the desired events
        Event updated = null;
        try {
            updated = upStreamGoogleCalendarService.events().update(calendarID, event.getId(), updatedEvent).execute();
        } catch (IOException e) {
            var message = String.format("Cannot update event %s to calendar %s", event, calendarID);
            throw new ThirdPartyException(message, e);
        }
        return updated;
    }

    public Event updateDescriptionOfEvent(String calendarId, String eventIdToUpdate, String description) {
        var event = getEventFromCalendar(calendarId, eventIdToUpdate);
        try {
            event.setDescription(description);
            return upStreamGoogleCalendarService.events().update(calendarId, event.getId(), event).execute();
        } catch (IOException e) {
            var message = String.format("Cannot update event %s to calendar %s", event, calendarId);
            throw new ThirdPartyException(message, e);
        }
    }

    // ***** https://developers.google.com/calendar/api/v3/reference/events/insert
    public Event addEventToCalendar(String calendarID, Event event) {
        if (event == null) {
            throw new BadEventShapeException();
        }
        try {
            return upStreamGoogleCalendarService.events().insert(calendarID, event).execute();
        } catch (IOException e) {
            var message = String.format("Cannot add event %s to calendar %s", event, calendarID);
            throw new ThirdPartyException(message, e);
        }
    }

    // * Google Meet Functionality
    public void createMeetRoom(String calendarID) {

    }
}
