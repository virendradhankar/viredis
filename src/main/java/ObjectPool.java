import vi.viredis.client.ViRedis;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public abstract class ObjectPool<T> implements Pool<T> {

    private int size;

    private boolean shutdown;

    private String host;
    private int port;

    private BlockingQueue objects;

    public ObjectPool(int size, String host, int port) {
        this.size = size;
        this.host = host;
        this.port = port;
        shutdown = false;
        init();
    }

    /*
     * initiate the pool with fix size
     */
    private void init() {
        objects = new LinkedTransferQueue();
        for (int i = 0; i < size; i++) {
            objects.add(createRedisConnection(host, port));
        }
    }

    private T createRedisConnection(String host, int port) {
        try {
            return (T) new ViRedis(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ViRedis get() {
        if (!shutdown) {
            ViRedis redisObject = null;

            try {
                redisObject = (ViRedis)objects.take();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return redisObject;
        }

        throw new IllegalStateException("Object pool is already shutdown.");
    }

    @Override
    public void release(T t) {
        try {
            objects.offer(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        objects.clear();
    }

    public int size() {
        return objects.size();
    }

}