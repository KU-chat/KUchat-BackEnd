package kuchat.server.domain.member.dto;

import lombok.Getter;

@Getter
public class SignupResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;

    public SignupResponse(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
