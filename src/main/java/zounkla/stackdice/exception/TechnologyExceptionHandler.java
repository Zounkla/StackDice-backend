package zounkla.stackdice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class TechnologyExceptionHandler {


    @ExceptionHandler({
            TechnologyNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(Exception ex) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<ErrorResponse> createErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now());
        error.setHttpStatus(status.value());
        error.setMessage(ex.getMessage());
        return ResponseEntity.status(status).body(error);
    }
}
