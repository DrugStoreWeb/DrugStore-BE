package com.github.drug_store_be.config.security;

import com.github.drug_store_be.web.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtTokenProvider jwtTokenProvider;
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http
//                .headers(h->h.frameOptions(f->f.sameOrigin()))
//                .csrf(c->c.disable())
//                .httpBasic(hb-> hb.disable())
//                .formLogin(fl->fl.disable())
//                .rememberMe(rm->rm.disable())
//                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(c-> c.configurationSource(corsConfig()))
//                .authorizeRequests((requests) -> requests
//                                .requestMatchers("/resources/static/**","/auth/sign-up",
//                                        "/auth/login","/auth/email-check","/auth/nickname-check").permitAll()
////                        .anyRequest().authenticated()
//                )
//                .exceptionHandling((exception) -> exception
//                        .authenticationEntryPoint(new CustomerAuthenticationEntryPoint())
//                        .accessDeniedHandler(new CustomerAccessDeniedHandler()))
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//    return http.build();
//    }

    private CorsConfigurationSource corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("token"); //추가
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh", "token"));
        corsConfiguration.setAllowedMethods(List.of("GET","PUT","POST","DELETE"));
        corsConfiguration.setMaxAge(1000L*60*60);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
