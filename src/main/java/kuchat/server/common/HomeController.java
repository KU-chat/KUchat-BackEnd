package kuchat.server.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        log.info("[HomeController : home] 홈화면으로 이동~");
        return "index";
    }

    // 회원 정보 받기
    @GetMapping("/member/signup")
    public String signup(){
        log.info("[HomeController : signup] 신규회원 정보 받는 화면으로 이동!");
        return "signup";
    }
}
