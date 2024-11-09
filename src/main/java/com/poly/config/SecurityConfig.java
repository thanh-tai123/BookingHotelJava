package com.poly.config;

import com.poly.auth.UserRootService;
import com.poly.auth.OAuthUser.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserRootService userRootService;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public BCryptPasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean()
  public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userRootService);
    authProvider.setPasswordEncoder(getPasswordEncoder());
    return authProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            req -> req
                .requestMatchers("/admin", "/admin/**", "/account/info","/api/**").authenticated()
                .anyRequest().permitAll())
                
        .formLogin(form -> form
            .loginPage("/account/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .loginProcessingUrl("/account/login-check")
            .defaultSuccessUrl("/account/login/success")
            .failureUrl("/account/login/failure"))
        .oauth2Login(ou -> ou
            .authorizationEndpoint(e -> e.baseUri("/oauth2/authorization"))
            .redirectionEndpoint(e -> e.baseUri("/login/oauth2/code/*"))
            .userInfoEndpoint(e -> e.userService(customOAuth2UserService)))
        .exceptionHandling(e -> e.accessDeniedPage("/account/accessDenied"))
        .build();
  }
  
  @Bean
  public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedOrigin("http://localhost:5500/index.html");
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      
      source.registerCorsConfiguration("/**", config);
      return new CorsFilter();
  }
}
