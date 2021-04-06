package com.example.rs256.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.rs256.service.CustomUserDetailService;
import com.example.rs256.service.CustomUserDetailService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtRequestFilter extends OncePerRequestFilter{

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4YWRtaW4iLCJleHAiOjE1OTUxODMyMTcsImlhdCI6MTU5NTE0NzIxN30.S9w8LROFAKYCWU2nsHu07FcWGfI5vwesC4MvsqzGDyo
        String autherizationHeader = request.getHeader("Authorization");
        String token = null;
        String username=null;
        if(autherizationHeader !=null && autherizationHeader.startsWith("Bearer"))
        {
            token = autherizationHeader.substring(7);
            try{
                username = jwtTokenUtil.extractUsername(token);
                System.out.println("Username in token "+username);
            } catch (IllegalArgumentException e)
            {
                System.out.println("JWT Token has expired");
            }

        }
        else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            System.out.println("a");
            UserDetails userDetails = service.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            System.out.println("b");
        }

        System.out.println(request);
        System.out.println(response);
        filterChain.doFilter(request, response);
        System.out.println("c");
    }
}
