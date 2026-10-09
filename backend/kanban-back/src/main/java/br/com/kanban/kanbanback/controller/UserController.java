package br.com.kanban.kanbanback.controller;

import br.com.kanban.kanbanback.exception.UserNotFoundException;
import br.com.kanban.kanbanback.records.UserInputDTO;
import br.com.kanban.kanbanback.records.UserResponseDTO;
import br.com.kanban.kanbanback.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> buscarUsuarios() {
        return ResponseEntity.ok(userService.buscarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
        UserResponseDTO user = userService.buscarPorId(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> cadastrarUsuario(@RequestBody UserInputDTO userInputDTO) {
        UserResponseDTO userResponseDTO = userService.cadastrarUsuario(userInputDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping(params = "email")
    public ResponseEntity<UserResponseDTO> buscarPorEmail(@RequestParam String email) {
        UserResponseDTO user = userService.buscarPorEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email", email));
        return ResponseEntity.ok(user);
    }

}
