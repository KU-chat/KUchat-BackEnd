package kuchat.server.common.oauth.dto;

import kuchat.server.common.exception.notfound.NotFoundPlatformException;
import kuchat.server.domain.enums.Platform;
import kuchat.server.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attribute {

    private Map<String, Object> attributes;
    private String userNameAttributeName;
    private String email;
    private Platform platform;

    @Builder
    public OAuth2Attribute(Map<String, Object> attributes, String userNameAttributeName, String email) {
        this.attributes = attributes;
        this.userNameAttributeName = userNameAttributeName;
        this.email = email;
    }

    public static OAuth2Attribute of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        // 나중엔 네이버도 추가하기
//        if ("naver".equals(registrationId)){
//            return ofNaver("id", attributes);
//        }
        throw new NotFoundPlatformException();
    }

    private static OAuth2Attribute ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .attributes(attributes)
                .email((String) attributes.get("email"))
                .userNameAttributeName(userNameAttributeName)
                .build();
    }

    public Member toMember() {
        return new Member(email, platform, userNameAttributeName);
    }
}
