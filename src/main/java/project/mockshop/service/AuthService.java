package project.mockshop.service;

import lombok.RequiredArgsConstructor;
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

    public String login(LoginRequestDto loginRequestDto) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginRequestDto.getLoginId());

        //아이디가 존재하지 않을 때
        if (findMember.isEmpty()) {
            throw new NullPointerException("아이디나 비밀번호가 일치하지 않습니다.");
        }

        //비밀번호가 일치하지 않을 때
        if (!findMember.get().getPassword().equals(loginRequestDto.getPassword())) {
            throw new NullPointerException("아이디나 비밀번호가 일치하지 않습니다.");
        }



        return jwtTokenProvider.createToken(loginRequestDto.getLoginId(), findMember.get().getRole().toString(), findMember.get().getId().toString());
    }
}
