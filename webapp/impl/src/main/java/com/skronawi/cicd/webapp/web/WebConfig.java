package com.skronawi.cicd.webapp.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan(basePackages = "com.skronawi.cicd.webapp.web")
public class WebConfig {
}
