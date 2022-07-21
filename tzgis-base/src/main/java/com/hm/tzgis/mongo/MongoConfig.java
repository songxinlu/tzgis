package com.hm.tzgis.mongo;

import com.hm.tzgis.entity.MongoProperties;
import com.hm.tzgis.util.StringUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import javax.annotation.Resource;

@Configuration
public class MongoConfig {

    @Resource
    private MongoProperties mongoProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.data.mongodb.custom")
    MongoProperties mongoSettingsProperties() {
        return new MongoProperties();
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
        return mongoTemplate;
    }

    @Bean
    MongoDbFactory mongoDbFactory() {
        // 配置连接池
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(mongoProperties.getConnectionsPerHost());
        builder.minConnectionsPerHost(mongoProperties.getMinConnectionsPerHost());
        builder.connectTimeout(mongoProperties.getConnectionTimeoutMs());
        builder.socketTimeout(mongoProperties.getReadTimeoutMs());
        builder.maxWaitTime(mongoProperties.getMaxWaitTimeMs());
        builder.maxConnectionIdleTime(mongoProperties.getMaxConnectionIdleTime());
        builder.maxConnectionLifeTime(mongoProperties.getMaxConnectionLifeTime());
        MongoClientOptions mongoClientOptions = builder.build();
        // 配置MongoDB地址
        ServerAddress serverAddress = new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort());

        MongoClient mongoClient;

        if(mongoProperties.getUsername() != null && mongoProperties.getPassword() != null){
            // 配置连接认证
            MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(
                    mongoProperties.getUsername(),
                    StringUtils.isEmpty(mongoProperties.getAuthentication())?mongoProperties.getDatabase():mongoProperties.getAuthentication(),
                    mongoProperties.getPassword().toCharArray()
            );
            mongoClient = new MongoClient(serverAddress,mongoCredential, mongoClientOptions);
        }else{
            mongoClient = new MongoClient(serverAddress , mongoClientOptions);
        }
        return new SimpleMongoDbFactory(mongoClient, mongoProperties.getDatabase());
    }
}