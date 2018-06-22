package com.evelyn.base.dlock.exception;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午11:42]
 */
public class LockNotAcquiredException extends RuntimeException {

    public LockNotAcquiredException(String message) {
        super(message);
    }

    public LockNotAcquiredException(Exception e) {
        super(e);
    }

}
