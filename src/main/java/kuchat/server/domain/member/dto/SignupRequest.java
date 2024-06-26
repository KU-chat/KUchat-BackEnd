package kuchat.server.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SignupRequest {
    private String setLanguage;          // SettingLanguage 타입
    private String firstLanguage;         // LearnLanguage
    private String secondLanguage;       // LearnLanguage
    private String hometown;
    private String name;
    private String department;
    private String studentId;
    private String gender;
    private String birthday;
    private String email;
}
