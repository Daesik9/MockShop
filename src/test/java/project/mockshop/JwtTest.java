//package project.mockshop;
//
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import project.mockshop.security.JwtTokenProvider;
//
//import javax.crypto.SecretKey;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.assertj.core.api.Assertions.*;
//
//public class JwtTest {
//
//    private JwtTokenProvider jwtTokenProvider;
//    private String base64Str;
//
//    @BeforeEach
//    void beforeEach() {
//        base64Str = "6d20add50475c518631292a6ef9170587e95f8d2265084c1d9a193baa52f7640";
//        jwtTokenProvider = new JwtTokenProvider(base64Str, 1L);
//    }
//
//    @Test
//    void createToken() throws Exception {
//        //given
//
//        //when
//        String token = jwtTokenProvider.createToken("loginid", "ADMIN");
//
//        //then
//        assertThat(jwtTokenProvider.getRole(token)).isEqualTo("ADMIN");
//        assertThat(jwtTokenProvider.getLoginId(token)).isEqualTo("loginid");
//    }
//
//    @Test
//    void validateToken() throws Exception {
//        //given
//        String token = jwtTokenProvider.createToken("loginid", "ADMIN");
//
//        //when
//        boolean isValid = jwtTokenProvider.validateToken(token);
//
//        //then
//        assertThat(isValid).isTrue();
//    }
//
//    @Test
//    void expiredToken() throws Exception {
//        //given
//        jwtTokenProvider = new JwtTokenProvider(base64Str, 1L/100);
//        String token = jwtTokenProvider.createToken("loginid", "ADMIN");
//
//        //when
//        TimeUnit.MILLISECONDS.sleep(100);
//        boolean isValid = jwtTokenProvider.validateToken(token);
//
//        //then
//        assertThat(isValid).isFalse();
//    }
//
//
//
//
//}