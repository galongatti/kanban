package br.com.kanban.kanbanback.exception;

public class BoardColumNotFound extends RuntimeException {

    public BoardColumNotFound(Long id) {
        super("Board com ID " + id + " não encontrado");
    }

    public BoardColumNotFound(String chave, String valor) {
        super("Board com " + chave + " " + valor + " não encontrado");
    }

    public BoardColumNotFound(String mensagem) {
        super(mensagem);
    }
}
