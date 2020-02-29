package UDP_Reliable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            System.err.println("Usage: java Client <localPort number>, <receiverPort number>");
            System.exit(1);
        }

        localPort = Integer.parseInt(args[0]);
        getIp();
        int receiverPort = Integer.parseInt(args[1]);

        createDatagramSocket();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Successfully created - Ready to send message!");
        try
        {
            while (true)
            {
                String input = reader.readLine();
                byte[] bytesSent = input.getBytes();
                DatagramPacket packetSent = new DatagramPacket(bytesSent, bytesSent.length, ip, receiverPort);
                socket.send(packetSent);

                byte[] bytesReceived = new byte[10000];
                DatagramPacket packetReceived = new DatagramPacket(bytesReceived, bytesReceived.length);

                try
                {
                    socket.receive(packetReceived);
                    String receivedString = new String(packetReceived.getData(), 0, packetReceived.getLength());

                    if (receivedString.equalsIgnoreCase(input))
                    {
                        System.out.println("Successfully sent and received the message: " + receivedString);
                    }
                    else
                    {
                        System.out.println("Failed to retrieve message - sending again...");
                        socket.send(packetSent);
                    }
                }
                catch (SocketTimeoutException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
            System.exit(1);
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

    private static void createDatagramSocket(){
        socket = null;
        try
        {
            socket = new DatagramSocket();
            socket.setSoTimeout(10000);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
