package br.com.kanban.kanbanback.exception;

public class ColumnContainsCard extends RuntimeException {


    public ColumnContainsCard(String chave, String valor) {
        super("Board com " + chave + " " + valor + " não encontrado");
    }

    public ColumnContainsCard(String mensagem) {
        super(mensagem);
    }
}
