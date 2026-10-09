package br.com.kanban.kanbanback.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Usuário com ID " + id + " não encontrado");
    }

    public UserNotFoundException(String chave, String valor) {
        super("Usuário com " + chave + " " + valor + " não encontrado");
    }
}
