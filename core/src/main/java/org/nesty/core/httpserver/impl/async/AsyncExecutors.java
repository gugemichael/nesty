package org.nesty.core.httpserver.impl.async;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncExecutors {

    /**
     * max async executor's queue length
     *
     */
    private static final int MAX_ASYNC_QUEUE_SIZE = 50000;

    private static final BlockingQueue<Runnable> innerQueue = new ArrayBlockingQueue<>(MAX_ASYNC_QUEUE_SIZE);

    /**
     * default thread factory
     *
     */
    public static final ThreadFactory factory = new ThreadFactory() {
        private final AtomicInteger poolNumber = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            // give new name
            Thread newThread = new Thread(r, String.format("nesty-executor-pool-%d", poolNumber.getAndIncrement()));
            if (newThread.isDaemon())
                newThread.setDaemon(false);
            if (newThread.getPriority() != Thread.NORM_PRIORITY)
                newThread.setPriority(Thread.NORM_PRIORITY);
            return newThread;
        }
    };

    public static ExecutorService newExecutors(int workers) {

        return new ThreadPoolExecutor(Math.max(4, workers / 4), Math.max(4, workers),           // from total/4 to total
                                                    10, TimeUnit.SECONDS,                                              // 10s to recycle idle thread
                                                    innerQueue,                                                              // bounded queue
                                                    factory,                                                                    // named factory
                                                    new ThreadPoolExecutor.CallerRunsPolicy()                    // caller run
        );
    }
}
