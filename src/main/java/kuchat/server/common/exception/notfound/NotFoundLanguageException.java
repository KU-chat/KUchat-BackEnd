package kuchat.server.common.exception.notfound;

import kuchat.server.common.exception.KuchatException;
import org.springframework.http.HttpStatus;

public class NotFoundLanguageException extends NotFoundException {
    public NotFoundLanguageException() {
        super("존재하지 않는 언어입니다.", 3001);
    }

}
