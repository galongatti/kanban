package br.com.kanban.kanbanback.records;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public record BoardResponseDTO(Long id, String name, String description, boolean active,
                               Date createdAt, Date updatedAt, Optional<List<ColumnBoardResponseDTO>> columns) {}
