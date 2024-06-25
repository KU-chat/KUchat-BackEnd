package kuchat.server.common.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import kuchat.server.common.exception.unauthorized.UnauthorizedException;
import kuchat.server.common.oauth.CustomOAuth2User;
import kuchat.server.domain.enums.Role;
import kuchat.server.domain.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service

public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("[OAuth2LoginSuccessHandler] 로그인 성공 이후!");
        try {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // 회원가입한 회원인 경우 (Role = GUEST)
            // email로 access token 발급, 요청 헤더에 추가 -> 회원 추가정보 작성 폼으로 리다이렉트
            // 나중에 정보 입력 다 받으면 Role.STUDENT 로 업데이트 시켜야함
            if (customOAuth2User.getRole() == Role.GUEST) {
                String accessToken = JwtTokenUtil.generateAccessToken(customOAuth2User.getEmail());
                response.addHeader(JwtTokenUtil.getAccessHeader(), "Bearer " + accessToken);
//                response.sendRedirect("member/signup");       // 회원 추가정보 받는 화면으로 리다이렉트
                response.sendRedirect("/");       // 회원 추가정보 받는 화면으로 리다이렉트
                jwtTokenUtil.sendAccessAndRefreshToken(response, accessToken, null);
            }

            // 기존 회원인 경우 (Role = STUDENT)
            // 로그인 처리 하기
            else {
                String accessToken = jwtTokenUtil.generateAccessToken(customOAuth2User.getEmail());
                String refreshToken = jwtTokenUtil.generateRefreshToken();
                response.addHeader(jwtTokenUtil.getAccessHeader(), "Bearer " + accessToken);
                response.addHeader(jwtTokenUtil.getRefreshHeader(), "Bearer " + refreshToken);

                jwtTokenUtil.sendAccessAndRefreshToken(response, accessToken, refreshToken);
                jwtTokenUtil.updateRefreshToken(customOAuth2User.getEmail(), refreshToken);
            }
        } catch (Exception e) {
            throw new UnauthorizedException("소셜 로그인이 제대로 처리되지 않았습니다. 다시 시도해주세요.", 3003);
        }
    }
}
