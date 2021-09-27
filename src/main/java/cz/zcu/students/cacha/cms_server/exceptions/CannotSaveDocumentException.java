package cz.zcu.students.cacha.cms_server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotSaveDocumentException extends RuntimeException{
    public CannotSaveDocumentException(String message) {
        super(message);
    }
}
