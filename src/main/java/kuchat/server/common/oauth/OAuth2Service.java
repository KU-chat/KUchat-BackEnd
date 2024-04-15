package kuchat.server.common.oauth;

import jakarta.servlet.http.HttpSession;
import kuchat.server.common.oauth.dto.OAuth2Attribute;
import kuchat.server.domain.enums.Platform;
import kuchat.server.domain.member.Member;
import kuchat.server.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    // 구글이 발급한 엑세스 토큰을 가지고 구글에게 사용자 정보 요청하기
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 엑세스 토큰 가져오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId,
                userNameAttributeName, oAuth2User.getAttributes());

        Platform platform = Platform.getPlatform(registrationId);

        Member member = saveOrUpdate(oAuth2Attribute);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())),
                oAuth2Attribute.getAttributes(),
                oAuth2Attribute.getUserNameAttributeName()
        );
    }

    private Member saveOrUpdate(OAuth2Attribute oAuth2Attribute) {
        Member member = memberRepository.findByPlatformAndEmail(oAuth2Attribute.getPlatform(), oAuth2Attribute.getEmail())
                .orElse(oAuth2Attribute.toMember());
        return memberRepository.save(member);
    }
}
