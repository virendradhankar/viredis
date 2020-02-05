package vi.viredis.client;
import org.junit.Test;
import vi.viredis.pool.RedisException;
import vi.viredis.pool.RedisPool;
import static org.junit.Assert.assertEquals;

public class RedisPoolTest {
    private RedisPool redisPool;
    private String host = "127.0.0.1";
    private int port = 6379;

    @Test
    public void testCreateRedisConnectionPool() throws RedisException {
        redisPool = new RedisPool(10, host, port);
        assertEquals(redisPool.size(), 10);
    }

    @Test
    public void testAfterGettingConnectionPoolSizeShouldDecrease() throws RedisException {
        redisPool = new RedisPool(11, host, port);
        ViRedis viRedis = redisPool.get();
        assertEquals(redisPool.size(), 10);
    }

    @Test
    public void testAfterReleasingConnectionPoolSizeShouldIncrease() throws RedisException {
        redisPool = new RedisPool(11, host, port);
        ViRedis viRedis = redisPool.get();
        redisPool.returnConnection(viRedis);
        assertEquals(redisPool.size(), 11);
    }

    @Test(expected = RedisException.class)
    public void testConnectionShouldFailWhenPortIsInvalid() throws RedisException {
        redisPool = new RedisPool(10, host, 6380);
    }

    @Test
    public void testCallingShutdownShouldReleaseAllConnections() throws RedisException {
        redisPool = new RedisPool(10, host, port);
        redisPool.shutdown();
        assertEquals(redisPool.size(), 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testShouldThrowExceptionWhenPoolIsShutdownAndTriesToAccess() throws RedisException {
        redisPool = new RedisPool(10, host, port);
        redisPool.isShutdown = true;
        redisPool.get();
    }

    @Test(expected = RedisException.class)
    public void testShouldNotBlockWhenClientAsksForMoreThanAvailableConnections() throws RedisException {
        redisPool = new RedisPool(10, host, port);
        for(int i = 0; i < 12; i++){
            System.out.println(redisPool.get());
        }
    }

}