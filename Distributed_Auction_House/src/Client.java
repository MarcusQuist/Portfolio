import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Client
{
    private static Socket serverConnectionSocket;
    private static String ip;
    private static String backupServerIp;
    private static int port;
    private static BufferedReader reader;
    private static PrintWriter writer;

    public static void main(String[] args) {
        if (args.length != 3)
        {
            System.err.println("Please specify both an ip port of the server to connect to and backup server ip as arguments");
            System.exit(1);
        }
        ip = args[0];
        port = Integer.parseInt(args[1]);
        backupServerIp = args[2];

        System.out.println("Welcome to the auction house system!");
        System.out.println("Trying to connect to the auction house..");
        connectToServer(true);
    }

    private static void connectToServer(boolean connectToPrimary)
    {
        try
        {
            if(connectToPrimary)
            {
                serverConnectionSocket = new Socket(ip, port);
            }
            else
            {
                serverConnectionSocket = new Socket(backupServerIp, 5001);
            }
            serverConnectionSocket.setSoTimeout(1500);
            reader = new BufferedReader(new InputStreamReader(serverConnectionSocket.getInputStream()));
            writer = new PrintWriter(serverConnectionSocket.getOutputStream(), true);
            System.out.println("Successfully connected!");
            handleUserInput();
        }
        catch (IOException e)
        {
            System.out.println("Attempting to connect to backup server");
            connectToServer(false);
        }
    }

    private static void waitForServerResponse(String message) throws IOException
    {
        try
        {
            String response = reader.readLine();
            System.out.println(response);
            handleUserInput();
        }
        catch (SocketTimeoutException e)
        {
            // Server crashed
            System.out.println("Server crashed");
            try
            {
                //serverConnectionSocket.close();
                System.out.println("Closed connection to server");
                serverCrashHandling(message);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private static void handleUserInput() throws IOException {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            System.out.println("Type 'bid (amount)' to place a bid on the current auction");
            System.out.println("Type 'result' to get the highest bid on the given auction");
            String input = sc.nextLine();
            String[] filteredInput = input.split(" ");
            String method = filteredInput[0];
            int value;
            String message = null;
            try
            {
                if(method.equalsIgnoreCase("bid"))
                {
                    value = Integer.parseInt(filteredInput[1]);
                    System.out.println("Bidding on the current auction with: " + value);
                    message = "Bid " + value;
                    writer.println(message);
                    waitForServerResponse(message);
                }
                if(method.equalsIgnoreCase("result"))
                {
                    System.out.println("Getting result of the current active auction");
                    message = "Result";
                    writer.println(message);
                    waitForServerResponse(message);
                }
            }
            catch(SocketException e)
            {
                System.out.println("Server has crashed, attempting to reconnect");
                serverCrashHandling(message);
            }
            catch(NumberFormatException e)
            {
                System.err.println("Error: Invalid input, please specify an integer");
            }
            catch (Exception e)
            {
                System.err.println("Error: Invalid input");
            }
        }
    }

    private static void serverCrashHandling(String message) throws IOException
    {
        serverConnectionSocket = new Socket(backupServerIp, 5001);
        writer = new PrintWriter(serverConnectionSocket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(serverConnectionSocket.getInputStream()));
        System.out.println("Successfully connected!");
        writer.println(message);
        waitForServerResponse(message);
    }
}
