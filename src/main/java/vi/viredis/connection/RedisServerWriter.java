package vi.viredis.connection;

import vi.viredis.commands.RedisProtocolCommand;
import vi.viredis.constants.RedisConstants;
import vi.viredis.exceptions.RedisException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RedisServerWriter {

    private OutputStream outputStream;

    public RedisServerWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public void sendCommand(List<Object> argsToRedis) throws IOException {
        
        writeCharToOutputStream(RedisConstants.CHAR_STAR);
        writeBytesToOutputStream(Integer.toString(argsToRedis.size()).getBytes());
        writeBytesToOutputStream(RedisConstants.BYTE_CRLN);

        for(Object obj : argsToRedis){

            if(obj == null){
                writeNullValuesToOutputStream();
            } else if(obj instanceof RedisProtocolCommand){
                String strObj = obj.toString();
                writeStringToOutputStream(strObj);
            } else if(obj instanceof String){
                String strObj = (String) obj;
                writeStringToOutputStream(strObj);
            } else if(obj instanceof  Integer){
                Integer intObj = (Integer)obj;
                writeIntegerToOutputStream(intObj);
            } else if(obj instanceof Long){
                Long longObj = (Long) obj;
                writeLongToOutputStream(longObj);

            } else{
                throw new RedisException("ERR Invalid command");
            }
        }
        outputStream.flush();
    }

    private void writeNullValuesToOutputStream() throws IOException {
        writeCharToOutputStream(RedisConstants.CHAR_DOLLAR);
        writeBytesToOutputStream(RedisConstants.NULL_REPRESENTATION.getBytes());
        writeBytesToOutputStream(RedisConstants.BYTE_CRLN);

    }


    private void writeIntegerToOutputStream(Integer intObj) throws IOException {
        writeCharToOutputStream(RedisConstants.CHAR_COLON);
        writeBytesToOutputStream(Integer.toString(intObj).getBytes());
        writeBytesToOutputStream(RedisConstants.BYTE_CRLN);

    }

    private void writeLongToOutputStream(long longObj) throws IOException {
        writeCharToOutputStream(RedisConstants.CHAR_COLON);
        writeBytesToOutputStream(Long.toString(longObj).getBytes());
        writeBytesToOutputStream(RedisConstants.BYTE_CRLN);

    }

    private void writeStringToOutputStream(String str) throws IOException {
        writeCharToOutputStream(RedisConstants.CHAR_DOLLAR);
        writeBytesToOutputStream(Integer.toString(str.length()).getBytes());
        writeBytesToOutputStream(RedisConstants.BYTE_CRLN);
        writeBytesToOutputStream(str.getBytes());
        writeBytesToOutputStream(RedisConstants.BYTE_CRLN);
    }

    private void writeBytesToOutputStream(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    private void writeCharToOutputStream(char singleChar) throws IOException {
        outputStream.write(singleChar);
    }

    public void sendCommand(Object ... args) throws IOException {

        List<Object> argsList = new ArrayList<>();
        for(Object arg: args){
            if(arg instanceof Collection){
                argsList.addAll((Collection<?>) arg);
            } else{
                argsList.add(arg);
            }
        }
        sendCommand(argsList);
    }

    public void checkForEmptyInput(Collection<String> inputCollection) throws RedisException {
        if( inputCollection.isEmpty()){
            String errorMsg = "";
            if(inputCollection instanceof List){
                errorMsg = "ERR Input list cannot be empty";
            } else if(inputCollection instanceof Set){
                errorMsg = "ERR Input set cannot be empty";
            }
            throw new RedisException(errorMsg);
        }
    }
}
