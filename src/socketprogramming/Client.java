/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketprogramming;

/**
 *
 * @author user
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static socketprogramming.Server.serverSocket;
import static socketprogramming.GroupChatServer.clientPorts;
import static socketprogramming.GroupChatServer.server;
import static socketprogramming.Server.servr;

/**
 *
 * @author user
 */
public class Client {
  //  static int counter;
    static  ServerSocket serverSocket;
    static Socket servr;
    static int port;
    //static HashMap hm;

   

    public static void main(String[] args) throws IOException {
      //  clientPorts = new Socket[10];
       // counter = 0;
       // hm = new HashMap();
        //  Scanner sc = new Scanner(System.in);
        port = 6006;
        
        //servr = serverSocket.accept();
        Socket cl = null;
        try {
        cl = new Socket("localhost", port);
        System.out.println("Connected to server");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        PrintWriter pwr = new PrintWriter(cl.getOutputStream());
          pwr.println("Server connected");
          pwr.flush();
        //write thread
        Thread wrt = new WrThread(cl);
        wrt.start();
        
        //read Thread
         Thread rd = new RdThread(cl);
        rd.start();
            



        // System.out.println("new");
    }
}
 class WrThread extends Thread {
   Socket sr;
     WrThread(Socket s) {
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
                
               // System.out.println(str);
                pwr.println(str);
                pwr.flush();
                if(str.matches("exit")) {
                    System.exit(0);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
 class RdThread extends Thread {
    // Socket server;
     Socket sr;
     RdThread(Socket s) {
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
                System.out.println(bfr.readLine());
            } 
             
             
         } catch (IOException ex) {
             Logger.getLogger(WriteThread.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
}
    


