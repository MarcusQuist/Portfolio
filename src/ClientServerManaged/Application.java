package ClientServerManaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
// CountDownLatch is a synchronization aid that allows one or more threads
// to wait until a set of operations being performed in other threads completes.
import java.util.concurrent.CountDownLatch;

public class Application
{
    private static Socket administratorSocket;
    private static Scanner sc;
    private static Administrator administrator;
    private static int administratorPort = 7070;
    private static ArrayList<String> channels;
    private static CountDownLatch latch;
    private static int channelPort;

    public static void main(String[] args) throws IOException {
        latch = new CountDownLatch(1);
        sc = new Scanner(System.in);
        channels = new ArrayList<>();
        System.out.println("Welcome to the network between Publishers and Subscribers!");
        if(!administratorHeartbeat())
        {
            System.out.println("No central server found, creating one..");
            try{
                administrator = new Administrator(administratorPort);
            }
            catch(Exception e) // numberFormatException? -- check console
            {
                e.printStackTrace();
            }
        }
        initAdministratorSocket();
        initAdministratorReader(administratorSocket);
        getChannels();
        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        System.out.println("Write a channel name to join the channel, or write 'create' to create a new channel");

        while(true)
        {
            String line = sc.nextLine();
            for (String channel : channels)
            {
                if (line.equalsIgnoreCase(channel))
                {
                    System.out.println("Found the given channel!");
                    latch = new CountDownLatch(1);
                    getPort(channel);
                    try
                    {
                        System.out.println("Connecting..");
                        latch.await();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    new Client("localhost", channelPort);
                }
            }
            if(line.equalsIgnoreCase("create"))
            {
                System.out.println("Write a unique name for your new channel");
                line = sc.nextLine();
                {
                    String channelName = line;
                    System.out.println("Write a port for your new channel");
                    int port = Integer.parseInt(sc.nextLine());
                    Server server = new Server(port);
                    initPublisher(server);
                    addChannel(port, channelName);
                }
            }
        }
    }

    private static void initPublisher(Server server){
        Thread thread = new Thread(() -> {
            try{
                server.init();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private static boolean administratorHeartbeat()
    {
        try
        {
            administratorSocket = new Socket("localhost", administratorPort);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
    private static void initAdministratorSocket()
    {
        try {
            administratorSocket = new Socket("localhost", administratorPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Successfully connected to the central server!");

    }

    private static void initAdministratorReader(Socket socket){
         Thread thread = new Thread(() -> {
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = reader.readLine()) != null)
                {
                    if(!message.contains("%"))
                    {
                        if(message.equals("Port"))
                        {
                            String port = reader.readLine();
                            channelPort = Integer.parseInt(port);
                            latch.countDown();
                        }
                        if(message.equals("The following channels exist:"))
                        {
                            System.out.println(message);
                            while((message = reader.readLine()) != null)
                            {
                                if(!message.equals("No more channels"))
                                {
                                    System.out.println(message);
                                    channels.add(message);
                                }
                                else
                                {
                                    latch.countDown();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    private static void getChannels() throws IOException
    {
        PrintWriter writer = new PrintWriter(administratorSocket.getOutputStream(), true);
        channels = new ArrayList<>();
        writer.println("getChannels()");
    }

    private static void getPort(String channel) throws IOException {
        PrintWriter writer = new PrintWriter(administratorSocket.getOutputStream(), true);
        writer.println("getPort(),"+channel);
    }

    private static void addChannel(int port, String channelName) throws IOException {
        PrintWriter writer = new PrintWriter(administratorSocket.getOutputStream(), true);
        writer.println("addChannel()," + port + "," + channelName);
    }
}
