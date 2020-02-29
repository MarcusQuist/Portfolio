package P2P;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PingPongThread extends Thread
{
    private Socket socket;
    private Peer peer;
    private boolean shouldExecute = true;
    private boolean shouldInitPingPong = false;

    public PingPongThread(Socket socket, Peer peer, boolean shouldInitPingPong)
    {
        this.socket = socket;
        this.peer = peer;
        this.shouldInitPingPong = shouldInitPingPong;
    }

    @Override
    public void run()
    {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try
        {
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (shouldInitPingPong)
        {
            writer.println(".");
        }
        else
        {
            writer.println("ackPingPong()");
        }

        while (shouldExecute)
        {
            try
            {
                String message = reader.readLine();
                Thread.sleep(600);
                if (message == null)
                {
                    return;
                }
                if (message.equals("."))
                {
                    writer.println(".");
                }
            }
            catch (InterruptedException | IOException e)
            {
                if (shouldHandleCrash())
                {
                    peer.getNeighbourSystem().secondNeighbourCrashHandling(peer);
                    System.out.println("Second neighbour crashed");
                    break;
                }
            }
        }
    }
    private boolean shouldHandleCrash() {
        return !shouldInitPingPong;
    }
    public void setShouldExecute(boolean value) {
        shouldExecute = value;
    }
    public Socket getSocket() {
        return socket;
    }
}
