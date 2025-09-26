package com.jungsan.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("정산 앱 백엔드 API")
                        .description("여행 및 게임 정산을 위한 REST API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("정산 앱 개발팀")
                                .email("dev@jungsan.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("개발 서버"),
                        new Server()
                                .url("https://api.jungsan.com/api")
                                .description("운영 서버")
                ));
    }
}
