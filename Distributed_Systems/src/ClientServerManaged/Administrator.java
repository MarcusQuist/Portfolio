package ClientServerManaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Administrator
{
    private  int portNumber;
    private ServerSocket serverSocket;

    private HashMap<Integer, String> portChannelMap;
    private HashMap<String, Integer> channelPortMap;

    public Administrator(int portNumber) throws IOException {
        portChannelMap = new HashMap<>();
        channelPortMap = new HashMap<>();

        this.portNumber = portNumber;

        serverSocket = new ServerSocket(portNumber);
        System.out.println("Successfully created the database server on port: " + portNumber);
        newConnectionListener();
    }

    private void newConnectionListener()
    {
        Thread thread = new Thread(() -> {
            try {

                while(true)
                {
                    Socket socket = serverSocket.accept();
                    newConnection(socket);
                }
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void newConnection(Socket publisherSocket)
    {
        Thread thread = new Thread(() -> {
            try {
                PrintWriter writer = new PrintWriter(publisherSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(publisherSocket.getInputStream()));

                while (true) {
                    String[] command = reader.readLine().split(",");
                    translateMessage(command, writer);
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
    }

    private int getPort(String channel){
        return channelPortMap.get(channel);
    }

    public String[] getChannels(){
        StringBuilder str = new StringBuilder();
        channelPortMap.forEach((key, value) -> str.append(key + ","));
        String[] array = str.toString().split(",");
        return array;
    }

    public void addChannel(int port, String channelName){
        portChannelMap.put(port, channelName);
        channelPortMap.put(channelName, port);
    }

    private void translateMessage(String[] command, PrintWriter writer){
        switch(command[0])
        {
            case "getChannels()":
                String[] channels = getChannels();
                writer.println("The following channels exist:");
                for(String s : channels)
                {
                    writer.println(s);
                }
                writer.println("No more channels");
                return;
            case "addChannel()":
                addChannel(Integer.parseInt(command[1]), command[2]);
                return;
            case "getPort()":
                int port = getPort(command[1]);
                writer.println("Port");
                writer.println(port);
        }
    }
}