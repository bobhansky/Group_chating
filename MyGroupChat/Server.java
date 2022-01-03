package MyGroupChat;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class Server {
    // store the socket to different Client
    private static final List<Socket> socketList = new ArrayList<>();
    // Thread Pool
    // no waiting queue
    private static ExecutorService pool = new ThreadPoolExecutor(20,20,
            6, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        // main thread's job is getting connection from client and allocate new thread
        // to each Client

        try {
            System.out.println("Server Starts");
            // serverSocket
            ServerSocket serverSocket = new ServerSocket(6324);

            // getting connection from client and allocate new thread
            // to each Client
            while(true){
                Socket socket = serverSocket.accept();
                socketList.add(socket);
                System.out.println("["+ new Date(System.currentTimeMillis()).toString() +"] " +
                        socket.getRemoteSocketAddress() + " is online now." );
                // everytime get a client-Server pipeline, hand it to an independent thread
                pool.execute(new ServerReaderRunnable(socket,socketList));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            System.out.println("Server closed");
        }
    }
}
