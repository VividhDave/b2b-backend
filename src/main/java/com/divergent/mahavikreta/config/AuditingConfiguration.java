package com.divergent.mahavikreta.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class provide {@link Bean} managed by Spring container returning object
 * for {@link SpringSecurityAuditAwareImpl} class.
 * 
 * @author Aakash
 * 
 * @see AuditorAware
 *
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditingConfiguration implements AuditorAware<String> {

	/**
	 * This method returns the username of current auditor of the application.
	 * 
	 * @return username of current auditor i.e. current logged in user.
	 *
	 * @see SecurityContextHolder#getContext()
	 *
	 */
	@Override
	public Optional<String> getCurrentAuditor() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return Optional.of(username);
	}

	/**
	 * This method provides the {@link Bean} managed by Spring container returning
	 * {@link AuditingConfiguration} object.
	 * 
	 * @return {@link AuditorAware}
	 */
	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditingConfiguration();
	}
}
