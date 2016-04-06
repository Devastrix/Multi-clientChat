/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static socketprogramming.GroupChatServer.clientPorts;
import static socketprogramming.GroupChatServer.server;

/**
 *
 * @author user
 */
public class Server {
  //  static int counter;
    static  ServerSocket serverSocket;
    static Socket servr;
    static int port;
    //static HashMap hm;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        //serverSocket.setSoTimeout(10000);
    }

    public static void main(String[] args) throws IOException {
      //  clientPorts = new Socket[10];
       // counter = 0;
       // hm = new HashMap();
        //  Scanner sc = new Scanner(System.in);
        port = 6006;
        Server sp = new Server(port);
        servr = serverSocket.accept();
        //System.out.println("Client connected");
          PrintWriter pwr = new PrintWriter(servr.getOutputStream());
          pwr.println("client connected");
          pwr.flush();
        //write thread
        Thread wrt = new WriteThread(servr);
        wrt.start();
        
        //read Thread
         Thread rd = new ReadThread(servr);
        rd.start();
            



        // System.out.println("new");
    }
}
 class WriteThread extends Thread {
   Socket sr;
     WriteThread(Socket s) {
         this.sr = s;
     }
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(sr.getOutputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        String str = "";
        while(true) {
            try {
                
                // reading terminal input
                str  = br.readLine();
                pwr.println(str);
                pwr.flush();
                
            } catch (IOException ex) {
                Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
 class ReadThread extends Thread {
    // Socket server;
     Socket sr;
     ReadThread(Socket s) {
         this.sr = s;
     }
     public void run() {
         BufferedReader bfr = null;
         try {
             bfr = new BufferedReader(new InputStreamReader(sr.getInputStream()));
         } catch (IOException ex) {
             Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         try {
            while(true) {
                String str = bfr.readLine();
                if(str.matches("exit")) {
                    System.exit(0);
                }
                System.out.println(str);
            } 
             
             
         } catch (IOException ex) {
             Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
}
    

