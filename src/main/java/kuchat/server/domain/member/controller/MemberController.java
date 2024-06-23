package kuchat.server.domain.member.controller;

import kuchat.server.domain.member.dto.SignupRequest;
import kuchat.server.domain.member.dto.SignupResponse;
import kuchat.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController

public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        log.info("[MemberController - signup request : {}]", signupRequest);
        SignupResponse response = memberService.signup(signupRequest);
        return ResponseEntity.ok(response);
//        return memberService.signup(signupRequest);
    }
}
