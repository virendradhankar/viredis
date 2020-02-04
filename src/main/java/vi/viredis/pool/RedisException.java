package vi.viredis.pool;

/*
 * whenever there is an connectivity issue with the redis server
 * this exception will be thrown.
 */
public class RedisException extends Exception {

    public RedisException(String message) {
        super(message);
    }
}
