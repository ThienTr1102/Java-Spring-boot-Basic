package com.example.demo.utils;

import com.example.demo.services.CustomUserDetailService;
import com.example.demo.services.OAuthService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuthService oAuthService;
    private final UserService userService;

    //trả về một CustomUserDetailService,
    //được sử dụng để cung cấp thông tin chi tiết về người dùng.
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

    //trả về một CustomUserDetailService,
    //được sử dụng để cung cấp thông tin chi tiết về người dùng.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //trả về một
    //DaoAuthenticationProvider, được sử dụng để định nghĩa cách xác thực người
    //dùng
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    //trả về một
    //SecurityFilterChain, được sử dụng để cấu hình các hành vi bảo mật trong
    //ứng dụng.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/css/**", "/js/**", "/", "/register", "/error")
                        .permitAll()
                        .requestMatchers( "/books/edit", "/books/delete")
                        .hasAnyAuthority("ADMIN")
                        .requestMatchers("/books", "/books/add")
                        .hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/api/**")
                        .hasAnyAuthority("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/login")
                                .failureUrl("/login?error")
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(oAuthService)
                                )
                                .successHandler(
                                        (request, response,
                                         authentication) -> {
                                            var oidcUser =
                                                    (DefaultOidcUser) authentication.getPrincipal();
                                            userService.saveOauthUser(oidcUser.getEmail(), oidcUser.getName());
                                            response.sendRedirect("/books");
                                        }
                                )
                                .permitAll())
                .rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                        .userDetailsService(userDetailsService())
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedPage("/403"))
                .build();
    }
}