package vi.viredis;

import vi.viredis.connection.RedisServerReader;
import vi.viredis.client.ViRedis;
import vi.viredis.exceptions.RedisException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VirendraTest {

    private Socket socket = null;
    private InputStream input = null;
    private OutputStream out = null;

    private static byte[] CRLF = new byte[]{'\r', '\n'};
    // constructor to put ip address and port

    public VirendraTest() throws IOException {
        socket = new Socket("127.0.0.1", 6379);
        System.out.println("Connected");
        // takes input from terminal
        input = new DataInputStream(socket.getInputStream());
        // sends output to the socket
        out = new DataOutputStream(socket.getOutputStream());

    }



    public static void main(String[] args) throws Exception {
        VirendraTest vt = new VirendraTest();
      //  vt.singleCommandTest();
     //   vt.listCommandTest();
        vt.myRedisTest();
    }


    private void myRedisTest() throws IOException, RedisException, InterruptedException {

        Socket s1 = new Socket("127.0.0.1", 6379);

        Socket s2 = new Socket("127.0.0.1", 6379);


        ViRedis myRedis1 = new ViRedis(s1);
        ViRedis myRedis2 = new ViRedis(s2);


        Set<String> myset = new HashSet<>();
     //   myset.add("first");
     //   myset.add("Second");
        List<String> mylist = new ArrayList<>();
        mylist.add("k1");
        mylist.add("k5");
        mylist.add("k2");
      //  Object res1 = myRedis1.put("viren", "VV");

      //  res1 = myRedis1.setTTLOnKey("viren", "10");
    //   Object res2 =  myRedis1.getSet("viren", "Testing 1234");
    //    Object res2 = myRedis1.getMultiKeys(new ArrayList<>());

        Object res1 = myRedis1.startTransaction();
        res1 = myRedis1.get("viren");
        res1 = myRedis1.get("vivek");
        res1 = myRedis1.incr("vivek");
        res1 = myRedis1.put("hello", "test");
        res1 = myRedis1.get("vivek");

        Object res2 = myRedis1.executeTransaction();



;
        RedisServerReader reader = new RedisServerReader(null);




         //Object ress = myRedis1.getResponse("t");


      //  Object res4 = myRedis1.closeConnection();



     //   Object clientlist = myRedis1.getclientlist();

      //  Thread.sleep(15000);

        Thread t1 = new Thread(){
            @Override
            public void run(){
                try {
                    System.out.print("Sending 1" );

                   // Boolean put1 = myRedis1.put("v1","testv1");
                    String put1 = myRedis1.get("viren");
                    System.out.println("viren Get " + put1);
                } catch (RedisException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };

        Thread t2 = new Thread(){
            @Override
            public void run(){
                try {
                    System.out.println("Sending push 2" );
                 //   List<String> push1 = myRedis1.getListItems("vive", "1", "3");
                    String push2 = myRedis1.incr("vivek");
                    System.out.println("Vivek Incr   " + push2 );

                } catch (RedisException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };
//
        //t1.start();
   //     Thread.sleep(1000);
      //      t2.start();

    //    Object put1 = myRedis1.put("v1","testv1");


    //    Object push1 = myRedis2.getListItems("vive", "1", "3");





        /*MyRedis myRedis = new MyRedis("127.0.0.1", 6379);

        String key = "myListt";
        String value = "myNewValue";




        Long valueLong = 100L;
      //  myRedis.put(key, value);

        List<String> arrList = new ArrayList<>();
        arrList.add("one");
        arrList.add("two");

        arrList.add("three");



    //    Object responsee = myRedis.getListItems("vive", "1", "3");
    //  int  response = myRedis.pushInList(key, arrList);

   //     Object responseee = myRedis.getListLength("dummy");

        Object getRes = myRedis.put("vivi", "10000");

        Object obj = myRedis.get("duma");
*/
        System.out.println("Tested");


    }

    public void listCommandTest() throws Exception{


        String s = "test";

       ViRedis ms = new ViRedis(new Socket());

        List list = new ArrayList();
        list.add(s);

        ms.pushInListAtStart("test", list);

        String command = "LPUSH";
        String key = "mytestlist";
     //   String value = "virendra";
        String value1 = "virendra";

        String value2 = "annu";

        String value3 = "viren";

        //3, Array of 3 Strings
        String line ="*3";
        out.write('*');
        out.write("5".getBytes());
        out.write(CRLF);

        //working
        //  String value = "SET vivek si 50";

        out.write('$');
        out.write(Integer.toString(command.length()).getBytes());
        out.write(CRLF);
        out.write(command.getBytes());
        out.write(CRLF);


        out.write('$');
        out.write(Integer.toString(key.length()).getBytes());
        out.write(CRLF);
        out.write(key.getBytes());
        out.write(CRLF);


        out.write('$');
        out.write(Long.toString(value1.length()).getBytes());
        out.write(CRLF);
        out.write(value1.getBytes());
        out.write(CRLF);

        out.write('$');
        out.write(Long.toString(value2.length()).getBytes());
        out.write(CRLF);
        out.write(value2.getBytes());
        out.write(CRLF);

        out.write('$');
        out.write(Long.toString(value3.length()).getBytes());
        out.write(CRLF);
        out.write(value3.getBytes());
        out.write(CRLF);
        out.flush();


/*
                byte[] val = Long.toString(value.length()).getBytes();
                String s = new String(val, "UTF-8");

                line = line+"$"+s+ "\r\n" + value + "\r\n";

                System.out.println(line);

                out.write(line.getBytes());

*/



        int ch = input.read();
        System.out.print(Character.toChars(ch));
        while(ch != '\n'){
            ch = input.read();
            System.out.print(Character.toChars(ch));
        }






        //Execute Command get vivek
        //Get Vivek

        command = "LRANGE";
        out.write('*');
        out.write("4".getBytes());
        out.write(CRLF);

        //working



        out.write('$');
        out.write(Integer.toString(command.length()).getBytes());
        out.write(CRLF);
        out.write(command.getBytes());
        out.write(CRLF);


        out.write('$');
        out.write(Long.toString(key.length()).getBytes());
        out.write(CRLF);
        out.write(key.getBytes());
        out.write(CRLF);


        String startIndex = "0";
        String endIndex = "5";
        out.write('$');
        out.write(Long.toString(startIndex.length()).getBytes());
        out.write(CRLF);
        out.write(startIndex.getBytes());
        out.write(CRLF);


        out.write('$');
        out.write(Long.toString(endIndex.length()).getBytes());
        out.write(CRLF);
        out.write(endIndex.getBytes());
        out.write(CRLF);

        ch = input.read();

        if(ch == '*'){
            ch = input.read();
            input.read(); //read \r
            input.read();  // read \n
            int i =1;
            while(i<= ch-48){
                int nextchar = input.read();
                   while (nextchar != '\n'){
                       System.out.print(Character.toChars(nextchar));
                       nextchar = input.read();
                   }
                i++;
            }

        }

        System.out.print(Character.toChars(ch));
        while(ch != '\n'){
            ch = input.read();
            System.out.print(Character.toChars(ch));
        }





        ch = input.read();
        System.out.print(Character.toChars(ch));
        while(ch != '\n'){
            ch = input.read();
            System.out.print(Character.toChars(ch));
        }

        //  out.flush();





    }



    public void singleCommandTest() throws Exception{



            String command = "SET";
            String key = "vivek kumar";
            String value = ".50 singh";
              //3, Array of 3 Strings
               String line ="*3";
               out.write('*');
               out.write("3".getBytes());
               out.write(CRLF);

               //working
              //  String value = "SET vivek si 50";

                out.write('$');
                out.write(Integer.toString(command.length()).getBytes());
                out.write(CRLF);
                out.write(command.getBytes());
                out.write(CRLF);


                out.write('$');
                out.write(Integer.toString(key.length()).getBytes());
                out.write(CRLF);
                out.write(key.getBytes());
                out.write(CRLF);


                out.write('$');
                out.write(Long.toString(value.length()).getBytes());
                out.write(CRLF);
                out.write(value.getBytes());
                out.write(CRLF);


/*
                byte[] val = Long.toString(value.length()).getBytes();
                String s = new String(val, "UTF-8");

                line = line+"$"+s+ "\r\n" + value + "\r\n";

                System.out.println(line);

                out.write(line.getBytes());

*/



                int ch = input.read();
                System.out.print(Character.toChars(ch));
                while(ch != '\n'){
                   ch = input.read();
                    System.out.print(Character.toChars(ch));
                }






                //Execute Command get vivek
                //Get Vivek

        command = "GET";
        out.write('*');
        out.write("2".getBytes());
        out.write(CRLF);

        //working



        out.write('$');
        out.write(Integer.toString(command.length()).getBytes());
        out.write(CRLF);
        out.write(command.getBytes());
        out.write(CRLF);


        out.write('$');
        out.write(Long.toString(key.length()).getBytes());
        out.write(CRLF);
        out.write(key.getBytes());
        out.write(CRLF);

         ch = input.read();
        System.out.print(Character.toChars(ch));
        while(ch != '\n'){
            ch = input.read();
            System.out.print(Character.toChars(ch));
        }


        ch = input.read();
        System.out.print(Character.toChars(ch));
        while(ch != '\n'){
            ch = input.read();
            System.out.print(Character.toChars(ch));
        }

              //  out.flush()

    }





}