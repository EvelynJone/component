package com.evelyn.base.dlock;

import com.evelyn.base.dlock.exception.BusinessException;
import com.evelyn.base.dlock.exception.LockNotAcquiredException;

import java.util.concurrent.TimeUnit;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午11:44]
 */
public abstract class AbstractDLock implements DLock {


    @Override
    public <T> T doWithLockProtect(int waitTime, BusinessProcess<T> businessProcess)
            throws LockNotAcquiredException, BusinessException {

        boolean isAcquired;

        try {
            isAcquired = tryLock(waitTime, TimeUnit.SECONDS);

            if (!isAcquired) {
                throw new LockNotAcquiredException("wait" + waitTime + "after,can not acquire lock!");
            }
        } catch (Exception e) {
            throw new LockNotAcquiredException(e);
        }

        try {
            return businessProcess.doProcess();
        } catch (Exception e) {
            throw new BusinessException(e);
        }finally {
            unlock();
        }
    }
}
