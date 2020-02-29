package P2P;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
public class Demo
{
    private static int count = 5001;
    private static ArrayList<Peer> peers;

    public static void main(String[] args)
    {
        peers = new ArrayList<>();
        try
        {
            Peer master = new Peer(5000);
            startPeer(master);
            peers.add(master);

            for (int i = 0; i < 3; i++)
            {
                addPeer();
                Thread.sleep(1000);
            }

            for (Peer peer : peers)
            {
                System.out.println("Peer " + peer.getPort() + " has clockwise neighbours: " + "-1: " + peer.getPriorNeighbour().getPort() + " 1: " + peer.getFirstNeighbour().getPort() + " 2: " + peer.getSecondNeighbour().getPort());
            }
            master.initIncrementCount();
            master.initIncrementCount();
            master.initIncrementCount();
            for (Peer peer : peers)
            {
                System.out.println("Peer " + peer.getPort() + " has count " + peer.getCount());
            }
            System.exit(1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private static void addPeer() throws IOException {
        Peer peer = new Peer(count);
        Socket socket = new Socket("localhost", 5000);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("insertNewPeer()," + count);
        startPeer(peer);
        peers.add(peer);
        count++;
    }

    public static void startPeer(Peer peer)
    {
        Thread task = new Thread(peer::init);
        task.start();
    }
}
