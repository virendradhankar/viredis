package vi.viredis.connection;

import vi.viredis.exceptions.RedisException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static vi.viredis.constants.RedisConstants.*;

public class RedisServerReader {

    private InputStream inputStream;
    private int DEFAULT_BUFFER_SIZE = 1024;


    public RedisServerReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Object readResponse() throws IOException {

        int ch = inputStream.read();
        Object response = null;

        switch(ch) {
            case CHAR_PLUS:
              response =  readSimpleString();
                break;
            case CHAR_DOLLAR:
              response =  readBulkString();
                break;
            case CHAR_COLON:
               response = readNumber();
                break;
            case CHAR_STAR:
                response = readRedisArray();
                break;
            case CHAR_MINUS:
                String errorMsg = readSimpleString();
                throw new RedisException(errorMsg);
            default:
                break;
        }

        return response;
    }

    private Long readNumber() throws IOException, RedisException {
        String response = readSimpleString();
        return Long.parseLong(response);

    }

    private String readBulkString() throws IOException, RedisException {

        int ch =  readNumber().intValue();
        if(ch == -1){
            return null;
        }
        byte [] byteArr = new byte[ch];
        inputStream.read(byteArr);

        if(inputStream.read() != '\r'){
            throw new RedisException("CR  was expected");
        }
        if(inputStream.read() != '\n'){
            throw new RedisException("LR was expected");
        }
        return new String(byteArr);
    }

    private List<Object> readRedisArray() throws IOException, RedisException {
        long len = readNumber();
        if(len == -1){
            return null;
        }
        List<Object> response = new ArrayList();
        int i = 0;
        while(i < len){
            response.add(readResponse());
            i++;
        }
        return response;
    }

    private String readSimpleString() throws IOException, RedisException {
        Integer bufferSize = DEFAULT_BUFFER_SIZE;
        byte [] bufferArr = new byte[bufferSize];
        int ch = 0;
        int index = 0;
        while( (ch = inputStream.read()) != '\r' ){

            byte b = (byte)ch;
            bufferArr[index++] = b;

            if(index == bufferSize){
              bufferArr =  resizeBuffer(bufferArr, bufferSize);
            }
        }

        ch = inputStream.read();
        if(ch != '\n'){
            throw new RedisException("LR was expected from Redis Server. But found:"+ Character.toChars(ch));
        }
        return new String(bufferArr, 0, index);
    }

    private byte[] resizeBuffer(byte[] bufferArr, Integer bufferSize) {

        byte[] newBufferArr = new byte[2* bufferSize];
        System.arraycopy(bufferArr, 0, newBufferArr, 0, bufferSize-1);
        bufferSize = bufferSize *2;
        return newBufferArr;
    }


    public boolean readResponseAsBool() throws IOException, RedisException {
        Object response = readResponse();
        if(response != null){
            if(response instanceof String){
               return !((String)response).isEmpty();
            } else if(response instanceof Long){
               return ((Long)response) > 0;
            }
        }
        return false;
    }

    public String readResponseAsString() throws IOException, RedisException {
        Object response = readResponse();
        return (response== null ? null: response.toString());
    }

    public List<String> readResponseAsList() throws IOException, RedisException {
        Object response =  readResponse();
        return getResponseInListFormat(response);

    }

    private List<String> getResponseInListFormat(Object response) {
        List<String> responseList = new ArrayList();
        if(response instanceof List){
            List list = (List)response;
            for(Object o : list){
                responseList.add(o == null ? null :o.toString());
            }
        } else{
            responseList.add(response.toString());
        }
        return responseList;

    }
}
