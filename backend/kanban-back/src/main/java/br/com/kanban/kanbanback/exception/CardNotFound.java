package br.com.kanban.kanbanback.exception;

public class CardNotFound extends RuntimeException {

    public CardNotFound(Long id) {
        super("Board com ID " + id + " não encontrado");
    }

    public CardNotFound(String chave, String valor) {
        super("Board com " + chave + " " + valor + " não encontrado");
    }

    public CardNotFound(String mensagem) {
        super(mensagem);
    }
}
