package project.mockshop.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockMemberSecurityContextFactory.class)
public @interface WithMockMember {

    long id() default 1L;
    String loginId() default "loginid";
    String role() default "ROLE_CUSTOMER"; //hasRole로 설정하면 앞에 ROLE_ 접두사가 있어야함. 접두사 없이 하려면 hasAuthority로 설정.

}
