package vi.viredis.pool;

import vi.viredis.client.ViRedis;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public abstract class ObjectPool implements Pool{
    private int size;
    private boolean shutdown;
    private String host;
    private int port;
    private BlockingQueue<ViRedis> queue;

    public ObjectPool(int size, String host, int port) throws RedisException {
        this.size = size;
        this.host = host;
        this.port = port;
        shutdown = false;
        init();
    }

    /*
     * initiate the pool with fix size
     */
    private void init() throws RedisException {
        queue = new LinkedTransferQueue();
        for (int i = 0; i < size; i++) {
            ViRedis viRedis = createRedisConnection(host, port);
            if(viRedis != null) queue.add(viRedis);
        }
    }

    private ViRedis createRedisConnection(String host, int port) throws RedisException {
        try {
            return  new ViRedis(host, port);
        } catch (IOException e) {
            throw new RedisException("unable to connect to the redis server");
        }
    }

    @Override
    public ViRedis get() throws RedisException {
        if (!shutdown) {
            ViRedis redisObject;
            try {
                redisObject = queue.take();
            }
            catch (Exception e) {
                throw new RedisException("unable to retrieve the connection object");
            }
            return redisObject;
        }
        throw new IllegalStateException("pool is already shutdown.");
    }

    @Override
    public void returnConnection(ViRedis viRedis) throws RedisException {
        try {
            queue.offer(viRedis);
        } catch (Exception e) {
            throw new RedisException("unable to return connection to the pool");
        }
    }

    @Override
    public void shutdown() {
        queue.clear();
    }

    public int size() {
        return queue.size();
    }

}