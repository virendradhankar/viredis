# ViRedis
-High level Redis client

## Usage
-It is a maven project.
-$mvn clean install, will generated a .jar file. Add this jar as dependency in your project.
-Create a socket connection and start talking to Redis.

```java
vi.viredis.client.ViRedis client = new vi.viredis.client.ViRedis(new Socket("127.0.0.1", 6379));
client.put("foo", "bar");
System.out.println(client.get("foo"); // will print 'bar'
```


## Running test cases
-Start a redis server on local host(127.0.0.1) and port 6379.
-Execute Junit test cases.

## Is the connection thread safe?
- No. One client connection must be used by single thread at any point of time.

## How to manage connections for multithread environment ?
- It's upto the user of this library.
- 1. Socket connections could be created for every thread on the fly
- OR
- 2. Create a pool of connection. Managing a connection pool is not at all Redis-specific.


## Questions? Issues?
- Feel free to contact virendradhankar@gmail.com

## Want to contribute?
Take a fork of this git hub repo.
Raise a PR with details about issue and fix of it.
