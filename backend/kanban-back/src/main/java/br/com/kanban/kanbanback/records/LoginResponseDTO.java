package br.com.kanban.kanbanback.records;

public record LoginResponseDTO(String token, String tokenType) {

    public LoginResponseDTO(String token) {
        this(token, "Bearer");
    }
}
