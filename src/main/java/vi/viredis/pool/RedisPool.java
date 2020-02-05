package vi.viredis.pool;

import vi.viredis.client.ViRedis;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

/*
 *  implements the method of common pool interface
 */
public class RedisPool implements Pool {
    public boolean isShutdown;
    private int size;
    private String host;
    private int port;
    private BlockingQueue<ViRedis> queue;

    /*
     *  initializes the pool with given parameters
     *
     *  @param size
     *         size of the pool
     *  @param host
     *         redis server host
     *  @param port
     *         redis server port
     *  @throws RedisException
     *          if there is connectivity issue with the redis server
     */
    public RedisPool(int size, String host, int port) throws RedisException {
        this.size = size;
        this.host = host;
        this.port = port;
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

    /*
     * created the redis connection
     * @param host
     *        redis server host name
     * @param port
     *        redis server port
     * @throws RedisException
     */
    private ViRedis createRedisConnection(String host, int port) throws RedisException {
        try {
            return  new ViRedis(host, port);
        } catch (IOException e) {
            throw new RedisException("unable to connect to the redis server");
        }
    }

    /*
     * returns the pooled redis connection object
     * @throws RedisException
     */
    @Override
    public ViRedis get() throws RedisException {

        if(queue.size() == 0)
            throw new RedisException("pool capacity is reached not able to get new connection");
             if (!isShutdown) {
                 ViRedis redisObject;
                    try {
                       redisObject = queue.take();
                }
                catch (Exception e) {
                    throw new RedisException("unable to retrieve the connection object.");
                }
                return redisObject;
            }

        throw new IllegalStateException("pool is already shutdown.");
    }

    /*
     * it invalidates the used connection and closes the connection with the
     * redis server and creates a new connection with the same host and port number as
     * the previous connection and adds to the pool.
     *
     * @throws RedisException
     */
    @Override
    public void returnConnection(ViRedis viRedis) throws RedisException {
        try {
            ViRedis newConnection = invalidateoldConnectionAndCreateNew(viRedis);
            queue.offer(newConnection);
        } catch (Exception e) {
            throw new RedisException("unable to return connection to the pool.");
        }
    }

    private ViRedis invalidateoldConnectionAndCreateNew(ViRedis viRedis) throws IOException {
        ViRedis newConnection = new ViRedis(viRedis.getHost(), viRedis.getPort());
        viRedis.closeConnection();
        viRedis = null; // throw away
        return newConnection;
    }

    /*
     * clears the pool
     */

    @Override
    public void shutdown() {
        queue.clear();
    }
    /*
     * returns the size of the pool
     */
    public int size() {
        return queue.size();
    }

}