package br.com.kanban.kanbanback.service;

import br.com.kanban.kanbanback.entity.Board;
import br.com.kanban.kanbanback.entity.Card;
import br.com.kanban.kanbanback.entity.ColumnBoard;
import br.com.kanban.kanbanback.exception.BoardColumNotFound;
import br.com.kanban.kanbanback.exception.BoardNotFound;
import br.com.kanban.kanbanback.exception.CardNotFound;
import br.com.kanban.kanbanback.records.*;
import br.com.kanban.kanbanback.repository.BoardRepository;
import br.com.kanban.kanbanback.repository.CardRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final CardRepository cardRepository;

    public BoardService(BoardRepository boardRepository, CardRepository cardRepository) {
        this.boardRepository = boardRepository;
        this.cardRepository = cardRepository;
    }


    public BoardResponseDTO cadastrarBoard(BoardRequestDTO boardRequestDTO) {

        Board board = new Board();
        board.setName(boardRequestDTO.name());
        board.setDescription(boardRequestDTO.description());
        board.setActive(boardRequestDTO.active());
        board.setColumns(boardRequestDTO.columns().stream().map(columnRequestDTO -> {
            ColumnBoard columnBoard = new ColumnBoard();
            columnBoard.setNome(columnRequestDTO.nome());
            columnBoard.setBoard(board);
            columnBoard.setActive(columnRequestDTO.active());
            columnBoard.setCreatedAt(new Date());
            columnBoard.setUpdatedAt(new Date());
            columnBoard.setPosicao(columnRequestDTO.posicao());
            return columnBoard;
        }).toList());
        board.setCreatedAt(new Date());
        board.setUpdatedAt(new Date());

        Board savedBoard = boardRepository.save(board);
        return toResponse(savedBoard);
    }

    public List<BoardResponseDTO> buscarBoards() {

        Sort sort = Sort.by(Sort.Direction.ASC, "columns.posicao");

        List<Board> boards = boardRepository.findAll(sort);
        return boards.stream().map(this::toResponse).toList();
    }

    private BoardResponseDTO toResponse(Board board) {



        return new BoardResponseDTO(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.isActive(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                board.getColumns().stream()
                        .map(this::toColumnResponse)
                        .toList());
    }

    private ColumnBoardResponseDTO toColumnResponse(ColumnBoard columnBoard) {

        if(columnBoard == null)
            return null;

        List<CardResponseDTO> cardResponse = columnBoard.getCards().isPresent() ? columnBoard.getCards().get().stream().map(this::toCardResponse).collect(Collectors.toList()) : null;

        return new ColumnBoardResponseDTO(
                columnBoard.getId(),
                columnBoard.getNome(),
                columnBoard.isActive(),
                columnBoard.getCreatedAt(),
                columnBoard.getUpdatedAt(),
                columnBoard.getPosicao(),
                cardResponse);
    }

    private CardResponseDTO toCardResponse(Card card) {
        return new CardResponseDTO(
                card.getId(),
                card.getTitulo(),
                card.getDescricao(),
                card.getColumnBoard().getId(),
                card.getCreatedAt(),
                card.getUpdatedAt());
    }

    public BoardResponseDTO atualizarBoard(Long id, @Valid BoardRequestDTO boardRequestDTO) {
        return null;
    }

    public void adicionarCard(Long boardId, Long columnId, @Valid CardRequestDTO cardRequestDTO) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFound("Board não encontrado"));

        ColumnBoard columnBoard = board.getColumns().stream().filter(column -> column.getId().equals(columnId)).findFirst().orElseThrow(() -> new BoardColumNotFound("Coluna do board não encontrada"));

        Card card = new Card();
        card.setColumnBoard(columnBoard);
        card.setDescricao(cardRequestDTO.descricao());
        card.setTitulo(cardRequestDTO.titulo());
        card.setCreatedAt(new Date());
        card.setUpdatedAt(new Date());

        cardRepository.save(card);
    }

    public void atualizarCard(Long boardId, Long columnId, Long cardId, @Valid CardRequestDTO cardRequest) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFound("Board não encontrado"));

        ColumnBoard columnBoard = board.getColumns().stream().filter(column -> column.getId().equals(columnId)).findFirst().orElseThrow(() -> new BoardColumNotFound("Coluna do board não encontrada"));

        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFound("Card não encontrado"));
        card.setTitulo(cardRequest.titulo());
        card.setDescricao(cardRequest.descricao());
        card.setUpdatedAt(new Date());
        card.setColumnBoard(columnBoard);

        cardRepository.save(card);
    }
}
