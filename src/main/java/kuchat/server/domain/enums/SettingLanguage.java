package kuchat.server.domain.enums;

import kuchat.server.common.exception.notfound.NotFoundLanguageException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SettingLanguage {
    ENGLISH("영어"),
    KOREAN("한국어");

    private String value;
    SettingLanguage(String value){
        this.value = value;
    }

    public static SettingLanguage of(String value) {

        return Arrays.stream(values())
                .filter(language -> value.equals(language.getValue()))
                .findFirst()
                .orElseThrow(NotFoundLanguageException::new);
    }


}
