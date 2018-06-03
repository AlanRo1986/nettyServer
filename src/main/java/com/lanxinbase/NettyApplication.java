package com.lanxinbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan("com.lanxinbase.*")
@EnableAsync
@EnableScheduling
@EnableWebMvc
@EnableAspectJAutoProxy
public class NettyApplication {

	/**
	 * The tcp server port.
	 */
	public static int port = 9000;

	public static void main(String[] args) {
		if (args.length > 0){
			port = Integer.valueOf(args[0]);
		}

		SpringApplication.run(NettyApplication.class, args);
	}
}
