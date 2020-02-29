package ClientServerMulti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class ServerMulti {
    private static ArrayList<Socket> sockets;

    public static void main(String[] args) throws IOException
    {
        sockets = new ArrayList<>();

        if (args.length != 1)
        {
            System.err.println("Error: please write a valid port in as the argument");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Successfully created the server!");
        while(true)
        {
            Socket socket = null;
            try
            {
                socket = serverSocket.accept();
            }
            catch (IOException e)
            {
                System.out.println("Failed to accept the client!");
                e.printStackTrace();
            }
            sockets.add(socket);
            createNewSessionThread(socket);
        }
    }

    private static void createNewSessionThread(Socket socket) {
        Thread thread = new Thread(() -> {
            try {
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter clientWriter = new PrintWriter(socket.getOutputStream(), true);
                clientWriter.println("Welcome to the server");
                clientWriter.println("Please enter your username to continue");
                String username = clientReader.readLine();
                clientWriter.println("Username successfully chosen!");
                clientWriter.println("You can now write messages:");
                broadCast(socket, username, "joined the channel!");
                String clientMessage;
                while ((clientMessage = clientReader.readLine()) != null)
                {
                    broadCast(socket, username, clientMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("Successfully created session with " + socket.getRemoteSocketAddress().toString());
    }

    private static void broadCast(Socket from, String username, String message) throws IOException {
        Iterator<Socket> it = sockets.iterator();
        while (it.hasNext()) {
            Socket cur = it.next();
            if (cur == null) it.remove();
            if (cur == from) continue;
            PrintWriter clientWriter = new PrintWriter(cur.getOutputStream(), true);
            clientWriter.println(formatTime() + " | " + username + ": " + message);
        }
    }

    private static String formatTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

}