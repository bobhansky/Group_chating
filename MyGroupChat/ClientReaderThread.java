package MyGroupChat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReaderThread extends Thread{

    private Socket socket;

    public ClientReaderThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // get inputStream
            InputStream is = socket.getInputStream();
            // wrap
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String msg;
            while((msg = br.readLine()) != null){
                System.out.println(msg);
            }
        } catch (Exception e) {
            System.out.println("Server kicks you out");
        }
    }
}
