package com.hm.tzgis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author shike
 * @date 2022/4/1 10:55
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SearchApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SearchApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
