package org.nesty.core.server;

import java.util.concurrent.TimeUnit;

public class NestyOptions<T> {

    // default value for every option
    private final T defaultValue;

    public NestyOptions(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public T defaultValue() {
        return defaultValue;
    }

    /**
     * max tcp accept compulish queue.
     *
     * NOT effect on max connections restriction. just a hint value for linux listen(fd, backlog)
     * increase the number if you have lots of short connection which sockets will be filled
     * in tcp accept compulish queue too much.
     *
     */
    public static final NestyOptions<Integer> TCP_BACKLOG = new NestyOptions<>(1024);

    /**
     * max connections in this httpserver instance. default is 8192
     *
     * incoming http connection over the number of maxConnections will be reject
     * and return http code 503
     */
    public static final NestyOptions<Integer> MAX_CONNETIONS = new NestyOptions<>(8192);

    /**
     * max received packet size. default is 16MB
     */
    public static final NestyOptions<Integer> MAX_PACKET_SIZE = new NestyOptions<>(16 * 1024 * 1024);

    /**
     * network socket io threads number. default is cpu core - 1
     *
     * NOT set the number more than cpu core number
     */
    public static final NestyOptions<Integer> IO_THREADS = new NestyOptions<>(Runtime.getRuntime().availableProcessors() - 1);

    /**
     * logic handler threads number. default is 128
     *
     * we suggest adjust the ioThreads number bigger if there are more. critical region
     * code or block code in your handler logic (io intensive). and smaller if your code
     * has no block almost (cpu intensive)
     */
    public static final NestyOptions<Integer> WORKER_THREADS = new NestyOptions<>(128);

    /**
     * logic handler timeout. default is 30s
     */
    public static final NestyOptions<Integer> TCP_TIMEOUT = new NestyOptions<>((int) TimeUnit.SECONDS.toMillis(30));


    public static final NestyOptions<Boolean> TCP_NODELAY = new NestyOptions<>(Boolean.TRUE);

    public static final NestyOptions<String> ACCESS_LOG = new NestyOptions<>("");

    public static final NestyOptions<Boolean> ACCESS_LOG_BUFFER_IO = new NestyOptions<>(false);

}
