package br.com.kanban.kanbanback.records;

import java.util.Date;

public record CardResponseDTO(Long id, String titulo, String descricao, Long columnBoardId, Date createdAt, Date updatedAt) {}
