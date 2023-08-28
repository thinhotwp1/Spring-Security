package com.example.springsecurity.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class FilterRequest extends OncePerRequestFilter {

    @Value("${keySecret}")
    private String keySecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            if (request.getHeader("api-key") != null && request.getHeader("api-key").equals(keySecret)) {     // Tạo 1 key đơn giản gán vào header nếu không có thì sẽ trả về 401
                log.info("Nếu key hợp lệ tạo một user để phân quyền sử dụng api, get key từ header api-key: " + request.getHeader("api-key"));

                /*  Vì ở SecurityConfig cấu hình anyRequest().authenticated() nên bất kì request nào tới cũng phải cần phải cấp quyền bằng 1 user security */
                List<GrantedAuthority> listAuthor = new ArrayList<>();
                listAuthor.add(new SimpleGrantedAuthority("admin"));

                // Phải có authentication để set vào SecurityContextHolder nếu không sẽ trả về 401 vì không có authentication
                UserDetails userDetails = new User("user", "pass", listAuthor);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("User phân quyền được tạo: " + SecurityContextHolder.getContext().getAuthentication().toString());

            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("failed on set user authentication", ex);
            response.sendError(401, "Không có quyền !");
        }

    }

}
