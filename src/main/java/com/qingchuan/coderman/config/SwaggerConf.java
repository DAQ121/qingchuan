package com.qingchuan.coderman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;

/**
 * @program: coderman
 * @description: Swagger配置
 * @author: Daq
 * @create: 2020-10-01 15:57
 **/

@Configuration
@EnableSwagger2// 开启Swagger2的自动配置
@SuppressWarnings("all")
public class SwaggerConf {

    //第1步：通过apiInfo()属性配置文档信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("代澳旗", "https://daqwt.top/联系人访问链接", "daq2829025551@163.com");
        return new ApiInfo(
                "网站接口", // 标题
                "admin是后台的接口，web是前台的接口", // 描述
                "v1.0", // 版本
                "https://daqwt.top/组织链接", // 组织链接
                contact, // 联系人信息
                "Apach 2.0 许可", // 许可
                "许可链接", // 许可连接
                new ArrayList<>()// 扩展
        );
    }

    //第2步：Swagger实例Bean是Docket，所以通过配置Docket实例来配置Swaggger。
    @Bean //配置docket以配置Swagger具体参数
    public Docket docket(Environment environment) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin") //分组的名称，如过有多个分组，就设置多个docket方法即可。这里的名字改一下就可以
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qingchuan.coderman.admin.controller"))//根据这个包生成接口文档
                .build();
    }

    //可以配置多个 docket
    @Bean
    public Docket docket1(Environment environment) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webSite") //分组的名称，如过有多个分组，就设置多个docket方法即可。这里的名字改一下就可以
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qingchuan.coderman.web.controller"))//根据这个包生成接口文档
                .build();
    }

}