package br.com.kanban.kanbanback.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record CardRequestDTO(
                             @NotBlank @Size(max = 100) String titulo,
                             @NotBlank String descricao) {}
