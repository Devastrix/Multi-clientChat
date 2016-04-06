/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketprogramming;

/**
 *
 * @author user
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

public class GroupChatClient extends Thread {

    // s;
    static JTextArea chat;
    static JTextField nameFd;
    static JTextArea event;
    static JLabel jl;
    static JButton leave;
    static JTextField mesgField;
    static JButton send;
    static OutputStream outToServer;
    static DataOutputStream out;
    static InputStream inFromServer;
    static DataInputStream in;
    static Socket client;
    static int port;
    public static String naam;
    static PrintWriter os;
    static BufferedReader is;
    private static JButton stopStart;
    public String nN;

  
    @Override
    public void run() {
        is = null;
        try {
            is = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            String response = "";
            try {
                String bakwas = is.readLine();
                if (bakwas.contains("has ") || bakwas.contains("exit")) {
                    
                    // dont decrypt
                    response = bakwas;
                    //break;
                } else {
                    // decrypt
                    response = EncDec.decrypt(bakwas);
                   // response = bakwas;
                }

            } catch (IOException ex) {
                Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if(response.matches("exit")) break;
                System.out.println(response);
           //     chat.setForeground(Color.BLACK);
                chat.append(response + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    static boolean connect(String host) {
        try {
            System.out.println("Connecting to " + "localhost"
                    + " on port " + port);
            client = new Socket(host, port);

            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            return true;
        } catch (UnknownHostException ex) {
            System.out.println("Error in connection");
            ////Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            System.out.println("Error in connection");
            // Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
   
    public static void main(String[] args) throws IOException {

        //ClientGui cg = new ClientGui();
        //this.naam = "";
        
        AuthGui ag = new AuthGui();
        ag.init();
        port = 6006;

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name : ");
    }

    public static void readPart() throws IOException {
      
        try {

//            outToServer = client.getOutputStream();
//         out = new DataOutputStream(outToServer);
//        
//          inFromServer = client.getInputStream();
//         in = new DataInputStream(inFromServer);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            os = new PrintWriter(client.getOutputStream());
//       
//          Thread readServer = new GroupChatClient();
//         readServer.start();

//         while(true) {
            //   client = new Socket("localhost", port);
            Thread st = new GroupChatClient();
            st.start();


            // client.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    // auth GUI
    public static class AuthGui extends JFrame {

        void init() {
            getContentPane().setLayout(null);
            final JTextField username = new JTextField();
            final JPasswordField pass = new JPasswordField();
            JButton reg = new JButton("Register");
            JButton login = new JButton("Login");
            final JLabel lb = new JLabel();
            username.setBounds(100, 50, 200, 50);
            pass.setBounds(100, 110, 200, 50);
            reg.setBounds(90, 170, 100, 50);
            login.setBounds(200, 170, 100, 50);
            lb.setBounds(200, 250, 300, 100);
            final JCheckBox cb = new JCheckBox();
            cb.setBounds(310, 130, 20, 20);
            JLabel cblb = new JLabel("Show");
            cblb.setBounds(335, 125, 50, 30);
            add(username);
            add(cblb);
            add(cb);
            add(login);
            add(reg);
            add(pass);
            add(lb);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(600, 400);

            setVisible(true);


            cb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (cb.isSelected()) {
                        // show password
                        pass.setEchoChar((char) 0);
                    } else {
                        pass.setEchoChar('*');
                    }
                    // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            // login listener
            login.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    File f = new File("users.txt");
                    if (!f.exists()) {
                        try {
                            f.createNewFile();
                        } catch (IOException ex) {
                            Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    String usr = username.getText().toString().trim();
                    String pwd = pass.getText().toString().trim();
                    if (!usr.matches("") && !pwd.matches("")) {
                        // proceed to login
                        FileReader pr = null;
                        try {
                            pr = new FileReader(f);
                            BufferedReader br = new BufferedReader(pr);
                            String getUser = "";
                            int flag = 0;
                            while ((getUser = br.readLine()) != null) {

                                if (getUser.matches(usr + ":" + pwd)) {
                                    //found
                                    flag = 1;
                                    break;
                                }
                            }
                            br.close();
                            pr.close();

                            if (flag == 1) {
                                // found user
                                dispose();
                                ClientGui cg = new ClientGui();
                                cg.init(usr);


                            } else {
                                JOptionPane.showMessageDialog(null,"Login again", "Not Registered", JOptionPane.OK_OPTION);

                             {
                                    
                                }
                            }
                        } catch (Exception ep) {
                            ep.printStackTrace();
                        }

                    } else {
                        lb.setText("Fill all fields");
                    }

                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });

            // reg listener
            reg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // write in file
                    File f = new File("users.txt");
                    String usr = username.getText().toString().trim();
                    String pwd = pass.getText().toString().trim();
                    if (!usr.matches("") && !pwd.matches("")) {
                        // if not blank then proceed
                        //if already there prompt user
                        FileReader pr = null;
                        try {
                            pr = new FileReader(f);
                            BufferedReader br = new BufferedReader(pr);
                            String getUser = "";
                            int flag = 0;
                            while ((getUser = br.readLine()) != null) {
                                String dt = getUser.split(":")[0];
                                if (dt.matches(usr)) {
                                    //found
                                    flag = 1;
                                    break;
                                }
                            }
                            br.close();
                            pr.close();

                            if (flag == 1) {
                                // found user
                                username.setText("");
                                pass.setText("");
                                lb.setText("User  already exists!");
                            } else {
                                // register user
                                FileWriter pw = new FileWriter(f, true);
                                BufferedWriter bw = new BufferedWriter(pw);
                                bw.write(usr + ":" + pwd);
                                bw.newLine();
                                username.setText("");
                                pass.setText("");
                                lb.setText("registered! now Login.");
                                bw.close();
                                pw.close();

                            }
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        }



                    } else {
                        lb.setText("Fill all fields");
                    }
                }
            });


        }
    }
    //auth end
    // GUI

    public static class ClientGui extends JFrame implements ActionListener, WindowListener {

        JLabel hostname;
        String userkanaam;

        void init(String n) {
            userkanaam = n;
            naam = userkanaam;
//            NaamMeKya.stre = n;
            //NaamMeKya nmk = new NaamMeKya();
           // System.out.print();
           // nmk.setUsr(n);
            JPanel north = new JPanel();
            hostname = new JLabel("Enter Host name");
            stopStart = new JButton("Start");

            nameFd = new JTextField(15);
            jl = new JLabel();
            leave = new JButton("EXIT");
            //stopStart.addActionListener(this);
            north.add(hostname);
            north.add(nameFd);
            north.add(stopStart);
            north.add(jl);
            north.add(leave);
            leave.setVisible(false);
            leave.addActionListener(this);

            add(north, BorderLayout.NORTH);

            //window cross button
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (JOptionPane.showConfirmDialog(null,
                            "Are you sure to close this window?", "Really Closing?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        // remove the client from the server
                        if (os != null) {
                            os.println("");
                            System.out.println("close");
                            os.flush();
                        }
                        System.exit(0);
                    }
                }
            });



            // the event and chat room

            JPanel center = new JPanel();

            //chat = new JTextArea( 40,40);
            chat = new JTextArea(15, 40);
            Font font = new Font("Verdana", Font.BOLD, 12);
            chat.setFont(font);
            chat.setForeground(Color.BLUE);
             chat.setBorder(BorderFactory.createCompoundBorder(
        chat.getBorder(), 
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            // scrolling to the last
            DefaultCaret caret = (DefaultCaret)chat.getCaret();
caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
// add scroll
            JScrollPane scr = new JScrollPane(chat,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// Add your text area to scroll pane 
            chat.setBounds(5, 35, 385, 330);
            chat.setLineWrap(true);
            chat.setWrapStyleWord(true);
            chat.setEditable(false);
            scr.setBounds(20, 30, 100, 100);

            //JScrollPane sp = new JScrollPane(chat);


            chat.setEditable(false);
            center.add(scr);


            //center.add(new JScrollPane(chat));







            add(center, BorderLayout.CENTER);
            JPanel southPanel = new JPanel();
            mesgField = new JTextField(40);
          //  mesgField.setColumns(500);
            mesgField.setBorder(BorderFactory.createCompoundBorder(
        mesgField.getBorder(), 
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
           // mesgField.setBounds(5, 500, 300, 100);
            southPanel.add(mesgField);
            send = new JButton("send");
            southPanel.add(send);
            add(southPanel, BorderLayout.SOUTH);

            send.addActionListener(this);
            stopStart.addActionListener(this);
            jl.setText("hello");
            send.setVisible(false);

            

            // need to be informed when the user click the close button on the frame

            //addWindowListener(this);
            // setDefaultCloseOperation(EXIT_ON_CLOSE);
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            setSize(600, 400);

            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            // System.out.print("helloooo");
            Object o = e.getSource();
            if (o == stopStart) {
                // enter name
                
                String hst = nameFd.getText().trim();
                //  System.out.println("name accepted");
                if (!hst.matches("")) {
                    if (connect(hst)) {

                        jl.setText(naam + " connected");
                        stopStart.setVisible(false);
                        nameFd.setVisible(false);
                        hostname.setVisible(false);
                        send.setVisible(true);
                        leave.setVisible(true);
                       
                        
                        try {
                            
                            System.out.println("connected");
                            readPart();
                            os.println(naam);
                            os.flush();
                            

                        } catch (IOException ex) {
                            Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Not able to connect");
                    }
                }
            } else if (o == send) {
                // enter message
                String msgToSend = mesgField.getText().trim();
                System.out.println(msgToSend);
                if (msgToSend.equals("")) {
                } else {

                    String str = naam + " : " + msgToSend;
                    try {
                        os.println(EncDec.encrypt(str));
                       // os.println(str);
                    } catch (Exception ex) {
                        Logger.getLogger(GroupChatClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    os.flush();
                    chat.setForeground(Color.BLUE);
                    chat.append("me" + " : " + msgToSend + "\n");
                }
                mesgField.setText("");
                mesgField.requestFocus(true);
            } else if (o == leave) {
                //exit chat
                os.println("");
                os.flush();
                System.exit(0);
            }
            //end

        }

        @Override
        public void windowOpened(WindowEvent e) {
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowClosing(WindowEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowClosed(WindowEvent e) {
            // System.out.println("closed");
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowIconified(WindowEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowActivated(WindowEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}