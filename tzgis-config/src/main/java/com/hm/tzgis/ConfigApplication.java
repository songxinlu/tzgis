package com.hm.tzgis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author shike
 * @date 2022/4/1 10:55
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConfigApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ConfigApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
