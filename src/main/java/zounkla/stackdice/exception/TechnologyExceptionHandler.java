package zounkla.stackdice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class TechnologyExceptionHandler {


    @ExceptionHandler({
            TechnologyNotFoundException.class,
            CategoryNotFoundException.class,
            CompatibilityNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(Exception ex) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception ex) {
        String message = ex.getMessage();

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            message = "Erreur de validation : " + validationEx.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + " (" + error.getDefaultMessage() + ")")
                    .collect(Collectors.joining(", "));
        }

        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now());
        error.setHttpStatus(status.value());
        error.setMessage(message);
        return ResponseEntity.status(status).body(error);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(Exception ex, HttpStatus status) {
        return createErrorResponse(ex.getMessage(), status);
    }
}
