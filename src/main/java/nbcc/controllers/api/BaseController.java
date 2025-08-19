package nbcc.controllers.api;

import javassist.NotFoundException;
import nbcc.dtos.ValidationErrorsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static nbcc.dtos.DtoConverters.toValidationErrors;

public abstract class BaseController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ValidationErrorsDTO<?>> validationErrorHandler(MethodArgumentNotValidException exception) {
        var errors = toValidationErrors(exception.getBindingResult());
        return new ResponseEntity<>(
                new ValidationErrorsDTO<>(
                        exception.getTarget(),
                        errors, HttpStatus.BAD_REQUEST.value()
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, ?>> exceptionHandler(Exception ex) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var message = "Something went wrong";

        if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = ex.getMessage();
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            status = HttpStatus.BAD_REQUEST;
            message = status.getReasonPhrase();
        }

        var errorResponse = Map.of(
                "status", status.value(),
                "message", message
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
