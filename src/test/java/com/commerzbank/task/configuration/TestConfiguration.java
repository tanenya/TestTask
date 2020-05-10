package com.commerzbank.task.configuration;

import com.commerzbank.task.TaskApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Configuration
@Import(TaskApplication.class)
public class TestConfiguration {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Bean
    public MockMvc mockMvc() {
        return MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
}
