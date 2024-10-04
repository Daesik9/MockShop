package project.mockshop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.mockshop.dto.CustomUserDetails;
import project.mockshop.entity.*;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@PropertySource("classpath:jwt.yml")
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long expirationMinutes;

    public JwtTokenProvider(@Value("${jwt-secret}") String secret,
                            @Value("${expiration-minutes}") long expirationMinutes
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMinutes = expirationMinutes;
    }

    public String getLoginId(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getMemberId(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getId();
    }

    //JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        //토근에서 username과 role 획득
        String loginId = getLoginId(accessToken);
        String role = getRole(accessToken);
        String id = getMemberId(accessToken);

        //Member Entity를 생성하여 값 set
        //DB에서 비밀번호를 가져오면 매번 조회하는 불상사가 생기기에
        //ContextHolder에는 굳이  정확한 비밀번호를 넣을 필요가 없기에 임시로 넣어줌.
        Member member;
        switch (role) {
            case "CUSTOMER":
                member = Customer.builder()
                        .id(Long.valueOf(id))
                        .loginId(loginId)
                        .role(role)
                        .password("Temppassword1!")
                        .build();
                break;
            case "MERCHANT":
                member = Merchant.builder()
                        .id(Long.valueOf(id))
                        .loginId(loginId)
                        .role(role)
                        .password("Temppassword1!")
                        .build();
                break;
            case "ADMIN":
                member = Admin.builder()
                        .id(Long.valueOf(id))
                        .loginId(loginId)
                        .role(role)
                        .password("Temppassword1!")
                        .build();
                break;
            default:
                member = Customer.builder()
                        .id(Long.valueOf(id))
                        .loginId(loginId)
                        .role(role)
                        .password("Temppassword1!")
                        .build();
                break;
        }

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload().getExpiration().after(new Date());
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (JwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    public String createToken(String loginId, String role, String id) {
        Date now = new Date(System.currentTimeMillis());

        log.info("{}", now);

        return Jwts.builder()
                .subject(loginId)
                .id(id)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMinutes * 1000 * 60))
                .signWith(secretKey)
                .compact();
    }

}
