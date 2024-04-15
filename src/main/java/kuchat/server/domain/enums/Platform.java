package kuchat.server.domain.enums;

import kuchat.server.common.exception.notfound.NotFoundPlatformException;

public enum Platform {
    GOOGLE("google");
    private String value;

    Platform(String value) {
        this.value = value;
    }

    public static Platform getPlatform(String registrationId) {
        if (registrationId.equals("google")) {
            return GOOGLE;
        }
        throw new NotFoundPlatformException();
    }
}
