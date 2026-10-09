package br.com.kanban.kanbanback.exception;

public class BoardNotFound extends RuntimeException {

    public BoardNotFound(Long id) {
        super("Board com ID " + id + " não encontrado");
    }

    public BoardNotFound(String chave, String valor) {
        super("Board com " + chave + " " + valor + " não encontrado");
    }

    public BoardNotFound(String mensagem) {
        super(mensagem);
    }
}
