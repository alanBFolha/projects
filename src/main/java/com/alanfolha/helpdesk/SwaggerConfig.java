package com.alanfolha.helpdesk;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	
	private ApiInfo metaInfo() {
	    Contact contact = new Contact("sdcuike", "https://github.com/sdcuike", "rain.mr@foxmail.com");
	    return new ApiInfoBuilder()
	            .title("spring boot practice  API")
	            .description("spring-boot")
	            .termsOfServiceUrl("https://github.com/sdcuike/spring-boot-practice")
	            .contact(contact)
	            .license("Apache License Version 2.0")
	            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
	            .version("1.0.0")
	            .build();
	}
	
	
	@Bean
    public Docket commonApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.alanfolha.helpdesk")).build().apiInfo(metaInfo()).securitySchemes(Lists.newArrayList(apiKey()));
    }

 

    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(null, null, null, null, "", ApiKeyVehicle.HEADER, "Authorization", "");
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
	 
	
}
