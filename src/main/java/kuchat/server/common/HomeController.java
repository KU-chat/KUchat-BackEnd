package kuchat.server.common;

import jakarta.servlet.http.HttpServletRequest;
import kuchat.server.domain.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
@Controller
public class HomeController {

    private final JwtTokenService jwtTokenService;

    @GetMapping("")
    public String home(HttpServletRequest request, Model model){
        log.info("[home] 홈화면으로 이동~");

        String token = jwtTokenService.extractAccessToken(request).orElse(null);

        model.addAttribute("token", token);

        return "index";
    }

    // 회원 정보 받기
    @GetMapping("member/signup")
    public String signup(){
        log.info("[signup] 신규회원 정보 받는 화면으로 이동!");
        return "signup";
    }
}
