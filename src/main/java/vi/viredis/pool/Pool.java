package vi.viredis.pool;

import vi.viredis.client.ViRedis;

public interface Pool<T> {

    /*
     * @return one of the pooled objects.
     */
    ViRedis get();

    /*
     * @param object T to be return back to pool
     */
    void release(T object);

    /**
     * Shuts down the pool. Should release all resources.
     */
    void shutdown();
}