package kuchat.server.common.config;

import kuchat.server.common.oauth.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${secret.jwt-secret-key}")
    private static String secretKey;

    private final OAuth2Service oAuth2Service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/swagger-ui/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")            // 사용자 정의 로그인 페이지 URL 설정
                        .defaultSuccessUrl("/oauth/login-info")         // 로그인 성공 후 리디렉션될 기본 URL 설정
                        .failureUrl("/login-error")             // 로그인 실패 시 리디렉션될 URL 설정
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2Service))            // 사용자 정보를 로드하는 데 사용할 UserService 설정
                )
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
                .build();
    }
}
