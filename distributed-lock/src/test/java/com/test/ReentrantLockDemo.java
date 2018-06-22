package com.test;

import com.evelyn.base.dlock.DLock;
import com.evelyn.base.dlock.DLockFactory;
import com.evelyn.base.dlock.cons.LockNature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReentrantLockDemo {

    private static final Logger LOG = LoggerFactory.getLogger(ReentrantLockDemo.class);

    public static void main(String[] args) {
        final Count ct = new Count();
        for (int i = 0; i < 2; i++) {
            Thread tread = new Thread(()->{
                ct.get();
            });
            tread.setName("Thread_Get_" + i);
            tread.start();
        }

        for (int i = 0; i < 2; i++) {
            Thread tread = new Thread(()->{
                ct.put();
            });
            tread.setName("Thread_Put_" + i);
            tread.start();
        }
    }

}

class Count {
    private static final Logger LOG = LoggerFactory.getLogger(Count.class);

    DLock lock = DLockFactory.getReentrantLock("name", LockNature.HIGH_PERFORMANCE);
    // ReentrantLock lock = new ReentrantLock();
    public void get() {

        lock.lock();
        LOG.info(Thread.currentThread().getName() + " get begin");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info(Thread.currentThread().getName() + " get end");
        lock.unlock();
    }

    public void put() {
        // final ReentrantLock lock = new ReentrantLock();
        lock.lock();
        LOG.info(Thread.currentThread().getName() + " put begin");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info(Thread.currentThread().getName() + " put end");
        lock.unlock();
    }
}