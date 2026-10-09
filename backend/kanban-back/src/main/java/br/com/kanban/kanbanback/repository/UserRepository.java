package br.com.kanban.kanbanback.repository;

import br.com.kanban.kanbanback.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
