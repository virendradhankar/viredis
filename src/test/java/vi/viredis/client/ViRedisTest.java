package vi.viredis.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ViRedisTest {


    private ViRedis client;
    private String key1 = "key1";
    private  String key2 = "key2";
    private String value1 = "value1";
    private String value2 = "value2";
    private String nonExistingKey = "nonExistingKey";

    @Before
    public void setUp() throws Exception {

        Socket socket = new Socket("127.0.0.1", 6379);
        client = new ViRedis(socket);
        client.put(key1, value1);
        client.put(key2, value2);
    }

    @After
    public void tearDown() throws Exception {


        client.deleteKey(key1);
        client.deleteKey(key2);
        assertEquals(true, client.closeConnection());

    }

    @Test
    public void testConnection() throws IOException {
        assertEquals(true,client.setConnectionName("ViRedisTest"));
        assertEquals("ViRedisTest", client.getConnectionName());

        //Test connection with host and port name
        ViRedis redisClientWithHostPort = new ViRedis("127.0.0.1", 6379);
        assertEquals(true,redisClientWithHostPort.setConnectionName("ViRedisWithHostAndPort"));
        assertEquals("ViRedisWithHostAndPort", redisClientWithHostPort.getConnectionName());
    }



    @Test
    public void testBasicOperations() throws IOException {


        //Test put and Get
        assertEquals(true, client.put(key1, value1));
        assertEquals(true,client.put(key2, value2));

        assertEquals(value1, client.get(key1));
        assertEquals(value2, client.get(key2));


        String key = "key3";
        String value = "value3";
        client.put(key, value);

        //Test getSet
        assertEquals(value, client.getSet(key, value1));
        assertEquals(value1, client.get(key));

        //Test Delete key
        assertEquals("0", client.deleteKey(nonExistingKey));
        assertEquals("1", client.deleteKey(key));
        assertEquals(null, client.get(key));

        //Test isKeyExist
        assertEquals(true, client.isKeyExist(key1));
        assertEquals(false, client.isKeyExist(nonExistingKey));

    }


    @Test
    public void getMultiKeys() throws IOException {
        //Test getMultikeys
        List<String> keysList = new ArrayList<>();
        keysList.add(key1);
        keysList.add(nonExistingKey);
        keysList.add(key2);

        List<String> valuesList = client.getMultiKeys(keysList);

        assertEquals(3,valuesList.size());
        assertEquals(value1, valuesList.get(0));
        assertEquals(null, valuesList.get(1));
        assertEquals(value2, valuesList.get(2));

    }

    @Test
    public void testIncrDecr() throws IOException {
        String integerKey = "integerKey";
        String integerValue = "1000";
        client.put(integerKey, integerValue);
        assertEquals("1001", client.incr(integerKey));
        assertEquals("1002", client.incr(integerKey));

        assertEquals("1001", client.decr(integerKey));
        assertEquals("1000", client.decr(integerKey));
    }



    @Test
    public void testListCommands() throws IOException {

        String keyListName = "testList";
        List<String> itemsForList = new ArrayList<>();
        itemsForList.add(value1);
        itemsForList.add(value2);
        itemsForList.add(nonExistingKey);

        assertEquals("3", client.pushInListAtStart(keyListName, itemsForList));
        assertEquals("3", client.getListLength(keyListName));

        List listInRedisServer = client.getListItems(keyListName, "0", "5");
        assertEquals(3, listInRedisServer.size());


        assertEquals(nonExistingKey, client.popListFromStart(keyListName));
        assertEquals("1", client.removeFromList(keyListName, value2));
        assertEquals("1", client.removeFromList(keyListName, value1));


        assertEquals("3", client.pushInListAtEnd(keyListName, itemsForList));
        assertEquals("3", client.getListLength(keyListName));

        assertEquals(nonExistingKey, client.popListFromEnd(keyListName));
        assertEquals(value2, client.popListFromEnd(keyListName));
        assertEquals("0", client.removeFromList(keyListName, value2));

        client.deleteKey(keyListName);

    }

    @Test
    public void testSetCommands() throws IOException {

        String keySetName = "testSet";
        Set<String> valuesForSet = new HashSet();
        valuesForSet.add(value1);
        valuesForSet.add(value2);

        assertEquals("2",client.addToSet(keySetName, valuesForSet));
        assertEquals("0", client.addToSet(keySetName, valuesForSet));
        assertEquals("2", client.getSetSize(keySetName));
        assertEquals(true, client.isSetMember(keySetName, value1));
        assertEquals(false, client.isSetMember(keySetName, nonExistingKey));

        assertEquals("1", client.removeFromSet(keySetName, value1));
        assertEquals("0", client.removeFromSet(keySetName, value1));
        assertEquals("1", client.getSetSize(keySetName));


        client.deleteKey(keySetName);


    }



    @Test
    public void testSetTTLOnKey() throws IOException, InterruptedException {
        String key = "keyForTimeout";
        String value = "value";

        client.put(key, value);
        client.setTTLOnKey(key, "2");
        assertEquals(value, client.get(key));
        Thread.sleep(2500);
        assertEquals(null, client.get(key));
    }


    @Test
    public void testTransaction() throws IOException {

        String integerKey = "integerKey";
        String intValue = "1000";
        client.put(key1, value1);
        client.put(integerKey, intValue);
        boolean response = client.startTransaction();
        assertEquals(true, response);

        assertEquals("QUEUED",client.get(key1));
        assertEquals("QUEUED",client.get(integerKey));
        assertEquals("QUEUED", client.incr(integerKey));
        assertEquals("QUEUED", client.incr(integerKey));

        List<String> transOutput = client.executeTransaction();
        assertEquals(value1,transOutput.get(0));
        assertEquals(intValue, transOutput.get(1));
        assertEquals(1001, Long.parseLong(transOutput.get(2)));
        assertEquals(1002, Long.parseLong(transOutput.get(3)));

        client.deleteKey(integerKey);

    }

    @Test
    public void testParalledClients() throws IOException {

        int totalClients = 50;
        ViRedis [] clients = new ViRedis[totalClients];
        Thread [] threads = new Thread[totalClients];

        for(int i=0; i<totalClients; i++){
            Socket socket = new Socket("127.0.0.1",6379);
            ViRedis client = new ViRedis(socket);
            clients[i] = client;
        }


        for(int i=0; i<totalClients; i++){
            assertEquals(true,clients[i].put(key1+i,value1+i));
        }

        for(int i=0; i<totalClients; i++){
            assertEquals(value1+i,clients[i].get(key1+i));
        }

    }



}