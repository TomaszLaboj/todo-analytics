package com.example.todo_analytics;

public class ToDoItemUpdated {

    public ToDoItem original;
    public ToDoItem updated;

    public ToDoItemUpdated(ToDoItem original, ToDoItem updated) {
        this.original = original;
        this.updated = updated;
    }
}