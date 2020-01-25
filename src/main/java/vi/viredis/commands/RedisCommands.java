package vi.viredis.commands;

import vi.viredis.exceptions.RedisException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface RedisCommands {


    // Connection Operations


    /**
     * Request for authentication in a password-protected Redis server.
     * Note: because of the high performance nature of Redis, it is possible to try a lot of passwords in parallel in very short time,
     * so make sure to generate a strong and very long password so that this attack is infeasible.
     *
     * @param plainTextPassword, Name of the connection
     * @return true, If  Authentication is successful.
     * @throws RedisException, If Authentication Fails.
     */
    boolean authenticate(String plainTextPassword) throws IOException;


    /**
     * Assign a name to current Connection
     *
     * @param connName, Name of the connection
     * @return {@code true), If  successfuly updated the connection name
     */
    boolean setConnectionName(String connName) throws IOException;


    /**
     * Get the current Connection Name
     *
     * @return {@code String), Name of the connection
     */
    String getConnectionName() throws RedisException, IOException;


    /**
     * Ask the server to close the connection.
     * The connection is closed as soon as all pending replies have been written to the client.
     *
     * @return {@code true} if connection close request is accepted, else {@code false}
     */
    boolean closeConnection() throws RedisException, IOException;


    /**
     *
     *
     * Basic Operations
     *
     */


    /**
     * Set a Key-Value pair in Redis Server
     *
     * @param key
     * @param value
     * @return True, If  key-value pair update is success in Redis Server
     */
    boolean put(String key, String value) throws IOException;


    /**
     * Get Value against key in Redis Server
     * If no value is present for the key, will return null.
     *
     * @param key
     * @return {@code String} Value against key
     */
    String get(String key) throws IOException;


    /**
     * Set Value against key in Redis Server and Get the Old value for key.
     * If no value is present for the key, It will set the value and  will return null.
     *
     * @param key
     * @param value
     * @return {@code String} Old Value against key.
     */
    String getSet(String key, String value) throws  IOException;


    /**
     * Get the values of all specified keys.
     * For every key that does not hold a string value or does not exist,null value will be returned.
     *
     * @param keys
     * @return List of values
     * <p>
     * eg: if Redis Server has :{"one": "first", "two":"second", "three":"third"}
     * Then,  getMultiKeys(list{"one","five","two"}), will return a list containing 3 items {"first, null, "second"}.
     */
    List<String> getMultiKeys(List<String> keys) throws IOException;


    /**
     * Get the Number of keys present in Redis Server
     *
     * @return {@code String} String representation of Total number of keys
     */
    String getDBSize() throws IOException;


    /**
     * Delete the Keys from Redis Server
     *
     * @param keys Keys to be deleted
     * @return {@code String}  String representation ofNumber of keys were deleted
     */
    String deleteKey(String... keys) throws IOException;


    /**
     * Set a timeout on key. After the timeout has expired, the key will automatically be deleted.
     * Important: A key with an associated timeout is often said to be volatile in Redis terminology
     * Before performing this operation, read documentation available at:https://redis.io/commands/expire
     *
     * @param key
     * @param timeOutInSeconds,
     * @return {@code true} if the timeout was set, else {@code false}.
     */
    boolean setTTLOnKey(String key, String timeOutInSeconds) throws IOException;


    /**
     * Checks if the key Exists in Redis Server
     *
     * @param key
     * @return {@code true} if Key exists in Redis Server, else {@code false}
     */
    boolean isKeyExist(String key) throws IOException;





    /**
     *
     *  Numeric Operations
     *
     */


    /**
     * Increments the number stored at key by one.
     * If the key does not exist, it is set to 0 before performing the operation.
     * <p>
     * This operation is limited to 64 bit signed integers.
     *
     * @param key
     * @return Incremented value  in String format
     * @throws RedisException, if the key contains a value of the wrong type or contains a string that can not be represented as integer.
     */
    String incr(String key) throws IOException;


    /**
     * Decrements the number stored at key by one.
     * If the key does not exist, it is set to 0 before performing the operation.
     * <p>
     * This operation is limited to 64 bit signed integers.
     *
     * @param key
     * @return Decremented value in String format
     * @throws RedisException, if the key contains a value of the wrong type or contains a string that can not be represented as integer.
     */
    String decr(String key) throws IOException;





    /**
     *
     *  List Operations
     *
     */


    /**
     * Push item(s) in the begining of a list.
     * Pushes the items in the same orders as itemsList contains.
     * Will Create a List in Redis Server, if list with name {@code listName} does not exist.
     * Throws exception if itemsList is empty.
     *
     * @param listName
     * @param itemsList List of {@Code String}
     * @return {@code String}   String representation of size of list in Server after adding itemsList data in List.
     */
    String pushInListAtStart(String listName, List<String> itemsList) throws IOException;


    /**
     * Push the item(s) in the end of a list.
     * Pushes the items in the same orders as itemsList contains.
     * Will Create a List in Redis Server, if list with name {@code listName} does not exist.
     *
     * @param listName
     * @param itemsList List of {@Code String}
     * @return {@code String}  String representation of size of list in Server after adding itemsList data in List.
     */
    String pushInListAtEnd(String listName, List<String> itemsList) throws IOException;


    /**
     * Removes and returns the first element from the list.
     * Will Return a null value, if list with name {@code listName} does not exist.
     *
     * @param listName
     * @return {@code String} Removed item from list.
     */
    String popListFromStart(String listName) throws IOException;


    /**
     * Removes and returns the last element from the list.
     * Will Return a null value, if list with name {@code listName} does not exist.
     *
     * @param listName
     * @return {@code String} Removed item from list.
     */
    String popListFromEnd(String listName) throws IOException;

    /**
     * Get the size of list from Redis Server
     *
     * @param listName
     * @return {@code String}  String representation of size of list
     */
    String getListLength(String listName) throws IOException;


    /**
     * Get the items from List.
     *
     * @param listName
     * @param startIndex
     * @param endIndex
     * @return List of items which are avaible from startIndex to EndIndex
     * <p>
     * eg: myList has items {"one", "two","three", "four"}
     * Then,  getListItems(myList, "0","2") will return a list containing 3 items {"one, "two", "three"}.
     * getListItems(myList, "8", "12"} will return a list containing 0 itmes.
     */
    List<String> getListItems(String listName, String startIndex, String endIndex) throws IOException;


    /**
     * Remove all the occurances of item from list
     *
     * @param listName
     * @param itemToBeRemoved, Item to be removed
     * @return {@code String} ,  String representation of Number of items removed from list.
     * <p>
     * eg: myList has items {"one", "two","three", "one" "four"}
     * Then,  removeItemFromList(myList, "one") will remove all "one"'s from list
     * and Will return 2.
     * If list is not present in redis server, method will return 0.
     * If item is not present in list means there is nothing to remove, then method will return 0.
     */
    String removeFromList(String listName, String itemToBeRemoved) throws IOException;


    /**
     *
     *
     * Set Operations
     *
     *
     *
     */


    /**
     * Add the specified members to the set stored at key
     * Specified members that are already a member of this set are ignored.
     * If key does not exist, a new set is created before adding the specified members.
     *
     * @param setName, Name of the set
     * @param items,   Item(s) to be added to set
     * @return {@code String} ,  String representation of Number of items added in set.
     */
    String addToSet(String setName, Set<String> items) throws IOException;


    /**
     * Returns the set cardinality (number of elements) of the set stored at key.
     * If set with 'setName' not present or set is empty, will return 0.
     *
     * @param setName, Name of the set
     * @return {@code String} ,  String representation of number of items in set.
     */
    String getSetSize(String setName) throws IOException;


    /**
     * Check if item is member of Set.
     *
     * @param setName, Name of the set
     * @param item,    item for isMember operation
     * @return {@code true}, if item is member of Set.
     */
    boolean isSetMember(String setName, String item) throws IOException;


    /**
     * Remove the specified members from the set stored at key.
     * Specified members that are not a member of this set are ignored.
     * If key does not exist, it is treated as an empty set and this method returns 0.
     *
     * @param setName, Name of the set
     * @param item,    item to be removed from set
     * @return {@code String},  String representation of Number of items removed from Set.
     */
    String removeFromSet(String setName, String item) throws IOException;





    /**
     *
     *
     * MultiOperations
     *
     */



    /**
     * Marks the start of a transaction block.
     * Subsequent operations will be queued for atomic execution using EXEC.
     *
     * Note: Once a Transactions block started,
     * All the subsequent operations (until executeTransaction() is called) will return :
     *  1. Operations which returns boolean, will return true .
     *  2. Operations which returns String , will return String "QUEUED".
     *  3. Operations which returns List</String>, will return a List</String> containing each item as String "QUEUED".
     *
     * @return {@code true} if successfully started transaction, else {@code false}
     * @see #executeTransaction()
     */
    boolean startTransaction() throws IOException;


    /**
     * Executes all previously queued commands in a transaction and
     * restores the connection state to normal.
     *
     * @return List, each element being the reply to each of the commands in the atomic transaction, which we started
     * after startTransaction() method call.
     * @see #startTransaction()
     */
    List executeTransaction()throws IOException;

}
