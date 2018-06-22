package com.evelyn.base.dlock.zkimpl;

import com.evelyn.base.dlock.AbstractDLock;
import com.evelyn.base.dlock.exception.CannotAcquireLockException;
import com.evelyn.base.dlock.util.LockUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午13:20]
 */
public class ZkReentrantLock extends AbstractDLock{
    private static final Logger LOG = LoggerFactory.getLogger(ZkReentrantLock.class);

    private InterProcessMutex lock;
    private String lockName;

    public ZkReentrantLock(String lockName) {
        this.lockName = lockName;

        String lockNameInside = LockUtil.getLockName(lockName);
        CuratorFramework client = ZkClient.getZkClient();

        lock = new InterProcessMutex(client,"/_dlock_/"+lockNameInside);
    }

    @Override
    public void lock() throws CannotAcquireLockException {
        try {
            lock.acquire();
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
            boolean res = lock.acquire(waitTime,unit);

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
        } catch (Exception e) {
            LOG.warn("InterProcessMutex#acquire attempt to acquired lock which named is "+lockName+", occur an error.",e);
            return false;
        }
    }

    @Override
    public void unlock() {
        try {
            lock.release();
            if (LOG.isInfoEnabled()) {
                LOG.info("In the lock {} execute unlock method success," +
                                " is held by the current thread: {}",
                        lockName,lock.isOwnedByCurrentThread());
            }
        }catch (IllegalMonitorStateException e) {
            LOG.error("InterProcessMutex#release tries to unlock someone else's lock！Holding lock" + lockName + "may have expired！", e);
        } catch (Exception e) {
            LOG.warn("InterProcessMutex#release unlock " + lockName + "occur an error！", e);
        }
    }
}
