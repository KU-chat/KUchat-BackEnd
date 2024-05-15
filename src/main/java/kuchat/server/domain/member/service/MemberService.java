package kuchat.server.domain.member.service;

import kuchat.server.domain.member.Member;
import kuchat.server.domain.member.dto.SignupRequest;
import kuchat.server.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Long signup(SignupRequest signupRequest) {
        Member member = memberRepository.save(new Member(signupRequest));
        // 엑세스 토큰 발급
        // 리프레시 토큰 발급
//        return new SignupResponse(member.getId(), );
        return member.getId();
    }
}
