package project.mockshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.Member;
import project.mockshop.repository.MemberRepository;
import project.mockshop.security.JwtTokenProvider;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String login(LoginRequestDto loginRequestDto) {
        Member findMember = memberRepository.findByLoginId(loginRequestDto.getLoginId())
                .orElseThrow(() -> new NullPointerException("아이디나 비밀번호가 일치하지 않습니다."));

        //비밀번호가 일치하지 않을 때
        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), findMember.getPassword())) {
            throw new NullPointerException("아이디나 비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(loginRequestDto.getLoginId(), "ROLE_" + findMember.getRole(), findMember.getId().toString());
    }
}
