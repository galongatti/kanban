package br.com.kanban.kanbanback.controller;

import br.com.kanban.kanbanback.records.BoardRequestDTO;
import br.com.kanban.kanbanback.records.BoardResponseDTO;
import br.com.kanban.kanbanback.records.CardRequestDTO;
import br.com.kanban.kanbanback.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<BoardResponseDTO> cadastrarBoard(@Valid @RequestBody BoardRequestDTO boardRequestDTO) {
        BoardResponseDTO board = boardService.cadastrarBoard(boardRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(board);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDTO> atualizarBoard(@PathVariable Long id, @Valid @RequestBody BoardRequestDTO boardRequestDTO) {
        BoardResponseDTO updatedBoard = boardService.atualizarBoard(id, boardRequestDTO);
        return ResponseEntity.ok(updatedBoard);
    }


    @GetMapping
    public ResponseEntity<List<BoardResponseDTO>> buscarBoards() {
        List<BoardResponseDTO> boards = boardService.buscarBoards();
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    @PostMapping("/{boardId}/columns/{columnId}/cards")
    public ResponseEntity<Void> adicionarCard(@PathVariable Long boardId, @PathVariable Long columnId, @Valid @RequestBody CardRequestDTO cardRequestDTO) {
        boardService.adicionarCard(boardId, columnId, cardRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
