package br.com.kanban.kanbanback.repository;

import br.com.kanban.kanbanback.entity.Board;
import br.com.kanban.kanbanback.entity.User;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends BaseRepository<Board, Long> {

    List<Board> findAll(Sort sort);

}
