package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private int port;

    private ExecutorService executor = Executors.newCachedThreadPool();


    public Server(int port) {
        this.port = port;
    }

    public void run() {

            try {
                ServerSocket serverSocket = new ServerSocket(port);
                while (true) {
                    try {
                        Connection connection = new Connection(serverSocket.accept());
                        executor.submit(connection);
                    } catch (IOException e) {
                        System.out.println("Error starting session: " + e.getLocalizedMessage());
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error starting server: " + e.getLocalizedMessage());
            } finally {
                executor.shutdownNow();
            }

    }
}

