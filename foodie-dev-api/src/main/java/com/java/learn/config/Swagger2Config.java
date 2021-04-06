package com.java.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    // 访问地址: http://localhost:8088/swagger-ui.html
    // bootstrap版本有bug，post请求若url带有参数直接被丢弃
    // 访问地址: http://localhost:8088/doc.html

    // swagger2核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2) // 指定API类型为Swagger2
                .apiInfo(apiInfo()) // 用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.java.learn.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        // 这里很明显是个建造者模式
        // 精髓就在于每个部件方法构造完部件（成员变量）后都返回实例本体，构成链式调用
        // 最后调用build方法，真正实例化并返回这个对象
        return new ApiInfoBuilder()
                .title("Spring-Boot 单体电商平台api")
                .contact(new Contact("Lumos",
                        "http://localhost:8088",
                        "test@wxsc.com"))
                .description("API接口文档")
                .version("1.0.0")
                .termsOfServiceUrl("http://127.0.0.1:8088")
                .build();
    }
}
