package vi.viredis.client;


import vi.viredis.commands.RedisCommands;
import vi.viredis.commands.RedisProtocolCommand;
import vi.viredis.connection.RedisServerReader;
import vi.viredis.connection.RedisServerWriter;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ViRedis implements RedisCommands {
    private RedisServerWriter writer;
    private RedisServerReader reader;

    public ViRedis(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        init(socket);
    }

    public ViRedis(Socket socket) throws IOException {
        init(socket);
    }

    private void init(Socket socket) throws IOException {
        reader = new RedisServerReader(socket.getInputStream());
        writer = new RedisServerWriter(socket.getOutputStream());
    }

    @Override
    public boolean authenticate(String plainTextPassword) throws IOException {
        writer.sendCommand(RedisProtocolCommand.AUTH, plainTextPassword);
        return reader.readResponseAsBool();
    }

    @Override
    public boolean setConnectionName(String connName) throws IOException {
        writer.sendCommand(RedisProtocolCommand.CLIENT, RedisProtocolCommand.SETNAME, connName);
        return reader.readResponseAsBool();
    }

    @Override
    public String getConnectionName() throws IOException {
        writer.sendCommand(RedisProtocolCommand.CLIENT, RedisProtocolCommand.GETNAME);
        return reader.readResponseAsString();
    }


    @Override
    public boolean closeConnection() throws IOException {
        writer.sendCommand(RedisProtocolCommand.QUIT);
        return reader.readResponseAsBool();
    }

    @Override
    public boolean put(String key, String value) throws IOException {
        writer.sendCommand(RedisProtocolCommand.SET, key, value);
        return reader.readResponseAsBool();
    }


    @Override
    public String get(String key) throws IOException {
        writer.sendCommand(RedisProtocolCommand.GET, key);
        return reader.readResponseAsString();
    }

    @Override
    public String getSet(String key, String value) throws IOException {
        writer.sendCommand(RedisProtocolCommand.GETSET, key, value);
        return reader.readResponseAsString();
    }

    @Override
    public List<String> getMultiKeys(List<String> keys) throws IOException {
        writer.checkForEmptyInput(keys);
        writer.sendCommand(RedisProtocolCommand.MGET, keys);
        return reader.readResponseAsList();
    }


    @Override
    public String incr(String key) throws IOException {
        writer.sendCommand(RedisProtocolCommand.INCR, key);
        return reader.readResponseAsString();
    }

    @Override
    public String decr(String key) throws IOException {
        writer.sendCommand(RedisProtocolCommand.DECR, key);
        return reader.readResponseAsString();
    }


    @Override
    public String pushInListAtEnd(String listName, List<String> itemsList) throws IOException {
        writer.checkForEmptyInput(itemsList);
        writer.sendCommand(RedisProtocolCommand.RPUSH, listName, itemsList);
        return reader.readResponseAsString();
    }

    @Override
    public String popListFromStart(String listName) throws IOException {
        writer.sendCommand(RedisProtocolCommand.LPOP, listName);
        return reader.readResponseAsString();
    }

    @Override
    public String popListFromEnd(String listName) throws IOException {
        writer.sendCommand(RedisProtocolCommand.RPOP, listName);
        return reader.readResponseAsString();
    }


    @Override
    public String pushInListAtStart(String listName, List<String> itemsList) throws IOException {
        writer.checkForEmptyInput(itemsList);
        writer.sendCommand(RedisProtocolCommand.LPUSH, listName, itemsList);
        return reader.readResponseAsString();
    }

    public String getListLength(String listName) throws IOException {
        writer.sendCommand(RedisProtocolCommand.LLEN, listName);
        return reader.readResponseAsString();
    }


    public List<String> getListItems(String listName, String startIndex, String endIndex) throws IOException {
        writer.sendCommand(RedisProtocolCommand.LRANGE, listName, startIndex, endIndex);
        return reader.readResponseAsList();
    }

    @Override
    public String removeFromList(String listName, String itemToBeRemoved) throws IOException {
        writer.sendCommand(RedisProtocolCommand.LREM, listName, Integer.toString(0), itemToBeRemoved);
        return reader.readResponseAsString();
    }

    @Override
    public String addToSet(String setName, Set<String> items) throws IOException {
        writer.checkForEmptyInput(items);
        writer.sendCommand(RedisProtocolCommand.SADD, setName, items);
        return reader.readResponseAsString();
    }

    @Override
    public String getSetSize(String setName) throws IOException {
        writer.sendCommand(RedisProtocolCommand.SCARD, setName);
        return reader.readResponseAsString();
    }

    @Override
    public boolean isSetMember(String setName, String item) throws IOException {
        writer.sendCommand(RedisProtocolCommand.SISMEMBER, setName, item);
        return reader.readResponseAsBool();
    }

    @Override
    public String removeFromSet(String setName, String item) throws IOException {
        writer.sendCommand(RedisProtocolCommand.SREM, setName, item);
        return reader.readResponseAsString();
    }


    @Override
    public String getDBSize() throws IOException {
        writer.sendCommand(RedisProtocolCommand.DBSIZE);
        return reader.readResponseAsString();
    }

    @Override
    public String deleteKey(String... keys) throws IOException {
        writer.sendCommand(RedisProtocolCommand.DEL, Arrays.asList(keys));
        return reader.readResponseAsString();
    }

    @Override
    public boolean setTTLOnKey(String key, String timeOutInSeconds) throws IOException {
        writer.sendCommand(RedisProtocolCommand.EXPIRE, key, timeOutInSeconds);
        return reader.readResponseAsBool();
    }

    @Override
    public boolean isKeyExist(String key) throws IOException {
       writer.sendCommand(RedisProtocolCommand.EXISTS, key);
       return reader.readResponseAsBool();
    }

    @Override
    public boolean startTransaction() throws IOException {
        writer.sendCommand(RedisProtocolCommand.MULTI);
        return reader.readResponseAsBool();
    }

    @Override
    public List executeTransaction() throws IOException {
        writer.sendCommand(RedisProtocolCommand.EXEC);
        return reader.readResponseAsList();
    }
}

