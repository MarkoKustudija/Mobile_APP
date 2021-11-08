package com.example.mobileappws3.security;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.mobileappws3.repository.UserRepository;
import com.example.mobileappws3.service.UserService;


@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity 
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	private final UserService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	
//	@Value("${security.enable-csrf}")
//    private boolean csrfEnabled;
	
	public WebSecurity(UserService userDetailsService,
			BCryptPasswordEncoder bCryptPasswordEncoder,
			UserRepository userRepository) {
		
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;			
		this.userRepository = userRepository;
	}
	
//	 @Override
//	    protected void configure(HttpSecurity http) throws Exception {
//	       super.configure(http);
//
//	       if(!csrfEnabled)
//	       {
//	         http.csrf().disable();
//	       }
//	    }
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
		.cors().and()
		.csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL)
		.permitAll()
		.antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
		.permitAll()
	    .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL)
		.permitAll()
		.antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
		.permitAll()
		.antMatchers(SecurityConstants.H2_CONSOLE)
		.permitAll()
		.antMatchers("/v2/api-docs","/configuration/**", "/swagger*/**", "/webjars/**")
		.permitAll()
//		.antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
//		.antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("DELETE_AUTHORITY")
		.anyRequest().authenticated().and()
		.addFilter(getAuthenticationFilter())
		.addFilter(new AuthorizationFilter(authenticationManager(), userRepository))
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////		 MOZEMO DA STAVIMO VISE OVLASCENJA SA ->  hasAnyRole("ADMIN", "SUPER_ADMIN") ili hasAnyAuthority("DELETE_AUTHORITY", "DELETE_ALL_AUTHORITY")
//		.anyRequest().authenticated().and()
//		.addFilter(getAuthenticationFilter());
//		.addFilter(new AuthorizationFilter(authenticationManager(), userRepository));	       
		http.headers().frameOptions().disable();
	}
	
	
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);	
	}
	
	
	public AuthenticationFilter getAuthenticationFilter() throws Exception {
		    final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		    filter.setFilterProcessesUrl("/users/login");
		    return filter;
    }
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		final CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("OPTIONS","GET","PUT","POST","DELETE","PATCH"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cashe Control", "Content Type"));
		
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
		
	}


}
