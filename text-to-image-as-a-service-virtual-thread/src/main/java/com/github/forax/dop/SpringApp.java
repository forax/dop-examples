package com.github.forax.dop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

@SpringBootApplication
public class SpringApp {
	@Bean
	public AsyncTaskExecutor applicationTaskExecutor() {
		// executor used for async calls
		var executor = Executors.newVirtualThreadPerTaskExecutor();
		return new TaskExecutorAdapter(executor);
	}

	@Bean
	public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
		// executor used for sync calls
		return handler -> handler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringApp.class, args);
	}
}