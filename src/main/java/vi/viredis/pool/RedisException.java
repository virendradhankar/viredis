package vi.viredis.pool;

/*
 * if there is an connectivity issue with the redis server
 * exception will be thrown.
 */
public class RedisException extends Exception {

    public RedisException(String message) {
        super(message);
    }
}
