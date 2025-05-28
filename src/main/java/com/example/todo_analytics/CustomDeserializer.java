package com.example.todo_analytics;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomDeserializer implements Deserializer<ToDoItem> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public ToDoItem deserialize(String topic, byte[] todoItem) {
        try {
            if (todoItem == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(todoItem, "UTF-8"), ToDoItem.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to ToDoItem", e);
        }
    }

    @Override
    public void close() {
    }
}
