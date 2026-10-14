package com.tennisscoreboard.util;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.tennisscoreboard")
@EnableWebMvc
public class WebConfig {
}
