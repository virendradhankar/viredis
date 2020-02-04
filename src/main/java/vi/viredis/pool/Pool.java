package vi.viredis.pool;

import vi.viredis.client.ViRedis;

/*
 *  common pool methods
 */
public interface Pool {

    /*
     * @return one of the pooled redis object.
     */
    ViRedis get() throws RedisException;

    /*
     * @param viRedis to be return back to pool
     */
    void returnConnection(ViRedis viRedis) throws RedisException;

    /**
     * Shuts down the pool. Should release all resources.
     */
    void shutdown();
}