import org.junit.Test;
import vi.viredis.client.ViRedis;
import vi.viredis.pool.RedisException;
import vi.viredis.pool.RedisPool;

public class RedisPoolTest {
    private RedisPool redisPool;
    private String host = "127.0.0.1";
    private int port = 6379;

    @Test
    public void testCreateRedisConnectionPool() throws RedisException {
        redisPool = new RedisPool(10, host, port);
        assert redisPool.size() == 10;
    }

    @Test
    public void testAfterGettingConnectionPoolSizeShouldDecrease() throws RedisException {
        redisPool = new RedisPool(11, host, port);
        ViRedis viRedis = redisPool.get();
        assert viRedis != null;
        assert redisPool.size() == 10;
    }

    @Test
    public void testAfterReleasingConnectionPoolSizeShouldIncrease() throws RedisException {
        redisPool = new RedisPool(11, host, port);
        ViRedis viRedis = redisPool.get();
        redisPool.returnConnection(viRedis);
        assert redisPool.size() == 11;
    }

    @Test(expected = RedisException.class)
    public void testConnectionShouldFailWhenPortIsInvalid() throws RedisException {
        redisPool = new RedisPool(10, host, 6380);
    }

    @Test
    public void testCallingShutdownShouldReleaseAllConnections() throws RedisException {
        redisPool = new RedisPool(10, host, port);
        redisPool.shutdown();
        assert redisPool.size() == 0;
    }

    @Test(expected = IllegalStateException.class)
    public void testShouldThrowExceptionWhenPoolIsShutdownAndTriesToAccess() throws RedisException {
        redisPool = new RedisPool(10, host, port);
        redisPool.isShutdown = true;
        redisPool.get();
    }

}