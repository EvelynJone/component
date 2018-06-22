package com.evelyn.base.dlock.zkimpl;

import com.evelyn.base.dlock.config.SimpleConfig;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午13:22]
 */
public class ZkClient {

    private static final Logger LOG = LoggerFactory.getLogger(ZkClient.class);

    private static volatile CuratorFramework client;

    private static int sessionTimeout = 10_000;
    private static int connectionTimeout = 15_000;

    // 等待连接zookeeper的时间
    private static int maxWaitConnect = 10;
    // 操作失败，重试次数
    private static int tryTimes = 3;
    // 重试间隔
    private static int tryInterval = 500;

    public static boolean isClientInited() {
        return client != null;
    }

    public static CuratorFramework getZkClient() {
        if (client == null || client.getState() == CuratorFrameworkState.STOPPED) {
            synchronized (ZkClient.class) {
                if (client == null || client.getState() == CuratorFrameworkState.STOPPED) {
                    final String zkServer = SimpleConfig.zookeeperClusterServerUrl;

                    RetryPolicy retryPolicy = new RetryNTimes(tryTimes, tryInterval);

                    client = CuratorFrameworkFactory.builder()
                            .connectString(zkServer)
                            .retryPolicy(retryPolicy)
                            .sessionTimeoutMs(sessionTimeout)
                            .connectionTimeoutMs(connectionTimeout)
                            .build();

                    client.start();

                    try {
                        client.blockUntilConnected(maxWaitConnect, TimeUnit.SECONDS);

                        client.getConnectionStateListenable().addListener(
                                (CuratorFramework client, ConnectionState state) -> {
                                    if (ConnectionState.LOST == state ||
                                            ConnectionState.RECONNECTED == state ||
                                            ConnectionState.SUSPENDED == state) {
                                        LOG.error("zookeeper connection occur an exception,the distribute lock service may be expire!." +
                                                "connection state {} ,server url : {}", state, zkServer);
                                    }
                                });
                    } catch (InterruptedException e) {
                        LOG.error("connect to zookeeper has an error.",e);
                        client.close();
                        client = null;
                    }
                }
            }
        }

        if (client == null) {
            throw new IllegalStateException("can not connect to zookeeper,can not to support distribute lock service");
        }

        return client;
    }
}
