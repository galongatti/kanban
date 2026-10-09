package br.com.kanban.kanbanback.records;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public record BoardRequestDTO(Optional<Long> id, @NotNull String name, String description, @NotNull boolean active, List<ColumnBoardRequestDTO> columns) {}
