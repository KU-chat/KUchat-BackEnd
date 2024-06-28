package kuchat.server.common.oauth.dto;

import kuchat.server.common.exception.notfound.NotFoundPlatformException;
import kuchat.server.common.oauth.userInfo.GoogleOAuth2UserInfo;
import kuchat.server.common.oauth.userInfo.OAuth2UserInfo;
import kuchat.server.domain.enums.Platform;
import kuchat.server.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuth2Attribute {

    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;
    private Platform platform;

    @Builder
    public OAuth2Attribute(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo, Platform platform) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
        this.platform = platform;
    }

    public static OAuth2Attribute of(Platform platform, String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if (platform == Platform.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        }
//        if(platform == Platform.NAVER){
//            return ofNaver(userNameAttributeName, attributes);
//        }
//        if(platform == Platform.KAKAO){
//            return ofKakao(userNameAttributeName, attributes);
//        }
        throw new NotFoundPlatformException();
    }

    private static OAuth2Attribute ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println("[ofGoogle] userNameAttributeName = " + userNameAttributeName);

        return OAuth2Attribute.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .platform(Platform.GOOGLE)
                .build();
    }

    public Member toMember(Platform platform, OAuth2UserInfo oAuth2UserInfo) {
        return Member.builder()
                .email(UUID.randomUUID() + "@socialUser.com")
                .platform(platform)
                .attributeName(oAuth2UserInfo.getId())
                .build();
    }
}
