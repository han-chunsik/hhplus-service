package kr.hhplus.be.server.config.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "Authorization", // Swagger UI에서 입력하는 이름
        description = "대기열 Token을 입력해주세요.",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .security(securityRequirements()); // Security 설정 적용
    }

    private List<SecurityRequirement> securityRequirements() {
        List<SecurityRequirement> securityRequirements = new ArrayList<>();
        securityRequirements.add(new SecurityRequirement().addList("Authorization")); // Authorization 헤더 추가
        return securityRequirements;
    }

    private Info apiInfo() {
        return new Info()
                .title("콘서트 예약 서비스")
                .description("항해 플러스 7기 한성경 콘서트 서비스")
                .version("1.0.0");
    }
}