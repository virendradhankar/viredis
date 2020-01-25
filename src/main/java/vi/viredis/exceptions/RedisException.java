package vi.viredis.exceptions;

import java.io.IOException;

public class RedisException extends IOException {

    public RedisException(String errorMsg){
        super(errorMsg);
    }

    public RedisException(String errorMsg, Throwable e){ super(errorMsg, e);}
}
