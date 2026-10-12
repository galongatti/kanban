package br.com.kanban.kanbanback.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
public class ColumnBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(name = "fk_column_board_board"))
    private Board board;

    @Column(nullable = false)
    private boolean active;

    @Column()
    private Date createdAt;

    @Column()
    private Date updatedAt;

    @Column(nullable = false)
    private Integer posicao;

    @OneToMany(mappedBy = "columnBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
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
    public Integer getPosicao() {
        return posicao;
    }
    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }
    public List<Card> getCards() {
        return Optional.ofNullable(cards).orElse(List.of());
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
