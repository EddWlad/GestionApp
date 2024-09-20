package com.tidsec.devengacion_inventarios.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tidsec.devengacion_inventarios.service.impl.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private PermitAuthorizationManager permitAuthorizationManager;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(x -> x.disable())
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers("/", "/css/*", "/js/*", "/login").permitAll();
					auth.requestMatchers("/users/**", "/roles/**", "/technicalGroups/**", "/materials/**")
							.access(permitAuthorizationManager);
					auth.anyRequest().authenticated();
				})
				.formLogin(form -> {
					form.loginPage("/login");
					form.successHandler(successHandler());
					form.permitAll();
					form.failureUrl("/login?error=true");
				}).sessionManagement(session -> {
					session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
					session.invalidSessionUrl("/login");
					session.maximumSessions(1);
					session.sessionFixation(s -> {
						s.migrateSession();
					});
				}).logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll())
				.build();
	}

	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	public AuthenticationSuccessHandler successHandler() {
		return ((request, response, authentication) -> {
			response.sendRedirect("/technicalGroups");
		});
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailServiceImpl) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailServiceImpl);
		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
