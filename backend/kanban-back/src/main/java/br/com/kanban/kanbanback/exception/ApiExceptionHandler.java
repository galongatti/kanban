package br.com.kanban.kanbanback.exception;

import br.com.kanban.kanbanback.records.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(
            BadCredentialsException exception,
            HttpServletRequest request
    ) {
        return errorResponse(
                HttpStatus.UNAUTHORIZED,
                "INVALID_CREDENTIALS",
                "Email ou senha inválidos",
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(
            UserNotFoundException exception,
            HttpServletRequest request
    ) {
        return errorResponse(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(BoardNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleBoardNotFound(
            BoardNotFound exception,
            HttpServletRequest request
    ) {
        return errorResponse(
                HttpStatus.NOT_FOUND,
                "BOARD_NOT_FOUND",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        logger.error("Erro inesperado ao processar requisição {}", request.getRequestURI(), exception);
        return errorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "Ocorreu um erro interno",
                request
        );
    }


    @ExceptionHandler(BoardColumNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleBoardColumNotFound(
            BoardColumNotFound exception,
            HttpServletRequest request
    ) {
        return errorResponse(
                HttpStatus.NOT_FOUND,
                "BOARD_COLUMN_NOT_FOUND",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(ColumnContainsCard.class)
    public ResponseEntity<ApiErrorResponse> handleColumnContainsCard(
            ColumnContainsCard exception,
            HttpServletRequest request
    ) {
        return errorResponse(
                HttpStatus.BAD_REQUEST,
                "COLUMN_CONTAINS_CARD",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(CardNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleCardNotFound(
            CardNotFound exception,
            HttpServletRequest request
    ) {
        return errorResponse(
                HttpStatus.NOT_FOUND,
                "CARD_NOT_FOUND",
                exception.getMessage(),
                request
        );
    }

    private ResponseEntity<ApiErrorResponse> errorResponse(
            HttpStatus status,
            String code,
            String message,
            HttpServletRequest request
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                status.value(),
                code,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }
}
