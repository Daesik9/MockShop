package project.mockshop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.mockshop.security.JwtAuthenticationFilter;
import project.mockshop.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);

        //Form 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        //http basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/swagger-ui/**", "/v3/**").permitAll()
                        .requestMatchers("/api/auth/**", "/api/users/find/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() //회원가입 누구나 가능
                        .requestMatchers("/api/users/check-duplicate/**").permitAll()
                        .requestMatchers("/api/events/**").permitAll()
                        .requestMatchers("/api/items/best-five", "/api/items/search").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("CUSTOMER")
                        .requestMatchers("/api/users/*/coupons").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/events/*/participants").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasRole("CUSTOMER")
                        .requestMatchers("/api/orders/*").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/admin/**", "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/events").hasRole("ADMIN")
                        .requestMatchers("/api/merchants/*/items").hasRole("ADMIN")
                        .requestMatchers("/api/orders/merchants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/items").hasRole("MERCHANT")
                        .requestMatchers(HttpMethod.PUT, "/api/users/reset-password").permitAll()
                        .requestMatchers("/api/items/*").permitAll()
                        .anyRequest().authenticated());

        http
                .addFilterAt(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}