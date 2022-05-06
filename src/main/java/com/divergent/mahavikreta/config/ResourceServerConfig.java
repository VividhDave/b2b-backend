package com.divergent.mahavikreta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource_id";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeRequests()
//				.antMatchers("/oauth/token", "/api/v1/user/save","/api/v1/webhook/payment","/api/v1/product/getAll","/api/v1/brand/getAll","/api/v1/category/getAll","/api/v1/subcategory/getAll","/api/v1/user/validate_password_link",
//						"/api/v1/user/forgot", "/api/v1/company/resend_password", "/api/v1/user/save_password",
//						"/swagger-ui.html", "/swagger-resources/**", "/v2/**", "/api-docs/**", "/webjars/**", "/api/v1/product/findbyid", "/api/v1/product/productByCatOrSubCat", "/api/v1/user/validatePhoneNumber", "/api/v1/user/otpVerification", "/api/v1/user/sendOtp",
//						"/api/v1/product/searchByProductName")
//				.permitAll().anyRequest().authenticated();

		http.csrf().disable().authorizeRequests().antMatchers("/api/v1/**").permitAll().anyRequest().authenticated().and().httpBasic();

	}

}
