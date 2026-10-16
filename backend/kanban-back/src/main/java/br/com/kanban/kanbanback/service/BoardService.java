package br.com.kanban.kanbanback.service;

import br.com.kanban.kanbanback.entity.Board;
import br.com.kanban.kanbanback.entity.Card;
import br.com.kanban.kanbanback.entity.ColumnBoard;
import br.com.kanban.kanbanback.exception.BoardColumNotFound;
import br.com.kanban.kanbanback.exception.ColumnContainsCard;
import br.com.kanban.kanbanback.exception.BoardNotFound;
import br.com.kanban.kanbanback.exception.CardNotFound;
import br.com.kanban.kanbanback.records.*;
import br.com.kanban.kanbanback.repository.BoardRepository;
import br.com.kanban.kanbanback.repository.CardRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        }).collect(Collectors.toCollection(LinkedHashSet::new)));
        board.setCreatedAt(new Date());
        board.setUpdatedAt(new Date());

        Board savedBoard = boardRepository.save(board);
        return toResponse(savedBoard, true);
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDTO> buscarBoards() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Board> boards = boardRepository.findByActiveTrue(sort);
        return boards.stream().map(board -> toResponse(board, false)).toList();
    }

    @Transactional(readOnly = true)
    public BoardResponseDTO buscarBoardPorId(Long id) {
        Board board = boardRepository.findByIdWithColumnsAndCards(id).orElseThrow(() -> new BoardNotFound("Board não encontrado"));
        return toResponse(board, true);
    }

    private BoardResponseDTO toResponse(Board board, boolean includeColumns) {

        Optional<List<ColumnBoardResponseDTO>> columns = Optional.empty();
        if (includeColumns) {
            columns = Optional.of(board.getColumns().stream().map(this::toColumnResponse).toList());
        }

        return new BoardResponseDTO(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.isActive(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                columns);
    }

    private ColumnBoardResponseDTO toColumnResponse(ColumnBoard columnBoard) {

        if(columnBoard == null)
            return null;

        List<CardResponseDTO> cardResponse = columnBoard.getCards().stream().map(this::toCardResponse).collect(Collectors.toList());

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

    @Transactional
    public BoardResponseDTO atualizarBoard(Long id, @Valid BoardRequestDTO boardRequestDTO) {

        Set<ColumnBoard> colunas = new LinkedHashSet<>();

        Board boardAtual = boardRepository.findById(id).orElseThrow(() -> new BoardNotFound("Board não encontrado"));
        Set<ColumnBoard> colunasAtuais = boardAtual.getColumns();
        List<Long> colunasAtuaisIds = colunasAtuais.stream().map(ColumnBoard::getId).toList();
        List<ColumnBoardRequestDTO> colunasRequest = boardRequestDTO.columns();

        colunasRequest.forEach(c -> {
            if(c.id().isPresent() && colunasAtuaisIds.contains(c.id().get()))
            {
                ColumnBoard colunaAtual = colunasAtuais.stream().filter(coluna -> coluna.getId().equals(c.id().get())).findFirst().orElseThrow(() ->
                        new BoardColumNotFound("Coluna do board não encontrada"));
                colunaAtual.setNome(c.nome());
                colunaAtual.setActive(c.active());
                colunaAtual.setPosicao(c.posicao());
                colunaAtual.setUpdatedAt(new Date());
                colunaAtual.setBoard(boardAtual);
                colunas.add(colunaAtual);

                if(!colunaAtual.getCards().isEmpty() && !c.active())
                {
                    throw new ColumnContainsCard("Não é possível desativar a coluna, pois ela contém cards");
                }
            }
            else
            {
                ColumnBoard novaColuna = new ColumnBoard();
                novaColuna.setNome(c.nome());
                novaColuna.setActive(c.active());
                novaColuna.setPosicao(c.posicao());
                novaColuna.setCreatedAt(new Date());
                novaColuna.setUpdatedAt(new Date());
                novaColuna.setBoard(boardAtual);
                colunas.add(novaColuna);
            }
        });

        boardAtual.setName(boardRequestDTO.name());
        boardAtual.setDescription(boardRequestDTO.description());
        boardAtual.setActive(boardRequestDTO.active());
        boardAtual.getColumns().clear();
        boardAtual.getColumns().addAll(colunas);
        boardAtual.setUpdatedAt(new Date());

        boardRepository.save(boardAtual);
        return toResponse(boardAtual, true);
    }

    @Transactional
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

    @Transactional
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
