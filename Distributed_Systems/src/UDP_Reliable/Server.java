package UDP_Reliable;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class Server
{
    private static DatagramSocket socket;
    private static InetAddress ip;
    private static int port;

    private static Set receivedStrings;

    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            System.err.println("Usage: java server <port number>");
            System.exit(1);
        }
        port = Integer.parseInt(args[0]);
        getIp();

        createDatagramSocket();

        receivedStrings = new HashSet<String>();
        byte[] received = new byte[10000];
        DatagramPacket packet = new DatagramPacket(received, received.length);
        while(true)
        {
            receivePacket(packet, received);
            String receivedString = new String(packet.getData(), 0, packet.getLength());
            if(!receivedStrings.contains(receivedString)) // Hashing to fix the issue of duplicate messages
            {
                receivedStrings.add(receivedString);
                sendPacket(packet);
            }
            else
            {
                System.out.println("Already sent the message back to the client...");
            }

            if(convert(received).toString().equalsIgnoreCase("disconnect"))
            {
                System.out.println("Closing connection..");
                break;
            }

            received = new byte[10000];
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
            socket = new DatagramSocket(port, ip);
            System.out.println("Successfully created UDP Echo server on: " + ip + ": " + port);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void receivePacket(DatagramPacket packet, byte[] received)
    {
        try
        {
            socket.receive(packet);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Client sent: " + convert(received));
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

    private static StringBuilder convert(byte[] array)
    {
        if(array == null) return null;
        StringBuilder sb = new StringBuilder();
        for (byte b : array)
        {
            sb.append((char) b);
        }
        return sb;
    }
}
