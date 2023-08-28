package com.example.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    CustomAuthEntryPoint customAuthentEntryPoint;

    @Autowired
    FilterRequest filterRequest;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        BasicAuthenticationEntryPoint basicAuth = new BasicAuthenticationEntryPoint();

        http.csrf().disable();       // Cấu hình chống tấn công giả mạo yêu cầu (CSRF): disable

        http.cors().disable();   // Cấu hình trao đổi dữ liệu giữa các nguồn khác (CORS): disable

        http.authorizeHttpRequests((authorize) -> authorize     // Cấu hình các request tới server
                .requestMatchers("/login").permitAll()  //Cho request http://localhost:8080/login pass qua mà không cần filter
                .anyRequest().authenticated());

        http.exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(basicAuth))    // 1.Nếu không có quyền thì trả về lỗi tự basic auth
                .authenticationEntryPoint(customAuthentEntryPoint));   // 2.Nếu không có quyền thì trả về lỗi tự custom

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));   //STATELESS sẽ tắt quản lý phiên làm việc

        http.addFilterBefore(filterRequest, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
