package com.vcloudapi;

import com.vcloudapi.global.config.properties.AppProperties;
import com.vcloudapi.global.config.properties.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@EnableConfigurationProperties({
        CorsProperties.class,
        AppProperties.class
})
@SpringBootApplication
public class VcloudapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VcloudapiApplication.class, args);
	}

}
