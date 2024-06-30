package kuchat.server.domain.member.service;

import kuchat.server.common.exception.notfound.NotFoundMemberException;
import kuchat.server.domain.enums.Platform;
import kuchat.server.domain.jwt.JwtTokenService;
import kuchat.server.domain.member.Member;
import kuchat.server.domain.member.dto.SignupRequest;
import kuchat.server.domain.member.dto.SignupResponse;
import kuchat.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public SignupResponse signup(String value, String attributeName, SignupRequest signupRequest) {

        Platform platform = Platform.of(value);
        Member member = memberRepository.findByPlatformAndAttributeName(platform, attributeName)
                .orElseThrow(() -> new NotFoundMemberException());
        member.updateInfo(signupRequest);

        // 엑세스 토큰, 리프레시 토큰 발급
        String accessToken = jwtTokenService.generateAccessToken(member.getEmail());
        String refreshToken = jwtTokenService.generateRefreshToken();
        member.updateRefreshToken(refreshToken);

        log.info("[signup] member id : " + member.getId());
        log.info("[signup] Signup request access token: " + accessToken);
        log.info("[signup] Signup request refresh token: " + refreshToken);
        return new SignupResponse(member.getId(), accessToken, refreshToken);
    }

    public boolean duplicateStudentId(String studentId) {
        return memberRepository.findAllByStudentId(studentId)
                .isPresent();
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException());
    }
}
