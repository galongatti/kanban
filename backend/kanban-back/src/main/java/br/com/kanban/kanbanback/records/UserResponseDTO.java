package br.com.kanban.kanbanback.records;

import java.util.Date;

public record UserResponseDTO(Long id, String name, String email, Date createdAt) {}
