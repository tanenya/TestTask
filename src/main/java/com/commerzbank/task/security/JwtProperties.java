package com.commerzbank.task.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Test task Commerzbank
 *
 * Configures Jwt token properties
 *
 * @author vtanenya
 * */

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String secretKey = "secret";

    //validity in milliseconds
    private long validityInMs = 3600000; // 1 hour
}

