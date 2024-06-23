package kuchat.server.common.exception.notfound;

public class NotFoundMemberException extends NotFoundException{
    public NotFoundMemberException() {
        super("존재하지 않는 회원입니다.", 3002);
    }
}
