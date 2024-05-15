package kuchat.server.domain.enums;

public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public static Gender of(String value) {
        if (value.equals("남성")) {
            return MALE;
        }
        return FEMALE;
    }
}
