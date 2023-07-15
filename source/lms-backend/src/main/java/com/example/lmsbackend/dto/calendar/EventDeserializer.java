package com.example.lmsbackend.dto.calendar;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class EventDeserializer extends JsonDeserializer<Event> {
    @Override
    public Event deserialize(JsonParser jsonParser,
                             DeserializationContext deserializationContext) throws IOException,
            JsonProcessingException {
        Event event = new Event();
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        // * Start Datetime
        ObjectNode startJsonString
                = (ObjectNode) treeNode.get("start");
        String dtString1  = startJsonString.get("dateTime").asText();
        String tz1  = startJsonString.get("timeZone").asText();
        event.setStart(new EventDateTime().setDateTime(new DateTime(dtString1)).setTimeZone(tz1));

        // * End Datetime
        ObjectNode endJsonString
                = (ObjectNode) treeNode.get("end");
        String dtString2 = startJsonString.get("dateTime").asText();
        String tz2 = startJsonString.get("timeZone").asText();
        event.setEnd(new EventDateTime().setDateTime(new DateTime(dtString2)).setTimeZone(tz2));

        // * Metadata
        TextNode locationNode = (TextNode)treeNode.get("location");
        TextNode descriptionNode =(TextNode) treeNode.get("description");
        TextNode summaryNode =(TextNode) treeNode.get("summary");

        String location = locationNode.asText();
        String description = descriptionNode.asText();
        String summary = summaryNode.asText();

        event.setLocation(location).setDescription(description).setSummary(summary);
        return event;
    }
}
