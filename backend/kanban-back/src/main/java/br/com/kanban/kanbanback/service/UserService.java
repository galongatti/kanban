package br.com.kanban.kanbanback.service;
import br.com.kanban.kanbanback.entity.User;
import br.com.kanban.kanbanback.exception.UserNotFoundException;
import br.com.kanban.kanbanback.records.UserInputDTO;
import br.com.kanban.kanbanback.records.UserResponseDTO;
import br.com.kanban.kanbanback.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> buscarUsuarios() {
        Iterable<User> users = userRepository.findAll();
        List<UserResponseDTO> userList = new ArrayList<>();
        users.forEach(user -> userList.add(toResponseDTO(user)));
        return userList;
    }

    public Optional<UserResponseDTO> buscarPorId(Long id) {
        return userRepository.findById(id).map(this::toResponseDTO);
    }

    public Optional<UserResponseDTO> buscarPorEmail(String email) {
        return userRepository.findByEmail(email).map(this::toResponseDTO);
    }

    public UserResponseDTO cadastrarUsuario(UserInputDTO userInputDTO) {

        String passwordHash = passwordEncoder.encode(userInputDTO.password());

        User user = new User(
                null,
                userInputDTO.name(),
                userInputDTO.email(),
                passwordHash,
                new Date()
        );

        return toResponseDTO(userRepository.save(user));
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

}
