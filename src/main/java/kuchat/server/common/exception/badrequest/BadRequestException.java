package kuchat.server.common.exception.badrequest;

import kuchat.server.common.exception.KuchatException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends KuchatException {
    public BadRequestException(String message, int code) {
        super(HttpStatus.BAD_REQUEST, message, code);
    }
}
