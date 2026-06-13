package cn.yanque.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class ThreadPoolConfig {

    private final static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    public static ScheduledExecutorService getScheduledPool() {
        return scheduledExecutorService;
    }
}
