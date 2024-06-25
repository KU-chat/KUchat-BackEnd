package kuchat.server.common.config;

import kuchat.server.common.oauth.handler.OAuth2LoginFailureHandler;
import kuchat.server.common.oauth.handler.OAuth2LoginSuccessHandler;
import kuchat.server.common.oauth.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //    @Value("${secret.jwt.secret-key}")
//    private String secretKey;
    private final OAuth2Service oAuth2Service;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/oauth2/login", "member/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .defaultSuccessUrl("/member/signup")         // 로그인 성공 후 리디렉션될 기본 URL 설정
                        .failureUrl("/")         // 로그인 실패 시 리디렉션될 URL 설정
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2Service))            // 사용자 정보를 로드하는 데 사용할 UserService 설정
                )
//                .addFilterBefore(new JwtTokenFilter(memberService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
