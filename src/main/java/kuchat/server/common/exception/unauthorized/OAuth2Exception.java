package kuchat.server.common.exception.unauthorized;

public class OAuth2Exception extends UnauthorizedException{
    public OAuth2Exception() {
        super("소셜 로그인이 제대로 처리되지 않았습니다. 다시 시도해주세요.", 3003);
    }
}
