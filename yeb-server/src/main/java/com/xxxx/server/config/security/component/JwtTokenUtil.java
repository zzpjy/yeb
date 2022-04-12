package com.xxxx.server.config.security.component;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken工具类
 */
@Component
public class JwtTokenUtil {

    //荷载里面，用户名key
    private static final String CLAIM_KEY_USERNAME="sub";
    //Jwt创建时间
    private static final String CLAIM_KEY_CREATED="created";
    //jwt加载秘钥
    @Value("${jwt.secret}")
    private String secret;
    //jwt失效时间
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成Token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();//荷载
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);//通过荷载获取token
    }

    /**
     * 从token中获取登录用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            //通过token获取荷载
            Claims claims=getClaimsFormToken(token);
            username=claims.getSubject();
        } catch (Exception e) {
            username=null;
            e.printStackTrace();
        }
        return username;
    }

    /**
     * 验证token是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails){
        String userName=getUserNameFromToken(token);
        return userName.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }


    /**
     * 判断token是否可以被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token); //token失效不可被刷新
    }

    /**
     * 刷新token令牌
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims=getClaimsFormToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }



    /**
     * 判断token是否失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
       Date expireDate= getExpiredDateFromToken(token);//获取失效时间
        //返回false则没过期，
       return expireDate.before(new Date());
    }




    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims=getClaimsFormToken(token);
        return claims.getExpiration();
    }


    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFormToken(String token) {
        Claims claims=null;
        try {
            claims= Jwts.parser()
                   .setSigningKey(secret)//秘钥
                   .parseClaimsJws(token)//token
                   .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据负载生成JWT TOKEN
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(GenerateExpirationDate())//秒，设置失效时间24小时
                .signWith(SignatureAlgorithm.HS512,secret)//设置hash算法，添加秘钥
                .compact();
    }

    /**
     * 生成token失效时间
     * @return
     */
    private Date GenerateExpirationDate() {
        //System.currentTimeMillis当前时间总秒数
        return new Date(System.currentTimeMillis()+expiration*1000);//转换时间为秒
    }

}
