package UDP;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client
{
    private static int localPort;
    private static InetAddress ip;
    private static DatagramSocket socket;

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("Usage: java Client <receiverPort number>");
            System.exit(1);
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Please write a port to create the client:");
        getIp();
        int receiverPort = Integer.parseInt(args[0]);

        createDatagramSocket();

        byte[] buf = null;

        while(true)
        {
            String input = sc.nextLine();
            buf = input.getBytes();

            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, receiverPort);

            sendPacket(packet);
        }
    }

    private static void getIp()
    {
        ip = null;
        try
        {
            ip = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    private static void createDatagramSocket()
    {
        socket = null;
        try
        {
            socket = new DatagramSocket(localPort, ip);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    private static void sendPacket(DatagramPacket packet)
    {
        try
        {
            socket.send(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
