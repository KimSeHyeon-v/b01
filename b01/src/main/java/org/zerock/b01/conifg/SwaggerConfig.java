package org.zerock.b01.conifg;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api(){
        return new OpenAPI().components(new Components()).info(apiInfo());
    }

    private Info apiInfo(){
        return new Info().title("Boot 01 Project Swagger").version("1.0.0");

    }

    @Bean
    public GroupedOpenApi customApi1() {
        return GroupedOpenApi.builder()
                .group("BO1 API") // 그룹 이름
                .packagesToScan("org.zerock.b01.controller") // 스캔할 패키지
                .build();
    }

/*    @Bean
    public GroupedOpenApi customApi2() {
        return GroupedOpenApi.builder()
                .group("BO1 API") // 그룹 이름
                .pathsToMatch("/board/**")
                *//*.packagesToScan("org.zerock.b01.controller") // 스캔할 패키지*//*
                .build();
    }*/
}
