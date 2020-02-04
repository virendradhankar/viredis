public class RedisPool extends ObjectPool {

    public RedisPool(int size, String host, int port) {
        super(size, host, port);
    }

}