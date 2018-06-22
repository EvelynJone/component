package com.evelyn.base.dlock;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午11:43]
 */
public interface BusinessProcess<T> {

    T doProcess() throws Exception;
}
