package com.example.demo.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


@Configuration
public class ThreadPoolTaskSchedulerConfig {

  @Bean
  public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
    ThreadPoolTaskScheduler poolTaskScheduler = new ThreadPoolTaskScheduler();
    poolTaskScheduler.setPoolSize(5);
    poolTaskScheduler.setThreadNamePrefix("Auto_Retry_Scheduler");
    return poolTaskScheduler;
  }
}
