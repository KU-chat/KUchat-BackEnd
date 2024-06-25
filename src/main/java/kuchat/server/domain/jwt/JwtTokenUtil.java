package kuchat.server.domain.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kuchat.server.common.exception.notfound.NotFoundMemberException;
import kuchat.server.common.exception.unauthorized.MalformedTokenException;
import kuchat.server.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${secret.jwt.secret-key}")
    private static String secretKey;

    @Value("${secret.jwt.access.expiration}")
    private static long accessTokenExpiration;

    @Value("${secret.jwt.refresh.expiration}")
    private static long refreshTokenExpiration;

    @Value("${secret.jwt.access.header}")
    private static String accessHeader;

    @Value("${secret.jwt.refresh.header}")
    private static String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "access_token";
    private static final String REFRESH_TOKEN_SUBJECT = "refresh_token";
    private final MemberRepository memberRepository;

    public JwtTokenUtil(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    // GoogleOAuth2UserInfo의 email을 사용하여 token 발급
    public static String generateAccessToken(String email) {
        final Claims claims = Jwts.claims();        // claims = jwt token에 들어갈 정보, claim에 id를 넣어줘야 회원 식별 가능
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(ACCESS_TOKEN_SUBJECT)       // subject : 토큰의 주체/사용자를 식별하기 위해 사용됨
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static String generateRefreshToken() {
        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)       // subject : 토큰의 주체/사용자를 식별하기 위해 사용됨
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // response header에 access_token 실어서 보내기
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 access token : {}", accessToken);
    }

    // response header에 access_token과 refresh_token 실어서 보내기
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("재발급된 access token : {} , refresh token : {}", accessToken, refreshToken);
        log.info("access token, refresh token 헤더에 추가 완료");
    }

    public static Optional<String> extractAccessToken(HttpServletRequest request) {            // Q. 이거말고 더 적합한 req, resp 클래스는 없을까???????
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(str -> str.startsWith("Bearer "))
                .map(accessToken -> accessToken.replace("Bearer ", ""));
    }

    public static Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(str -> str.startsWith("Bearer "))
                .map(refreshToken -> refreshToken.replace("Bearer ", ""));
    }

    // 토큰에서 멤버의 email 추출
    public static String extractEmail(String token) {
        try {
            return getBody(token)
                    .get("email")
                    .toString();
        } catch (Exception e) {
            throw new MalformedTokenException();
        }
    }

    // 토큰의 만료시간이 지났는지 확인
    public static boolean isExpired(String token) {
        Date expiredDate = getBody(token).getExpiration();
        return expiredDate.before(new Date());
    }


    private static Claims getBody(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJwt(token)
                .getBody();
    }

    public static String getAccessHeader() {
        return accessHeader;
    }

    public static String getRefreshHeader() {
        return refreshHeader;
    }

    public void updateRefreshToken(String email, String refreshToken) {
        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new NotFoundMemberException()
                );
    }
}
