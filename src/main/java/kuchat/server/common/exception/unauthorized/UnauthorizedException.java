package kuchat.server.common.exception.unauthorized;

import kuchat.server.common.exception.KuchatException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends KuchatException {
    public UnauthorizedException(String message, int code) {
        super(HttpStatus.UNAUTHORIZED, message, code);
    }
}
