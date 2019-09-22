package com.vboss.okline.tsm;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/7 <br/>
 * Summary : 工作线程调度
 */
public class WorkerThreadScheduler {
    static final int POOL_SIZE = 3;
    static final int MAX_POOL_SIZE = 4;
    static final int TIMEOUT = 30;
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

    /**
     * See {@link ThreadPoolExecutor#shutdown()}
     */
    public void shutdown() {
        mThreadPoolExecutor.shutdown();
    }

    /**
     * See {@link ThreadPoolExecutor#shutdownNow()}
     */
    public void shutdownNow() {
        mThreadPoolExecutor.shutdownNow();
    }
}
