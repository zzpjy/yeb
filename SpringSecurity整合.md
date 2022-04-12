# SpringSecurity整合

### SpringSecurity

@EnableWebSecurity启动

```xml
<!--SpringSecurity 依赖-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```

##### PasswordEncoder

实现类BCryptPasswordEncoder();

encode（）：hash加密

matches(“ ”，encode):判断原密码与加密



##### 自定义登录页面

继承WebSecurityConfigurerAdapter

实现configure(HttpSecurity http)方法

![1642125143018](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642125143018.png)

跳转外部页面重写AuthenticationSuccessHandler接口.successHandler(new AuthenticationSuccessHandler)

登入失败重写AuthenticationFailureHandler接口.failureHandler(new AuthenticationFailureHandler)



##### 权限判断

底层都是通过access方法的相应表达式

http.authorizeRequests().

![](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642129487655.png)匹配相应权限

ROLE_abc设置相应的角色abc

![1642130809678](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642130809678.png)

赋予角色权限

通过IP地址判断

![1642131377531](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642131377531.png)



##### 异常拦截

权限不足

```Java
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {   
    @Override   
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException { 
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        RespBean respBean = RespBean.error("权限不足，请联系管理员");
        respBean.setCode(403);
        writer.write(new ObjectMapper().writeValueAsString(respBean));
        writer.flush();
        writer.close();
    }}
```

没有登录

```java
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {   
    @Override   
    public void commence(HttpServletRequest request, HttpServletResponse response,                        AuthenticationException authException) throws IOException, ServletException {     
        response.setCharacterEncoding("UTF-8");  
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter(); 
        RespBean respBean = RespBean.error("尚未登录，请登录");
        respBean.setCode(401);
        writer.write(new ObjectMapper().writeValueAsString(respBean)); 
        writer.flush();   
        writer.close();  
    }}
```



//自定义异常返回结果，异常拦截器

http.exceptionHandling()      .accessDeniedHandler(myAccessDeniedHandler)      .authenticationEntryPoint(myAuthenticationEntryPoint);



##### RememberMe 记住我功能

```java 
//记住我
http.rememberMe()
    //设置失效时间
    .tokenValiditySeconds(60)
    //修改表单默认名称
   	//rememberMeParameter()
	//自定义登入逻辑
	.userDetailsService(userDetailsService)
	//持久层对象
	.tokenRepository(getPersistentTokenRepository)
    
 
```

![1642151058465](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642151058465.png)

复选框name必须是remember-me

![1642152652816](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642152652816.png)

##### 退出

![1642206441605](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642206441605.png)

##### csrf

传递一个_csrf.token值判断



##### 获取user对象

User user=(User) authentication.getPrincipal()

##### 获取本机IP地址：ipconfig

### Oauth2认证协议

![1642217377780](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642217377780.png)

![1642217308397](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642217308397.png)

![1642217507116](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642217507116.png)

![1642208639550](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642208639550.png)

![1642209285275](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642209285275.png)

![1642209413180](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642209413180.png)

![1642209488107](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642209488107.png)

![1642209640829](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642209640829.png)

##### 相关依赖

```xml
 <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring-cloud.version>Greenwich.SR2</spring-cloud.version>
    </properties>

<dependency>        <groupId>org.springframework.cloud</groupId>        <artifactId>spring-cloud-starter-oauth2</artifactId>    </dependency>     
<dependency>        <groupId>org.springframework.cloud</groupId>        <artifactId>spring-cloud-starter-security</artifactId> </dependency>
<dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

##### 授权服务器配置

@Configuration

@EnableAuthorizationServer

继承AuthorizationServerConfigurerAdapter![1642214319409](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642214319409.png)

##### 资源服务器配置

@Configuration

@EnableResourceServer

继承ResourceServerConfigurerAdapter

![1642214554555](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642214554555.png)

##### 测试

![1642215481064](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642215481064.png)

获取token令牌http://localhost:8080/oauth/token

![1642215809860](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642215809860.png)



![](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642215913614.png)

![1642238443887](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642238443887.png)

密码模式

![1642216421040](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642216421040.png)

将token存入redis

```java
@Autowired
//连接工厂
private RedisConnectionFactory redisConnectionFactory;

@Bean
public TokenStore redisTokenStore(){
    //自动将token存入redis
    return new RedisTokenStore(redisConnectionFactory);
}
```

### JWT认证机制

##### Jwt组成

头部：![1642228543143](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642228543143.png)

负载：

![1642228623302](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642228623302.png)

![1642230433321](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642230433321.png)

签证:

![1642230485589](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642230485589.png)

##### jjwt所导依赖

```xml
<dependency>    
    <groupId>io.jsonwebtoken</groupId>  
    <artifactId>jjwt</artifactId>    <version>0.9.0</version></dependency>
```

##### 生成token

![1642231289691](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642231289691.png)

##### 获取token

![1642233696210](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642233696210.png)

##### SpringSecurityOauth2整合JWT

JwtToken配置类

![1642234981927](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642234981927.png)

![1642243525373](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642243525373.png)

Jwt内容增强器

实现TokenEnhancer

![1642244071686](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642244071686.png)

增加相应配置

```java
@Bean
public JwtTokenEnhancer jwtTokenEnhancer(){
    return new JwtTokenEnhancer();
}
```

![1642244300624](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642244300624.png)

上图加.tokenEnhancer(enhancerChain)

获取用户信息

![1642246555572](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642246555572.png)

##### SpringSecurityOauth2整合SSO

![1642247291636](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642247291636.png)

开启单点登录功能在启动类上加上@EnableOauthSso注解

修改授权服务器

![1642247699351](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642247699351.png)

![1642247820777](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642247820777.png)

