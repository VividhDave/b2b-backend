package com.divergent.mahavikreta.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	public static final String RESULT = "Result";
	
	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public Docket mahavikretaApi() {
		return new Docket(
				DocumentationType.SWAGGER_2)
				.securityContexts(Arrays.asList(securityContext()))
			     .securitySchemes(Arrays.asList(apiKey())).select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().pathMapping("/").apiInfo(metaData())
				.globalResponseMessage(RequestMethod.GET, getResponseMessages())
				.globalResponseMessage(RequestMethod.POST, getResponseMessages()).useDefaultResponseMessages(false);

	}

	private ApiInfo metaData() {
		return new ApiInfo("Mahavikreta Project API", "Spring Boot REST API for Mahavikreta Project", "1.0.0",
				"Terms of service",
				new Contact("Aakash Chawda", "https://divergentsl.com/", "aakash.chawda@divergentsl.com"), "", "");
	}

	List<ResponseMessage> getResponseMessages() {
		List<ResponseMessage> list = new ArrayList<>();
		list.add(new ResponseMessageBuilder().code(200).message("Success").responseModel(new ModelRef(RESULT)).build());
		list.add(new ResponseMessageBuilder().code(400).message("Bad Request").responseModel(new ModelRef(RESULT))
				.build());
		list.add(new ResponseMessageBuilder().code(401).message("Unauthorized").responseModel(new ModelRef(RESULT))
				.build());
		list.add(new ResponseMessageBuilder().code(403).message("Access denied").responseModel(new ModelRef(RESULT))
				.build());
		list.add(new ResponseMessageBuilder().code(406).message("Not Acceptable").responseModel(new ModelRef(RESULT))
				.build());
		list.add(new ResponseMessageBuilder().code(500).message("Internal Server Error")
				.responseModel(new ModelRef(RESULT)).build());

		return list;
	}

	private ApiKey apiKey() {
		return new ApiKey("Authorization", AUTHORIZATION_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
	}

}