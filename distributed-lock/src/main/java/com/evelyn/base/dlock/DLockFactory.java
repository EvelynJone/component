package com.evelyn.base.dlock;

import com.evelyn.base.dlock.cons.LockNature;
import com.evelyn.base.dlock.exception.LockTypeNotSupportException;
import com.evelyn.base.dlock.redisimpl.RedisClient;
import com.evelyn.base.dlock.redisimpl.RedisReentrantLock;
import com.evelyn.base.dlock.zkimpl.ZkClient;
import com.evelyn.base.dlock.zkimpl.ZkReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午21:06]
 */
public class DLockFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DLockFactory.class);

    public static DLock getReentrantLock(String lockName, LockNature lockNature) {
        if (lockNature == LockNature.HIGH_PERFORMANCE) {
            return new RedisReentrantLock(lockName);
        }

        if (lockNature == LockNature.HIGH_SAFETY) {
            return new ZkReentrantLock(lockName);
        }

        throw new LockTypeNotSupportException();
    }

    public static void closeClients() {
        try {
            if (ZkClient.isClientInited()) {
                ZkClient.getZkClient().close();
            }
        }catch (Exception e) {
            LOG.warn("close zk client exception",e);
        }

        try {
            if (RedisClient.isClientInited()) {
                RedisClient.getRedisClient().shutdown();
            }
        }catch (Exception e) {
            LOG.warn("close redis client exception.",e);
        }
    }

}
