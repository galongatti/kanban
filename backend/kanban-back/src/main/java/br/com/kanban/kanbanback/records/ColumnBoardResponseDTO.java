package br.com.kanban.kanbanback.records;

import java.util.Date;
import java.util.List;

public record ColumnBoardResponseDTO(Long id, String nome, boolean active, Date createdAt, Date updatedAt, Integer posicao, List<CardResponseDTO> cards) {}
