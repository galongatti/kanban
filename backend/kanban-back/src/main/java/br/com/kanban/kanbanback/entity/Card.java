package br.com.kanban.kanbanback.entity;


import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false, length = 100)
    private String titulo;

    @Column(unique = false, nullable = false, columnDefinition = "TEXT")
    private String descricao;

    // Many-To-one vai na classe que tem a chave estrangeira, ou seja, a classe que vai ter a coluna com o id da outra classe.
    @ManyToOne(fetch = FetchType.LAZY)
    // JoinColumn é usado para especificar a coluna que vai ser usada como chave estrangeira.
    // O nome da coluna vai ser column_board_id, não pode ser nula e o nome da constraint vai ser fk_card_column_board.
    @JoinColumn(name = "column_board_id", nullable = false, foreignKey = @ForeignKey(name = "fk_card_column_board"))
    private ColumnBoard columnBoard;

    @Column(unique = false, nullable = false)
    private Date createdAt;
    @Column(unique = false, nullable = false)
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ColumnBoard getColumnBoard() {
        return columnBoard;
    }

    public void setColumnBoard(ColumnBoard columnBoard) {
        this.columnBoard = columnBoard;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
