package com.hm.tzgis.entity;

import lombok.Data;

/**
 * @author shike
 * @date 2021/12/2 11:21
 */
@Data
public class MongoProperties {
    private String database;
    private String authentication;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Integer minConnectionsPerHost;
    private Integer connectionsPerHost;
    private Integer connectionTimeoutMs;
    private Integer readTimeoutMs;
    private Integer maxWaitTimeMs;
    private Integer maxConnectionIdleTime;
    private Integer maxConnectionLifeTime;
}