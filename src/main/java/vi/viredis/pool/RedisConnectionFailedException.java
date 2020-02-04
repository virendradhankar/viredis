package vi.viredis.pool;

public class RedisConnectionFailedException extends Exception {

    public RedisConnectionFailedException(String message) {
        super(message);
    }
}
