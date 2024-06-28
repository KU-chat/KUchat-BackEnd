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
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public SignupResponse signup(String value, String attributeName, SignupRequest signupRequest) {
        System.out.println("=============11111==============");

        Platform platform = Platform.getPlatform(value);
        Member member = memberRepository.findByPlatformAndAttributeName(platform, attributeName)
                .orElseThrow(() -> new NotFoundMemberException());
        System.out.println("============ member id : "+member.getId());


        member.updateInfo(signupRequest);
        System.out.println("=============22222==============");
        // 엑세스 토큰, 리프레시 토큰 발급
        String accessToken = jwtTokenService.generateAccessToken(member.getEmail());
        System.out.println("=============33333==============");

        String refreshToken = jwtTokenService.generateRefreshToken();
        System.out.println("=============44444==============");

        member.updateRefreshToken(refreshToken);

        log.info("[signup] member id : " + member.getId());
        log.info("[signup] Signup request access token: " + accessToken);
        log.info("[signup] Signup request refresh token: " + refreshToken);
        return new SignupResponse(member.getId(), accessToken, refreshToken);
    }

    public Member getMemberById(String memberId) {
        Long id = Long.parseLong(memberId);
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundMemberException());

    }

    public boolean duplicateStudentId(String studentId) {
        return memberRepository.findAllByStudentId(studentId)
                .isPresent();
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException());
    }
}
