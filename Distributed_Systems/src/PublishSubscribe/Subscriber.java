package PublishSubscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Subscriber
{
    private static Socket socket;
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        try
        {
            socket = new Socket(ip, port);
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + ip);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " +
                    "" +
                    ip);
            System.exit(1);
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true){
                System.out.println(reader.readLine());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
