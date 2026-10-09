package br.com.kanban.kanbanback.records;

import java.util.Date;

public record ColumnBoardResponseDTO(Long id, String nome, boolean active, Date createdAt, Date updatedAt, Integer posicao) {}
