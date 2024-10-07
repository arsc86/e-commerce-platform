package net.project.ecommerce.msa.api.bff.config.security;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityManager {
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SecurityConfiguration securityConfiguration;		
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		 return authenticationConfiguration.getAuthenticationManager();
	}
	
	@SuppressWarnings("removal")
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {	
						
		http.csrf().disable().cors().and();
		
		//Setting matchers from properties
		for (Map.Entry<String, String> path : securityConfiguration.getAuthorizedUris().entrySet()) 
		{
			try 
			{
				http
				    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
	            	.requestMatchers(new AntPathRequestMatcher(path.getKey(),path.getValue())).permitAll()	
	            );
			} 
			catch (Exception e) 
			{
				log.error("Error configurando endpoints sin autorizacion {}, ERROR : ",path.getKey(),e.getMessage());
			}
		}
								
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated());			       
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterAfter(new AuthorizationFilter(securityConfiguration,
        		                                    authorizationService
        		                                   ),
        		            UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
	
	@Primary
	@Bean
	FilterRegistrationBean<CorsFilter> customCorsFilterAuth() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(securityConfiguration.getOrigins());
		config.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
		config.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
		config.setMaxAge((long) 86400);
		source.registerCorsConfiguration("/**", config);
		
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setName("CORS");
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    	return bean;
	}
}
