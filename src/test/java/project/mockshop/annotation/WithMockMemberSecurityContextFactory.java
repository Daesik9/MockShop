package project.mockshop.annotation;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import project.mockshop.dto.CustomUserDetails;
import project.mockshop.entity.Member;

public class WithMockMemberSecurityContextFactory implements WithSecurityContextFactory<WithMockMember> {
    @Override
    public SecurityContext createSecurityContext(WithMockMember mockMember) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = Member.memberBuilder()
                .id(mockMember.id())
                .loginId(mockMember.loginId())
                .role(mockMember.role())
                .build();

        CustomUserDetails principal = new CustomUserDetails(member);
        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);

        return context;
    }
}
