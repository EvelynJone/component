package com.evelyn.base.dlock;

import com.evelyn.base.dlock.exception.BusinessException;
import com.evelyn.base.dlock.exception.CannotAcquireLockException;
import com.evelyn.base.dlock.exception.LockNotAcquiredException;

import java.util.concurrent.TimeUnit;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午11:36]
 */
public interface DLock {

    /**
     * 尝试获取锁，如果获取不到，会一直阻塞
     * @throws CannotAcquireLockException ：连接断开、interrupt等均会抛出CannotAcquireLockException
     */
    void lock() throws CannotAcquireLockException;


    /**
     * 尝试获取锁，若在waitTime时间内获取到了，返回true，否则在waitTime时间后返回false。
     * 方法也有可能在waitTime时间内返回，如果中间发生异常。
     * @param waitTime 等待时间
     * @param unit 时间单位
     * @return
     */
    boolean tryLock(int waitTime, TimeUnit unit);

    /**
     * 释放锁。如果是可重入锁，在重入次数减为0时释放锁。
     */
    void unlock();

    /**
     * 使用锁保护资源的便捷方法
     * @param waitTime 获取锁的最大等待时间（以s为单位）
     * @param businessProcess 实际的业务逻辑
     * @param <T>
     * @return
     * @throws LockNotAcquiredException
     * @throws BusinessException 业务逻辑抛出异常后，会被包装一层抛出此异常
     */
    <T> T doWithLockProtect(int waitTime,BusinessProcess<T> businessProcess) throws LockNotAcquiredException,BusinessException;
}
