package com.InfinityInscribe.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtConfigurationFilter JwtAuthenticationFilter;

    private final AuthenticationProvider AuthenticationProvider;

    private static final Logger logger=LoggerFactory.getLogger(SecurityConfig.class);
    public SecurityConfig( JwtConfigurationFilter jwtAuthenticationFilter,
                           AuthenticationProvider authenticationProvider) {
        this.JwtAuthenticationFilter = jwtAuthenticationFilter;
        this.AuthenticationProvider = authenticationProvider;
    }

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/",
            "api/**",
            "/swagger",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/files/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
            throws  Exception {

        logger.info("Filter chain is executing");
        httpSecurity
                .headers(h->h.disable())
                .csrf(csrf->csrf.disable())
                .cors().and()
                .authorizeHttpRequests(requests->
                        requests.requestMatchers( AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(AuthenticationProvider)
                .addFilterBefore(JwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        //.httpBasic(Customizer.withDefaults())
        ;

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200"); // Add your frontend's origin here
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
