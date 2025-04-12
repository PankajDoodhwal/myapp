package com.example.myapp.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogEntry {
    private final String className;
    private final String message;

    public String format() {
        return "  (" + className + ") - " + message;
    }
}
