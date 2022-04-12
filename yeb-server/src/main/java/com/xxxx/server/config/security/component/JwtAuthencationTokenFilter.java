package com.xxxx.server.config.security.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登入授权过滤器
 */
public class JwtAuthencationTokenFilter extends OncePerRequestFilter {

    // JWT存储的请求头
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    // JWT 负载中拿到开头
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取前端请求头
        String authHeader=request.getHeader(tokenHeader);
        //存在token，存在是以tokenHead开头的
        if(null!=authHeader && authHeader.startsWith(tokenHead)){
            //获取前端请求发送过来的token
            String authToken = authHeader.substring(tokenHead.length());
            //根据token获取用户名
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            //token存在用户名但未登录
            if(null!=username && null==SecurityContextHolder.getContext().getAuthentication()){
                //登录
                UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                //验证token是否有效，重新设置用户对象
                if(jwtTokenUtil.validateToken(authToken,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails,null
                            ,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
