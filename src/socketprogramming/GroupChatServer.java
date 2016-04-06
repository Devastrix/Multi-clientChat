/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketprogramming;

/**
 *
 * @author user
 */
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static socketprogramming.GroupChatServer.hm;

public class GroupChatServer extends Thread {

    static Socket[] clientPorts;
    static int counter;
    static private ServerSocket serverSocket;
    static Socket server;
    static int port;
    static HashMap<Socket, String> hm;

    public GroupChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        //serverSocket.setSoTimeout(10000);
    }

    public static void main(String[] args) throws IOException {
        clientPorts = new Socket[10];
        counter = 0;
        hm = new HashMap();
        //  Scanner sc = new Scanner(System.in);
        port = 6006;
        GroupChatServer sp = new GroupChatServer(port);



        try {
            while (true) {
                server = serverSocket.accept();
                // System.out.println("new client:");
                if (hm.get(server) == null) {
                    //NaamMeKya nk = new NaamMeKya();
                    hm.put(server, "");
                    System.out.println("new client");
                    //continue;
                }
                Thread t = new ServerrThread(server);
                t.start();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        // System.out.println("new");
    }
}

class ServerrThread extends Thread {

    String line = null;
    BufferedReader is = null;
    PrintWriter os = null;
    Socket s = null;

    public ServerrThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String ClientKaNaam = is.readLine();
            GroupChatServer.hm.put(s,ClientKaNaam);
            // sabko btao ki mai aa gya
             Set set = GroupChatServer.hm.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    if (me.getKey() == s) {
                        continue;
                    }
                    os = new PrintWriter(((Socket) (me.getKey())).getOutputStream());
                    os.println(ClientKaNaam+ " has connected !");
                    os.flush();
                }



        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {
              is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true) {
                line = is.readLine();
                System.out.println(line);
                if (line.matches("")) {
                    String leavingMan = hm.get(s);
                    GroupChatServer.hm.remove(s);
                    os = new PrintWriter(s.getOutputStream());
                    os.println("exit");
                    os.flush();
                    System.out.println("---------closing client-------");

                    Set set = GroupChatServer.hm.entrySet();
                    if(hm.get(s) == null) {
                        System.out.println("bahar ho gaya");
                    }
                    Iterator i = set.iterator();
                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry) i.next();
                        
                        os = new PrintWriter(((Socket) (me.getKey())).getOutputStream());
                        os.println(EncDec.encrypt(leavingMan+" has left..."));
                        os.flush();
                    }
                    break;
                }

                Set set = GroupChatServer.hm.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    if (me.getKey() == s) {
                        continue;
                    }
                    os = new PrintWriter(((Socket) (me.getKey())).getOutputStream());
                    os.println(line);
                    os.flush();
                }





                //   System.out.println("Response to Client  :  "+line);

            }
//        System.out.println("bahar ");
        } catch (IOException e) {
            e.printStackTrace();
            line = this.getName(); //reused String line for getting thread name
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getName(); //reused String line for getting thread name
            System.out.println("Client " + line + " Closed");
        } catch (Exception ex) {
            Logger.getLogger(ServerrThread.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}