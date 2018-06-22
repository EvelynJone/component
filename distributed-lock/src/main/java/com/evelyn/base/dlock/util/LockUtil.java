package com.evelyn.base.dlock.util;

import com.evelyn.base.dlock.config.SimpleConfig;
import com.evelyn.base.dlock.cons.Const;

/**
 * 功能说明：TODO
 *
 * @auther by zhaoxl
 * @return <br/>
 * 修改历史：<br/>
 * 1.[2018年06月21日上午11:54]
 */
public class LockUtil {
    public static String getLockName(String lockName) {
        String applicationName = SimpleConfig.springApplicationName;
        return Const.lockPrefix + applicationName + "_" + lockName;
    }
}
