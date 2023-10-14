package com.silvioromano.todo.task;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class TaskPriorityDeserializer extends JsonDeserializer<TaskPriority> {
    @Override
    public TaskPriority deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        try {
            return TaskPriority.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid TaskPriority. Accepted values are: [HIGH, MEDIUM, LOW]");
        }
    }
}