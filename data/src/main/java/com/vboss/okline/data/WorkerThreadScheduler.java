package com.vboss.okline.data;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/7 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class WorkerThreadScheduler {
    public static final int POOL_SIZE = 3;

    public static final int MAX_POOL_SIZE = 4;

    public static final int TIMEOUT = 30;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private static WorkerThreadScheduler instance;

    public static WorkerThreadScheduler getInstance() {
        if (instance == null) {
            instance = new WorkerThreadScheduler();
        }
        return instance;
    }

    private WorkerThreadScheduler() {
        mThreadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE,
                TIMEOUT, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE));

    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }
}
