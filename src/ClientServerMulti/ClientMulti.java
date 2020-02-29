package ClientServerMulti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMulti
{
    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.err.println(
                    "Please specify both an ip and port as the arguments");
            System.exit(1);
        }
        String hostName = args[0];
        int port = Integer.parseInt(args[1]);

        try
        {
            Socket socket = new Socket(hostName, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            startServerListener(socket);

            String userInput;
            while ((userInput = stdIn.readLine()) != null)
            {
                out.println(userInput);
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    private static void startServerListener(Socket socket) {
        Thread serverListenerThread = new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true)
                {
                    System.out.println(in.readLine());
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        serverListenerThread.setDaemon(true);
        serverListenerThread.start();
    }
}
