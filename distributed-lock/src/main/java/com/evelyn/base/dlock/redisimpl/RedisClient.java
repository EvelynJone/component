package com.evelyn.base.dlock.redisimpl;

import com.evelyn.base.dlock.config.SimpleConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午11:53]
 */
public class RedisClient {

    private static volatile RedissonClient redissonClient = null;

    public static boolean isClientInited() {
        return redissonClient != null;
    }

    public static RedissonClient getRedisClient() {
        if (redissonClient == null || redissonClient.isShutdown() || redissonClient.isShuttingDown()) {
            synchronized (RedisClient.class) {
                if (redissonClient == null || redissonClient.isShuttingDown() || redissonClient.isShuttingDown()) {
                    Config config = new Config();

                    SingleServerConfig singleServerConfig = config.useSingleServer();

                    singleServerConfig.setAddress(SimpleConfig.redisSingleServerUrl);

                    String password = SimpleConfig.redisSingleServerPassword;
                    if (password !=null ) {
                        singleServerConfig.setPassword(password);
                    }

                    redissonClient = Redisson.create(config);
                }
            }
        }

        return redissonClient;
    }
}
