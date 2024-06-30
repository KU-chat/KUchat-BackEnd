package kuchat.server.common.oauth.service;

import kuchat.server.common.oauth.CustomOAuth2User;
import kuchat.server.common.oauth.dto.OAuth2Attribute;
import kuchat.server.domain.enums.Platform;
import kuchat.server.domain.member.Member;
import kuchat.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    // 구글이 발급한 엑세스 토큰을 가지고 구글에게 사용자 정보 요청하기
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[loadUser] 구글이 발급한 엑세스 토큰을 가지고 구글에게 사용자 정보 요청하기");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Platform platform = Platform.of(registrationId);
        String attributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(platform, attributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(oAuth2Attribute);
        log.info("[loadUser] member : "+member.toString());
        memberRepository.save(member);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())),
                attributes,
                oAuth2Attribute.getNameAttributeKey(),
                member.getEmail(),
                member.getRole(),
                member.getPlatform()
        );
    }

    private Member saveOrUpdate(OAuth2Attribute oAuth2Attribute) {
        Platform platform = oAuth2Attribute.getPlatform();
        String attributeName = oAuth2Attribute.getNameAttributeKey();
        return memberRepository.findByPlatformAndAttributeName(platform, attributeName)
                .orElse(oAuth2Attribute.toMember(platform, oAuth2Attribute.getOAuth2UserInfo()));
    }
}
