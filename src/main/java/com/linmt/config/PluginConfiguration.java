package com.linmt.config;

import com.linmt.delayqueue.handler.DelayQueueHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.config.EnablePluginRegistries;


@Configuration
@EnablePluginRegistries(value = {DelayQueueHandler.class})
public class PluginConfiguration {
}
