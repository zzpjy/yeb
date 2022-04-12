package com.xxxx.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置类
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi(){
        //规定扫描哪些包下面去生成文档
        return new Docket(DocumentationType.SWAGGER_2)//文档类型
                .apiInfo( apiInfo())
                //扫描包
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xxxx.server.controller"))
                //配置路径，任何路径都可以
                .paths(PathSelectors.any())
                .build()
                //设置哪些路径需要带请求头
                .securityContexts(securityContexts())
                //每次登入会返回一个token，将token保存到请求头里面
                .securitySchemes(securitySchemes());
    }

    //文档相关信息
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("云E办接口文档")//标题
                .description("云E办接口文档")
                .contact(new Contact("xxxx","http:localhost:8081/doc.html",
                        "xxxx@xxxx.com"))
                .version("1.0")
                .build();
    }

    private List<ApiKey> securitySchemes(){
        //设置请求头信息
        List<ApiKey> result=new ArrayList<>();
        ApiKey apiKey=new ApiKey("Authorization","Authorization"
        ,"Header");
        result.add(apiKey);
        return result;
    }

    private List<SecurityContext> securityContexts(){
        //设置需要登入认证的路径
        List<SecurityContext> result=new ArrayList<>();
        result.add(getContextByPath("/hello/.*"));
        return result;
    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result=new ArrayList<>();
        //授权范围，所有
        AuthorizationScope authorizationScope=new AuthorizationScope("global",
                "accessEverything");
        AuthorizationScope[] authorizationScopes=new AuthorizationScope[1];
        authorizationScopes[0]=authorizationScope;
        result.add(new SecurityReference("Authorization",authorizationScopes));
        return result;
    }

}
