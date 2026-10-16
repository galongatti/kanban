package br.com.kanban.kanbanback.repository;

import br.com.kanban.kanbanback.entity.Board;
import br.com.kanban.kanbanback.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends BaseRepository<Board, Long> {
    List<Board> findAll(Sort sort);
    List<Board> findByActiveTrue(Sort sort);


    @Query(""" 
            SELECT DISTINCT b
            FROM Board b
            LEFT JOIN FETCH b.columns c
            LEFT JOIN FETCH c.cards
            WHERE b.id = :id
           """)
    Optional<Board> findByIdWithColumnsAndCards(@Param("id") Long id);
}
