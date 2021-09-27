package cz.zcu.students.cacha.cms_server.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Data
public class ValidationErrorException extends RuntimeException {
    private HashMap<String, String> errors;

    public ValidationErrorException(HashMap<String, String> errors) {
        this.errors = errors;
    }
}
