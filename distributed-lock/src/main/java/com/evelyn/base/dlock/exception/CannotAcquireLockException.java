package com.evelyn.base.dlock.exception;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午11:37]
 */
public class CannotAcquireLockException extends RuntimeException {

    public CannotAcquireLockException(Exception e) {
        super(e);
    }
}
