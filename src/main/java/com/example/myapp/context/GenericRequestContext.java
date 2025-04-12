package com.example.myapp.context;

import com.example.myapp.model.LogEntry;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
public class GenericRequestContext {
    private final Map<String, Object> context = new LinkedHashMap<>();
    private final Set<String> classTrace = new LinkedHashSet<>();
    private final List<LogEntry> logs = new ArrayList<>();
    private final List<String> sqlLogs = new ArrayList<>();
    private final String traceId = UUID.randomUUID().toString();

    private Object requestBody;
    private Object responseBody;
    private String apiName;

    public void put(String key, Object value) {
        context.put(key, value);
    }

    public Object get(String key) {
        return context.get(key);
    }

    public void markClass(String className) {
        classTrace.add(className);
    }

    public void addLog(String className, String message) {
        logs.add(new LogEntry(className, message));
    }

    public void addSqlLog(String sql) {
        sqlLogs.add(sql);
    }

    public String getFormattedLogs() {
        StringBuilder sb = new StringBuilder();
        sb.append("<logger>\n");
        for (LogEntry log : logs) {
            sb.append("    ").append(log.format()).append("\n");
        }
        sb.append("</logger>\n");
        return sb.toString();
    }

    public String getFormattedContext() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t<context>\n");
        context.forEach((k, v) -> sb.append("\t\t").append(k).append(": ").append(v).append("\n"));
        sb.append("\t</context>\n");
        return sb.toString();
    }

    public String getFormattedTrace() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t<flow>\n");
        if (!classTrace.isEmpty()) {
            sb.append("\t\t").append(String.join(" → ", classTrace)).append("\n");
        }
        sb.append("\t</flow>\n");
        return sb.toString();
    }

    public String getFormattedSqlLogs() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t<sql>\n");
        for (String sql : sqlLogs) {
            sb.append("\t\t").append(sql).append("\n");
        }
        sb.append("\t</sql>\n");
        return sb.toString();
    }

    public String getFormattedRequestResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t<request>\n\t\t").append(requestBody).append("\n\t</request>\n");
        sb.append("\t<response>\n\t\t").append(responseBody).append("\n\t</response>\n");
        return sb.toString();
    }

    public void clear() {
        context.clear();
        classTrace.clear();
        logs.clear();
    }
}


