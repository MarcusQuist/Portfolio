package P2P;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client
{
    private static Peer peer;

    public static void main(String[] args) throws UnknownHostException
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the P2P network!");
        System.out.println("Would you like to create a new network? (y/n)");
        while (true)
        {
            String answer = sc.nextLine();
            if (answer.equals("y"))
            {
                createNewNetwork(sc);
                break;
            }
            if (answer.equals("n"))
            {
                connectToExistingNetwork(sc);
                break;
            }
            System.out.println("Wrong input! Please Try again (y/n)");
        }

        Thread t = new Thread (() -> peer.init());
        t.start();

        handleUserInputForPeer();
    }

    private static void createNewNetwork(Scanner sc)
    {
        System.out.println("Creation of a new network");
        int port = 5000;
        peer = new Peer(port);
        System.out.println("Network is up and running!");
    }

    private static void connectToExistingNetwork(Scanner sc) throws UnknownHostException {
        System.out.println("Connecting to an existing network..");
        while (true)
        {
            System.out.println("Enter a port for your peer:");
            int portOfPeer = Integer.parseInt(sc.nextLine());
            String ip = "localhost";
            int port = 5000;
            System.out.println("Connecting to network..");
            try
            {
                connectToPeer(ip, port, portOfPeer);
                System.out.println("Successfully connected to network!");
                return;
            }
            catch (IOException e)
            {
                System.out.println("Error: failed connecting to peer!");
                continue;
            }
        }
    }
    private static void connectToPeer(String ip, int port, int portOfPeer) throws IOException
    {
        Socket socket = new Socket(ip, port);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        peer = new Peer(portOfPeer);
        writer.println("insertNewPeer()," + portOfPeer);
    }

    private static void handleUserInputForPeer(){
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            System.out.println("Type 'get' to get the current count of a peer");
            System.out.println("Type 'increment' to increment the count of the ring");
            System.out.println("Type 'status' to get the network status");
            String input = sc.nextLine();
            try
            {
                if (input.equalsIgnoreCase("get"))
                {
                    System.out.println("Current count of the network: " + peer.getCount());
                }
                if (input.equalsIgnoreCase("increment"))
                {
                    System.out.println("Incrementing count..");
                    peer.initIncrementCount();
                    System.out.println("Success!");
                    System.out.println("Network count is now: " + peer.getCount());
                }
                if (input.equalsIgnoreCase("status"))
                {
                    peer.initGetNetworkStatus(peer.getPort());
                }
            }
            catch (Exception e)
            {
                System.err.println("ERROR: Invalid input!");
            }
        }
    }
}
