package com.evelyn.base.dlock.config;

import com.evelyn.base.dlock.util.FlattenYamlUtil;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午13:02]
 */
public class SimpleConfig {

    private static volatile boolean config = false;

    public static String redisSingleServerUrl = null;
    public static String redisSingleServerPassword = null;
    public static String springApplicationName = null;
    public static String zookeeperClusterServerUrl = null;

    static {
        if (!config) {
            redisSingleServerUrl = FlattenYamlUtil.getProperty("redis-single.server.url");
            redisSingleServerPassword = FlattenYamlUtil.getProperty("redis-single.server.passoword");
            springApplicationName = FlattenYamlUtil.getProperty("spring.application.name");
            zookeeperClusterServerUrl = FlattenYamlUtil.getProperty("zookeeper-cluster.server.url");
        }
    }
}
