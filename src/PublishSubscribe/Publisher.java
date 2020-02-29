package PublishSubscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Publisher {
    private static ArrayList<Socket> sockets;
    private static int port;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException  //ctrl + option + r
    {
        sockets = new ArrayList<>();

        if (args.length != 1) {
            System.err.println("Incorrect usage, please enter a port number as arguments");
            System.exit(1);
        }
        port = Integer.parseInt(args[0]);
        serverSocket = new ServerSocket(port);
        System.out.println("Successfully created the publisher on port: " + port);

        checkForNewConnectionsThread();

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = stdIn.readLine()) != null)
        {
            communicate(input);
        }
    }

    private static void checkForNewConnectionsThread()
    {
        Thread thread = new Thread(() -> {
            while(true)
            {
                Socket socket = null;
                try
                {
                    socket = serverSocket.accept();
                }
                catch (IOException e)
                {
                    System.out.println("Failed to accept the subscriber!");
                    e.printStackTrace();
                }
                sockets.add(socket);
                newConnectionThread(socket);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static void newConnectionThread(Socket subscriberSocket)
    {
        Thread thread = new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(subscriberSocket.getInputStream()));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    communicate(inputLine);
                }
            }
            catch (IOException e)
            {
                System.out.println("Exception caught when trying to listen on port "
                        + port + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("Application successfully connected with the ip: " + subscriberSocket.getInetAddress());
    }
    private static void communicate(String message){
        for(Socket socket : sockets)
        {
            try
            {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(message);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}