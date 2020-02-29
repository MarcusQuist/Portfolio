package PublishSubscribeBroker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Publisher
{
    private static Socket socket;
    public static void main(String[] args) throws IOException
    {
        if (args.length != 2)
        {
            System.err.println("Usage: Please specify an IP + port to connect to");
            System.exit(1);
        }
        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        createSocket(ip, port);
        System.out.println("Successfully connected to: " + ip + ": " + port);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("publisher");

        String userInput;
        while ((userInput = reader.readLine()) != null)
        {
            writer.println(userInput);
        }
    }

    private static void createSocket(String ip, int port)
    {
        try
        {
            socket = new Socket(ip, port);
        }
        catch (UnknownHostException e)
        {
            System.err.println("Unknown host: " + ip);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " +
                    "" +
                    ip);
            System.exit(1);
        }
    }
}
