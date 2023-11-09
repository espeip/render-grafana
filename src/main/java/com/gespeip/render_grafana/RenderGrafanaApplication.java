package com.gespeip.render_grafana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RenderGrafanaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RenderGrafanaApplication.class, args);
	}

}
