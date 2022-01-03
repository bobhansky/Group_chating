package MyGroupChat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerReaderRunnable implements Runnable {
    private Socket socket = new Socket();
    private List<Socket> socketList = new ArrayList<>();
    private String name;

    public ServerReaderRunnable(Socket socket, List<Socket> socketList) {
        this.socket = socket;
        this.socketList = socketList;
    }

    /**
     *  receiving from socket and sending msg to all sockets
     */
    @Override
    public void run() {
        try {
            // get inputStream
            InputStream is = socket.getInputStream();
            // wrap
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String msg;
            while((msg = br.readLine()) != null){
                // check if this is register request
                if(msg.equals("/nameRegister")){
                    msg = br.readLine();
                    name = msg;
                }
                else{  // not register request
                    System.out.println("["+ new Date(System.currentTimeMillis()).toString() +"] " + socket.getRemoteSocketAddress() +": "+ msg);
                    sendToAll(msg);
                }
            }
        } catch (Exception e) {
            System.out.println("["+ new Date(System.currentTimeMillis()).toString() +"] " +
                    socket.getRemoteSocketAddress() +" is offline");
            socketList.remove(socket);
        }
    }

    private void sendToAll(String msg) {
        for(Socket s : socketList){
            try {
                PrintStream ps = new PrintStream(s.getOutputStream());
                ps.println("["+ new Date(System.currentTimeMillis()).toString() +"] " +
                        name+"("+socket.getRemoteSocketAddress()+"): "+ msg );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
