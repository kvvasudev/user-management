package com.mastek.poc;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author KaverV01
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.mastek.poc")
@EnableTransactionManagement
@EntityScan("com.mastek.poc.model")
@EnableJpaRepositories(basePackages = "com.mastek.poc.persistence")
@PropertySource("classpath:application.properties")
public class TestConfig {
    
}
