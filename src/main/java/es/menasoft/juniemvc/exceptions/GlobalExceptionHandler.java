package es.menasoft.juniemvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

/**
 * Global exception handler for REST controllers.
 * Handles specific exceptions and returns appropriate HTTP responses.
 */
@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles EntityNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the EntityNotFoundException
     * @return a ProblemDetail with status 404 and error details
     */
    @ExceptionHandler(EntityNotFoundException.class)
    ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        
        problemDetail.setTitle("Entity Not Found");
        problemDetail.setType(URI.create("https://api.juniemvc.com/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
}