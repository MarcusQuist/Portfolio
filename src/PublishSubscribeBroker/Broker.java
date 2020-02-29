package PublishSubscribeBroker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Broker {
    private static ArrayList<Socket> publishers;
    private static ArrayList<Socket> subscribers;
    private static int port;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException
    {
        publishers = new ArrayList<>();
        subscribers = new ArrayList<>();

        if (args.length != 1)
        {
            System.err.println("Incorrect usage, please enter a port number as argument");
            System.exit(1);
        }
        port = Integer.parseInt(args[0]);
        serverSocket = new ServerSocket(port);
        System.out.println("Successfully created the broker on port: " + port);

        checkForNewConnections();

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = stdIn.readLine()) != null)
        {
            communicate(input);
        }
    }

    private static void checkForNewConnections()
    {
        Thread thread = new Thread(() -> {
            while(true)
            {
                Socket socket = null;
                try
                {
                    socket = serverSocket.accept();
                    handleRequest(socket);
                }
                catch (IOException e)
                {
                    System.out.println("Failed to accept the node!");
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static void handleRequest(Socket requester)
    {
        Thread thread = new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(requester.getInputStream()));
                String inputLine = reader.readLine();
                if(inputLine.equalsIgnoreCase("subscriber"))
                {
                    System.out.println("A subscriber joined as: " + requester.getPort());
                    subscribers.add(requester);
                }
                if(inputLine.equalsIgnoreCase("publisher"))
                {
                    System.out.println("A publisher joined as: " + requester.getPort());
                    while ((inputLine = reader.readLine()) != null)
                    {
                        communicate(inputLine);
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println("Exception caught when trying to listen on port " + port);
                System.out.println(e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("Application successfully connected with the ip: " + requester.getInetAddress());
    }
    private static void communicate(String message){
        for(Socket socket : subscribers)
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
