package kuchat.server.domain.enums;

import kuchat.server.common.exception.notfound.NotFoundPlatformException;
import lombok.Getter;

@Getter
public enum Platform {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private String value;

    Platform(String value) {
        this.value = value;
    }

    public static Platform getPlatform(String registrationId) {
        for(Platform platform : Platform.values()){
            if(platform.getValue().equals(registrationId)){
                return platform;
            }
        }
        throw new NotFoundPlatformException();
    }
}
