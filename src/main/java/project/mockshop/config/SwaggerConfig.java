package project.mockshop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MOCKSHOP API 명세서",
                description = "MOCKSHOP 프로젝트에 사용되는 API 명세서",
                version = "v1.0.0"
        )
)
public class SwaggerConfig {
    @Bean
    @Profile("local") // 로컬 환경에서만 Swagger 활성화
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components()
                .addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        // Swagger UI 접속 후, 딱 한 번만 accessToken을 입력해주면 모든 API에 토큰 인증 작업이 적용됩니다.
        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
//    private Info apiInfo() {
//        return new Info()
//                .title("API Test") // API의 제목
//                .description("Let's practice Swagger UI") // API에 대한 설명
//                .version("1.0.0"); // API의 버전
//    }
}

