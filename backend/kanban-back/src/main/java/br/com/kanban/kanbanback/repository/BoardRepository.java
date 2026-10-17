package br.com.kanban.kanbanback.repository;

import br.com.kanban.kanbanback.entity.Board;
import br.com.kanban.kanbanback.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends BaseRepository<Board, Long> {
    List<Board> findAll(Sort sort);
    List<Board> findByActiveTrue(Sort sort);


    @EntityGraph(attributePaths = {"columns", "columns.cards"})
    @Query("""
            SELECT b
            FROM Board b
            WHERE b.id = :id
           """)
    Optional<Board> findByIdWithColumnsAndCards(@Param("id") Long id);
}
