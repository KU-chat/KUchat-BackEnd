package kuchat.server.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kuchat.server.domain.member.dto.SignupRequest;
import kuchat.server.domain.member.dto.SignupResponse;
import kuchat.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Member", description = "회원")
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입 처리하기
    @Operation(summary = "회원가입")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestParam String platform,
                                                 @RequestParam String attributeName,
                                                 @RequestBody SignupRequest signupRequest) {
        log.info("[signup] parameter : platform = {}, attributeName = {}", platform, attributeName);
        log.info("[signup] request : {}", signupRequest.toString());
        if (memberService.duplicateStudentId(signupRequest.getStudentId())) {
            System.out.println("[error] 이미 존재하는 학번입니다.");
        }

        SignupResponse response = memberService.signup(platform, attributeName, signupRequest);
        return ResponseEntity.ok(response);
    }


}
