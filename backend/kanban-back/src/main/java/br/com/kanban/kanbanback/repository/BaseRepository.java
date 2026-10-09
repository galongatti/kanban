package br.com.kanban.kanbanback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
interface BaseRepository<T, ID> extends ListCrudRepository<T, ID> {

    Page<T> findAll(Pageable pageable);
}