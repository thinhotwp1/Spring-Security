package com.example.springsecurity.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
@Log4j2
public class SecurityConfig {

    @Autowired
    CustomAuthEntryPoint customAuthentEntryPoint;

    @Autowired
    FilterRequest filterRequest;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        BasicAuthenticationEntryPoint basicAuth = new BasicAuthenticationEntryPoint();
        log.info("Filter request trước khi vào Controller");

        http.csrf().disable();       // Cấu hình chống tấn công giả mạo yêu cầu (CSRF): disable

        http.cors().disable();   // Cấu hình trao đổi dữ liệu giữa các nguồn khác (CORS): disable

        http.authorizeHttpRequests((authorize) -> authorize     // Có thể bỏ cấu hình này nếu không muốn tạo user details và phân quyền
                .requestMatchers("/login").permitAll()
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
