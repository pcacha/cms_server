package cz.zcu.students.cacha.cms_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotUpdateReviewException extends RuntimeException {
    public CannotUpdateReviewException(String message) {
        super(message);
    }
}