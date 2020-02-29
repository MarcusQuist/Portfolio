import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Server
{
    private static int highestBid;
    private static HighestBidder highestBidder;
    private static boolean auctionEnded;
    private static int time = 60000;
    private static boolean primaryServer;
    private static String primaryServerIp;
    private static int primaryServerPort;
    private static Socket primaryServerSocket;
    private static Socket backupServerSocket;


    public static void main(String[] args) throws IOException
    {
        if (args.length != 2)
        {
            System.err.println("Error: please write a valid port and boolean as the arguments");
            System.exit(1);
        }
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("my ip: "+ ip);
        int port = Integer.parseInt(args[0]);
        primaryServer = Boolean.valueOf(args[1]);
        ServerSocket serverSocket = new ServerSocket(port);
        if(primaryServer) System.out.println("Successfully created the primary server!");
        else System.out.println("Successfully created the backup server!");

        if(primaryServer)
        {
            initTimer();
        }

        if(!primaryServer)
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("Please write the ip of the primary server to connect to:");
            primaryServerIp = sc.nextLine();
            primaryServerPort = 5000;
            primaryServerSocket = new Socket(primaryServerIp, primaryServerPort);

            PrintWriter primaryServerWriter = new PrintWriter(primaryServerSocket.getOutputStream(), true);
            primaryServerWriter.println("initBackupConnection " + ip);

            if(!primaryServer)
            {
                String message = "connectToBackupServer " + port;
                PrintWriter clientWriter = new PrintWriter(primaryServerSocket.getOutputStream(), true);
                clientWriter.println(message);
            }
        }

        while(true)
        {
            Socket socket = null;
            try
            {
                socket = serverSocket.accept();
            }
            catch (IOException e)
            {
                System.out.println("Error: Failed to accept the client connecting");
                e.printStackTrace();
            }
            createNewSessionThread(socket);
        }
    }

    private static void createNewSessionThread(Socket socket)
    {
        Thread thread = new Thread(() -> {
            try {
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientMessage;
                while ((clientMessage = clientReader.readLine()) != null)
                {
                    String[] filteredClientMessage = clientMessage.split(" ");
                    String method = filteredClientMessage[0];
                    if(method.equalsIgnoreCase("bid"))
                    {
                        int value = Integer.parseInt(filteredClientMessage[1]);
                        bid(socket, value);
                    }
                    if(method.equalsIgnoreCase("result"))
                    {
                        result(socket);
                    }
                    if(method.equalsIgnoreCase("connectToBackupServer"))
                    {
                        int backupServerPort = Integer.parseInt(filteredClientMessage[1]);
                        backupServerSocket = new Socket("localhost", backupServerPort);
                        System.out.println("Established connection to backup");
                    }
                    if(method.equalsIgnoreCase("setHighestBid"))
                    {
                        int newHighestBid = Integer.parseInt(filteredClientMessage[1]);
                        highestBidder = new HighestBidder(filteredClientMessage[2], Integer.parseInt(filteredClientMessage[3]));
                        System.out.println("New highest bidder: " + highestBidder.getIp() + ": " + highestBidder.getPort());
                        setHighestBid(newHighestBid);
                    }
                    if(method.equalsIgnoreCase("initBackupConnection"))
                    {
                        String ip = filteredClientMessage[1];
                        initBackupConnection(ip);
                    }
                    if(method.equalsIgnoreCase("setTime"))
                    {
                        time =  Integer.parseInt(filteredClientMessage[1]);
                        initTimer();
                    }
                }
            }
            catch (IOException e)
            {
                if(primaryServer)
                {
                    System.out.println("Backup server crashed!");
                }
                else
                {
                    System.out.println("Primary server crashed!");
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("Successfully created session with " + socket.getRemoteSocketAddress().toString() + ": " + socket.getPort());
    }

    private static void broadcast(Socket from, String message) throws IOException
    {
        PrintWriter clientWriter = new PrintWriter(from.getOutputStream(), true);
        if(!auctionEnded)
        {
            clientWriter.println("Server: " + message);
        }
        else
        {
            clientWriter.println("Server: The auction has already ended with the highest bid of: " + highestBid);
        }
    }

    private static void broadcastToServer(Socket from, String message) throws IOException
    {
        PrintWriter clientWriter = new PrintWriter(from.getOutputStream(), true);
        clientWriter.println(message);
    }

    private static synchronized void bid(Socket socket, int bid) throws IOException
    {
        String response;
        if(!auctionEnded)
        {
            if(bid > highestBid)
            {
                highestBid = bid;

                response = "Bid accepted, your bid of " + bid + " is now the highest.";
                if(backupServerSocket != null && !backupServerSocket.isClosed())
                {
                    broadcastToServer(backupServerSocket, "setHighestBid " + highestBid + " " + socket.getInetAddress().getHostAddress() + " " + socket.getPort());
                }
                highestBidder = new HighestBidder(socket.getInetAddress().getHostAddress(), socket.getPort());
                System.out.println("Highest bidder: " + socket.getInetAddress().getHostAddress() + ": " + socket.getPort());
                broadcast(socket, response);
                return;
            }
            if(bid == highestBid)
            {
                response = "Bid failed, your bid of " + bid + " is equal to the current highest of " + highestBid;
                broadcast(socket, response);
            }
            else
            {
                response = "Bid failed, your bid of " + bid + " is lower than the current highest of " + highestBid;
                broadcast(socket, response);
            }
        }
        else
        {
            response = "Server: The auction has already ended with the highest bid of: " + highestBid;
            broadcast(socket, response);
        }
    }

    private static void result(Socket socket) throws IOException
    {
        String response;
        if(!auctionEnded)
        {
            response = "The current active auction has the highest bid of: " + highestBid;
            broadcast(socket, response);
        }
        else
        {
            response = "Server: The auction has already ended with the highest bid of: " + highestBid;
            broadcast(socket, response);
        }
    }

    private static void setHighestBid(int bid)
    {
        if(bid > highestBid)
        {
            highestBid = bid;
            System.out.println("Updated highest bid to: " + highestBid);
        }
    }

    private static void initBackupConnection(String ip) throws IOException
    {
        int backupServerPort = 5001;
        backupServerSocket = new Socket(ip, backupServerPort);
        String message = "setTime " + time;
        broadcastToServer(backupServerSocket, message);
    }

    private static void initTimer()
    {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if(time != 0)
                {
                    time -= 1000;
                    if(time == 0) auctionEnded = true;
                }
            }
        }, 0,1000);
    }
}