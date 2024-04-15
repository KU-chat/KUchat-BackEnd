package kuchat.server.common.exception.notfound;

import kuchat.server.common.exception.KuchatException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends KuchatException {
    public NotFoundException(String message, int code) {
        super(HttpStatus.NOT_FOUND, message, code);
    }
}
