package br.com.kanban.kanbanback.records;

import java.util.Date;
import java.util.List;

public record BoardResponseDTO(Long id, String name, String description, boolean active,
                               Date createdAt, Date updatedAt, List<ColumnBoardResponseDTO> columns) {}
