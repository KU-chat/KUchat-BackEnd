package kuchat.server.domain.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kuchat.server.common.exception.unauthorized.MalformedTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${secret.jwt-secret-key}")
    private static String secretKey;

    @Value("${secret.jwt-expired-in}")
    private static long expireTime;

    public static String generateToken(Long id) {
        // claims = jwt token에 들어갈 정보, claim에 id를 넣어줘야 회원 식별 가능
        final Claims claims = Jwts.claims();
        claims.put("id", id);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 멤버의 id 추출
    public static String extractId(String token) {
        try{
            return getBody(token)
                    .get("id")
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
}
