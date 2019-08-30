package com.admin.core.util.time;

import com.admin.core.net.callback.IFailure;

public class TimeUtils {
    /**
     * 讲一个毫秒转换为 分钟的形式
     *
     * @param millisecond 毫秒
     * @return xx:xx
     */
    public static String getTime(long millisecond) {
        StringBuilder time = new StringBuilder();
        //将毫秒转换 为分钟
        long second = millisecond / 60000;
        long seconds = millisecond % 60000;
        //四舍五入
        long s = Math.round((float) seconds / 1000);
        if (second < 10) {
            time.append(0);
        }
        time.append(second).append(":");
        if (s < 10) {
            time.append(0);
        }
        time.append(s);
        return time.toString();
    }
}
