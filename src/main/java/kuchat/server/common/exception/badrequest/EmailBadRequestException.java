package kuchat.server.common.exception.badrequest;

public class EmailBadRequestException extends BadRequestException{
    public EmailBadRequestException() {
        super("건국대학교 이메일이 아닙니다.", 3004);
    }
}
