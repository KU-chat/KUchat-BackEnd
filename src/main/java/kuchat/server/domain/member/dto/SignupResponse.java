package kuchat.server.domain.member.dto;

import lombok.Getter;

@Getter
public class SignupResponse {
    private Long id;
    private Long accessToken;
    private Long refreshToken;

    public SignupResponse(Long id, Long accessToken, Long refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
