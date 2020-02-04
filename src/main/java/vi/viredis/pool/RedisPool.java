package vi.viredis.pool;

/*
 *  This is the main class where client can interact to create the
 *  pool of redis connections.
 */
public class RedisPool extends ObjectPool {
    /**
     * creates the redis connection pool by accepting the parameter
     * pool size, redis server host name and port number.
     * @param  size
     *         pool size
     *
     * @param  host
     *         redis server host name
     *
     * @param  port
     *         redis server port number
     *
     * @throws  RedisException
     *          if there are issues connecting to the redis server.
     */
    public RedisPool(int size, String host, int port) throws RedisException {
        super(size, host, port);
    }

}