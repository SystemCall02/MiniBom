package com.idme.minibom.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class WebMVCConfiguration extends WebMvcConfigurationSupport {

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket1() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("接口文档")
                .version("1.0")
                .description("接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("接口详情")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.idme.minibom.Controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

//    /**
//     * 扩展MVC框架消息转换器
//     * @param converters
//     */
//    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//
//        //创建消息转换器
//        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
//        messageConverter.setObjectMapper(new JacksonObjectMapper());
//        converters.add(0,messageConverter);
//    }
}
