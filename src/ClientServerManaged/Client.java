package ClientServerManaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{

    public Client(String hostName, int portNumber) throws IOException
    {

        try {
                Socket subscriber = new Socket(hostName, portNumber);
                System.out.println("Successfully connected to the channel!");
                PrintWriter writer = new PrintWriter(subscriber.getOutputStream(), true);
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

                publisherListener(subscriber);

                String userInput;
                while ((userInput = stdIn.readLine()) != null)
                {
                    //Send message to server
                    writer.println(userInput);
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
    private void publisherListener(Socket subscriber){
        Thread thread = new Thread(() -> {
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(subscriber.getInputStream()));
                while(true){
                    System.out.println(reader.readLine());
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
}
