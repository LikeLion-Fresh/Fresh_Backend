package com.example.demo.config;
import com.example.demo.config.handler.CustomOAuth2SuccessHandler;
import com.example.demo.config.oauth.CustomAuthenticationSuccessHandler;
import com.example.demo.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/subscribe/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_SUB')")
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("http://localhost:3000", true) // 항상 리다이렉트
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .successHandler(customOAuth2SuccessHandler) // 커스텀 OAuth2 성공 핸들러 설정
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}
