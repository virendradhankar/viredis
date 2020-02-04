import org.junit.Test;
import vi.viredis.client.ViRedis;

public class RedisPoolTest {
    private RedisPool redisPool;
    private String host = "127.0.0.1";
    private int port = 6379;

    @Test
    public void testCreateRedisConnectionPool() {
        redisPool = new RedisPool(10, host, port);
        assert redisPool.size() == 10;
    }

    @Test
    public void testAfterGettingConnectionPoolSizeShouldDecrease(){
        redisPool = new RedisPool(11, host, port);
        ViRedis viRedis = redisPool.get();
        assert viRedis != null;
        assert redisPool.size() == 10;
    }

    @Test
    public void testAfterReleasingConnectionPoolSizeShouldIncrease(){
        redisPool = new RedisPool(11, host, port);
        ViRedis viRedis = redisPool.get();
        redisPool.release(viRedis);
        assert redisPool.size() == 11;
    }

    @Test(expected = NullPointerException.class)
    public void testConnectionShouldFailWhenPortIsInvalid() {
        redisPool = new RedisPool(10, host, 6380);
    }

    @Test
    public void testCallingShutdownShouldReleaseAllConnections(){
        redisPool = new RedisPool(10, host, port);
        redisPool.shutdown();
        assert redisPool.size() == 0;
    }

}