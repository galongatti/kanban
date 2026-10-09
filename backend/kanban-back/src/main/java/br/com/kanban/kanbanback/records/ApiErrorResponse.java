package br.com.kanban.kanbanback.records;

public record ApiErrorResponse(int status, String code, String message, String path) {}
