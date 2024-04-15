package kuchat.server.common.exception.notfound;

public class NotFoundPlatformException extends NotFoundException{
    public NotFoundPlatformException() {
        super("존재하지 않는 플랫폼입니다.", 2001);
    }
}
