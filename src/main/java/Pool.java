import vi.viredis.client.ViRedis;

public interface Pool {

    /*
     * @return one of the pooled objects.
     */
    ViRedis get();

    /*
     * @param object T to be return back to pool
     */
    void release(ViRedis object);

    /**
     * Shuts down the pool. Should release all resources.
     */
    void shutdown();
}