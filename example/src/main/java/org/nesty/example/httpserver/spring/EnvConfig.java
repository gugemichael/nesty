package org.nesty.example.httpserver.spring;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by adol on 17-2-24.
 */
@Configuration
//@ImportResource({"classpath:example.xml"})
@ComponentScan({"org.nesty.core.server.springplus", "org.nesty.example.httpserver.spring"})
public class EnvConfig {

    @Bean
    public ExampleBean getExampleBean() {
        ExampleBean bean = new ExampleBean();
        bean.msg = "programmer spring";
        return bean;
    }
}
