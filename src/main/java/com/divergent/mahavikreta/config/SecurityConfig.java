package com.divergent.mahavikreta.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().anonymous().disable().sessionManagement().and().authorizeRequests()
//				.antMatchers("/oauth/token", "/api/v1/user/create","/api/v1/webhook/payment","/api/v1/user/forgot",
//						"/api/v1/user/validate_password_link", "/api/v1/company/resend_password",
//						"/api/v1/user/save_password","/api/v1/user/save", "/swagger-ui.html", "/swagger-resources/**", "/v2/**",
//						"/api-docs/**", "/webjars/**", "/api/v1/product/findbyid", "/api/v1/product/productByCatOrSubCat", "/api/v1/user/validatePhoneNumber", "/api/v1/user/otpVerification", "/api/v1/user/sendOtp",
//						"/api/v1/product/searchByProductName")
//				.permitAll();

		http.csrf().disable().authorizeRequests().antMatchers("/api/v1/**" , "/termsandhelp/terms.pdf" , "/termsandhelp/privacynote.pdf").
				permitAll().anyRequest().authenticated().and().httpBasic();

	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
