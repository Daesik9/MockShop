package project.mockshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.mockshop.dto.AuthCodeDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.AuthCode;
import project.mockshop.entity.Member;
import project.mockshop.repository.MemberRepository;
import project.mockshop.repository.AuthCodeRepository;
import project.mockshop.security.JwtTokenProvider;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;
    private final AuthCodeRepository authCodeRepository;

    public String login(LoginRequestDto loginRequestDto) {
        Member findMember = memberRepository.findByLoginId(loginRequestDto.getLoginId())
                .orElseThrow(() -> new NullPointerException("아이디나 비밀번호가 일치하지 않습니다."));

        //비밀번호가 일치하지 않을 때
        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), findMember.getPassword())) {
            throw new NullPointerException("아이디나 비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(loginRequestDto.getLoginId(), "ROLE_" + findMember.getRole(), findMember.getId().toString());
    }

    public boolean verifyAuthCode(AuthCodeDto authCodeDto) {
        AuthCode authCode = authCodeRepository.findByEmail(authCodeDto.getEmail());

        if (authCode.getCode().equals(authCodeDto.getAuthCode())) {
            return !authCode.isExpired();
        }

        return false;
    }

    public String sendAuthCode(String toEmail) {
        Optional<Member> byEmail = memberRepository.findByEmail(toEmail);
        if (byEmail.isEmpty()) {
            throw new NoSuchElementException("일치하는 아이디가 없습니다.");
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String code = createCode();
        boolean isSuccess = false;

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject("[MOCKSHOP] 인증번호가 발급 되었습니다.");

            mimeMessageHelper.setText("인증번호는 " + code + " 입니다.");
            javaMailSender.send(mimeMessage);
            isSuccess = true;

            return code;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } finally {
            if (isSuccess) {
                AuthCode authCode = AuthCode.builder()
                        .code(code)
                        .email(toEmail)
                        .expirationTime(LocalDateTime.now().plusMinutes(3))
                        .build();
                authCodeRepository.save(authCode);
            }
        }
    }

    //100000 ~ 999999 사이의 랜덤 인증 코드 생성
    private String createCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);

        return String.valueOf(code);
    }
}
