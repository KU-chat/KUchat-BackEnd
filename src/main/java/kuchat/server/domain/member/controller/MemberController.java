package kuchat.server.domain.member.controller;

import kuchat.server.domain.member.dto.SignupRequest;
import kuchat.server.domain.member.dto.SignupResponse;
import kuchat.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    // 회원가입 처리하기
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        log.info("[MemberController - signup request : {}]", signupRequest);
        if (memberService.duplicateStudentId(signupRequest.getStudentId())) {
            System.out.println("[error] 이미 존재하는 학번입니다.");
        }

        SignupResponse response = memberService.signup(signupRequest);
        return ResponseEntity.ok(response);
    }


}
