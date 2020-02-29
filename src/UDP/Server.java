package UDP;

import java.io.IOException;
import java.net.*;

public class Server
{
    private static DatagramSocket socket;
    private static InetAddress ip;
    private static int port;

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

        byte[] received = new byte[65535];
        DatagramPacket packet = null;

        while(true)
        {
            packet = new DatagramPacket(received, received.length);
            receivePacket(packet, received);

            if(convert(received).toString().equalsIgnoreCase("disconnect"))
            {
                System.out.println("Closing connection..");
                break;
            }

            received = new byte[65535];
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
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        System.out.println("Successfully created the server!");
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
