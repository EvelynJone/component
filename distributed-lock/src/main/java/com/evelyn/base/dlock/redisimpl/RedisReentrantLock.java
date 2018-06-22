package com.evelyn.base.dlock.redisimpl;

import com.evelyn.base.dlock.AbstractDLock;
import com.evelyn.base.dlock.exception.CannotAcquireLockException;
import com.evelyn.base.dlock.util.LockUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午11:52]
 */
public class RedisReentrantLock extends AbstractDLock {

    private static final Logger LOG = LoggerFactory.getLogger(RedisReentrantLock.class);

    private RLock rLock;
    private String lockName;

    public RedisReentrantLock(String lockName) {
        this.lockName = lockName;
        RedissonClient redissonClient = RedisClient.getRedisClient();

        rLock = redissonClient.getLock(LockUtil.getLockName(lockName));
    }

    @Override
    public void lock() throws CannotAcquireLockException {
        try {
            rLock.lock();
            if (LOG.isInfoEnabled()) {
                LOG.info("Lock which named is {} is acquired by the thread which named is {}"
                        ,lockName,Thread.currentThread().getName());
            }
        } catch (Exception e) {
            throw new CannotAcquireLockException(e);
        }
    }

    @Override
    public boolean tryLock(int waitTime, TimeUnit unit) {
        try {
            boolean res = rLock.tryLock(waitTime,unit);

            if (LOG.isInfoEnabled()) {
                if (res) {
                    LOG.info("Lock which named is {} is acquired by the thread which named is {}"
                            ,lockName,Thread.currentThread().getName());
                }else {
                    LOG.info("Failed to acquire lock which named is {} at specified time which is {}{}, exit wait",
                            lockName,waitTime,unit);
                }
            }
            return res;
        } catch (InterruptedException e) {
            LOG.warn("RLock#tryLock attempt to acquired lock which named is "+lockName+", occur an error.",e);
            return false;
        }
    }

    @Override
    public void unlock() {
        try {
            rLock.unlock();

            if (LOG.isInfoEnabled()) {
                LOG.info("In the lock {} execute unlock method success," +
                        " counter value is {}, is held by the current thread: {}, lock state:",
                        lockName,rLock.getHoldCount(),rLock.isHeldByCurrentThread(),rLock.isLocked());
            }
        }catch (IllegalMonitorStateException e) {
            LOG.error("RLock#unlock tries to unlock someone else's lock！Holding lock" + lockName + "may have expired！", e);
        } catch (Exception e) {
            LOG.warn("RLock#unlock unlock " + lockName + "occur an error！", e);
        }
    }
}
