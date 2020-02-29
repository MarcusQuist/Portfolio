package ClientServerManaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private  ArrayList<Socket> sockets;
    private  int portNumber;
    private ServerSocket serverSocket;

    public Server(int portNumber)  //ctrl + option + r
    {
        this.portNumber = portNumber;
    }

    public void init() throws IOException {
        sockets = new ArrayList<>();

        serverSocket = new ServerSocket(portNumber);
        System.out.println("Successfully created the publisher on port: " + portNumber);
        while(true){
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            newConnection(socket);
        }
    }

    private void newConnection(Socket subscriberSocket)
    {
        Thread thread = new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(subscriberSocket.getInputStream()));

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    communicate(inputLine, subscriberSocket);
                }
            }
            catch (IOException e)
            {
                System.out.println("Exception caught when trying to listen on port "
                        + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("Application successfully connected with the ip: " + subscriberSocket.getInetAddress());
    }
    private void communicate(String message, Socket subscriberSocket){
        for(Socket socket : sockets)
        {
            if(socket != subscriberSocket)
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

    public ServerSocket getSocket(){
        return serverSocket;
    }
}