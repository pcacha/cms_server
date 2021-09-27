package cz.zcu.students.cacha.cms_server.error;

import cz.zcu.students.cacha.cms_server.exceptions.ValidationErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationErrorsHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(400, "Chyba validace", request.getServletPath());

        BindingResult result = exception.getBindingResult();

        Map<String, String> validationErrors = new HashMap<>();

        for(FieldError fieldError: result.getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        apiError.setValidationErrors(validationErrors);

        return apiError;
    }

    @ExceptionHandler({ValidationErrorException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCustomValidationException(ValidationErrorException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(400, "Chyba validace", request.getServletPath());
        apiError.setValidationErrors(exception.getErrors());
        return apiError;
    }
}