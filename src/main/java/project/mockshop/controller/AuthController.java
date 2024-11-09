package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.mockshop.dto.AuthCodeDto;
import project.mockshop.dto.EmailDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.response.Response;
import project.mockshop.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public Response login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto);
        return Response.success(token);
    }

    @PostMapping("/send-auth-code")
    public Response sendAuthCode(@RequestBody EmailDto emailDto) {
        String resetCode = authService.sendAuthCode(emailDto.getToEmail());

        return Response.success(resetCode);
    }

    @PostMapping("/verify-auth-code")
    public Response verifyAuthCode(@RequestBody AuthCodeDto authCodeDto) {
        log.info("verifyAuthCode");
        boolean isValid = authService.verifyAuthCode(authCodeDto);

        return Response.success(isValid);
    }
}