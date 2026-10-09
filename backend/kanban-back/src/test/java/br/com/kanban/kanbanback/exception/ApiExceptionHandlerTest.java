package br.com.kanban.kanbanback.exception;

import br.com.kanban.kanbanback.records.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void badCredentialsReturnUnauthorizedError() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/login");

        var response = handler.handleBadCredentials(
                new BadCredentialsException("internal authentication details"),
                request
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().status());
        assertEquals("INVALID_CREDENTIALS", response.getBody().code());
        assertEquals("Email ou senha inválidos", response.getBody().message());
        assertEquals("/auth/login", response.getBody().path());
    }

    @Test
    void missingUserReturnsNotFoundError() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/42");

        var response = handler.handleUserNotFound(new UserNotFoundException(42L), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("USER_NOT_FOUND", response.getBody().code());
        assertEquals("Usuário com ID 42 não encontrado", response.getBody().message());
        assertEquals("/users/42", response.getBody().path());
    }

    @Test
    void unexpectedErrorsDoNotExposeInternalMessages() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");

        var response = handler.handleUnexpectedException(
                new IllegalStateException("database password leaked"),
                request
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.status());
        assertEquals("INTERNAL_SERVER_ERROR", body.code());
        assertEquals("Ocorreu um erro interno", body.message());
        assertEquals("/users", body.path());
        assertFalse(body.message().contains("database password leaked"));
    }
}
