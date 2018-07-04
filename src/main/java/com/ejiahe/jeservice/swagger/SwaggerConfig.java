package com.ejiahe.jeservice.swagger;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaofei on 2018/1/2.
 */
@Configuration    // 配置注解，自动在本类上下文加载一些环境变量信息
@EnableSwagger2   // 使swagger2生效
public class SwaggerConfig {
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("sessionId").description("user sessionId")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("business-api")
                .select()   // 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                        //.apis(RequestHandlerSelectors.any())  // 对所有api进行监控
                .paths(PathSelectors.any())   // 对所有路径进行监控
//                .paths(paths())//.paths(paths()) 只对指定路径监控
                .build()
                .globalOperationParameters(pars);
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger联调接口列表")
//                .termsOfServiceUrl("http://localhost:9097/swagger-ui.html")
                .description("此API提供接口测试")
//                .license("License Version 2.0")
//                .licenseUrl("http://blog.csdn.net/yangshijin1988")
                .contact("lf")
                .version("1.0").build();
    }

//    private Predicate<String> paths() {
//        return or(regex("/person.*"));
//    }
//
//    private List<ApiKey> securitySchemes() {
//        return newArrayList(
//                new ApiKey("clientId", "客户端ID", "header"),
//                new ApiKey("clientSecret", "客户端秘钥", "header"),
//                new ApiKey("accessToken", "客户端访问标识", "header"));
//    }
//
//    private List<SecurityContext> securityContexts() {
//        return newArrayList(
//                SecurityContext.builder()
//                        .securityReferences(defaultAuth())
//                        .forPaths(PathSelectors.regex("/*.*"))
//                        .build()
//        );
//    }
//
//    List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return newArrayList(
//                new SecurityReference("clientId", authorizationScopes),
//                new SecurityReference("clientSecret", authorizationScopes),
//                new SecurityReference("accessToken", authorizationScopes));
//    }

}
