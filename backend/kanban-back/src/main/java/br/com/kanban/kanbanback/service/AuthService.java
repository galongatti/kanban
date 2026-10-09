package br.com.kanban.kanbanback.service;

import br.com.kanban.kanbanback.entity.User;
import br.com.kanban.kanbanback.records.LoginRequestDTO;
import br.com.kanban.kanbanback.records.LoginResponseDTO;
import br.com.kanban.kanbanback.repository.UserRepository;
import br.com.kanban.kanbanback.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new LoginResponseDTO(token);
    }

}
