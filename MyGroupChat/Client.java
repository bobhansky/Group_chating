package MyGroupChat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * two threads
 *      one for receiving msg
 *      one for sending msg
 */
public class Client {
    private static String  name;
    private Scanner sc = new Scanner(System.in);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param ps  OutPutStream to Service   msg sender
     */
    public void register(PrintStream ps){
        try {
            System.out.println("Please enter your name:");
            String name = sc.nextLine();
            ps.println("/nameRegister");
            ps.println(name);

            System.out.println("name is valid and Registration is done!");
            this.name = name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // 1. make connection to Server
            // local networking, then using 127.0.0.1  or localhost as IP
            // connect it to 6324 port
            Socket socket = new Socket("127.0.0.1",6324);

            // 2. get byte output stream
            OutputStream os = socket.getOutputStream();
            // wrap it to upper stream   : printStream here
            PrintStream ps = new PrintStream(os);


            // register this user
            // ask if user preferred name is registered before on Service
            Client c = new Client();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            c.register(ps);

            // allocating thread for this client
            ClientReaderThread crt = new ClientReaderThread(socket);
            crt.setName(name+" thread");
            crt.start();


            while(true){
                System.out.println("Please Enter the message you want to send:");
                String msg = c.sc.nextLine();
                ps.println(msg);
                ps.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
