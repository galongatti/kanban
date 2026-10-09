package br.com.kanban.kanbanback.records;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record ColumnBoardRequestDTO(Optional<Long> id, @NotNull String nome, Long boardId, boolean active, Integer posicao) {
}
