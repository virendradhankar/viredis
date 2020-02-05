package vi.viredis.pool;

import vi.viredis.client.ViRedis;
import vi.viredis.exceptions.RedisException;

/*
 *  common pool methods
 */
public interface Pool {

    /*
     * @return one of the pooled redis object.
     */
    ViRedis getConnection() throws RedisException;

    /*
     * @param viRedis to be return back to pool
     */
    void returnConnection(ViRedis viRedis) throws RedisException;

    /**
     * Shuts down the pool. Should release all resources.
     */
    void shutdown();
}
