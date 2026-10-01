package com.codevictims.bayt.maintenance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MaintenanceAiConfig {
  @Bean
  RestClient.Builder maintenanceAiRestClientBuilder() {
    return RestClient.builder();
  }
}