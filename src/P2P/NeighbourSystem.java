package P2P;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class NeighbourSystem
{
    private Peer peer;

    public NeighbourSystem(Peer peer)
    {
        this.peer = peer;
    }

    public void insertNewPeer(String ip, int port) throws IOException
    {
        if(peer.getFirstNeighbour() == null)
        {
            System.out.println("ONLY 1 EXISTS");
            Neighbour newNeighbour = new Neighbour(ip, port);
            peer.setFirstNeighbour(newNeighbour);
            peer.setPriorNeighbour(newNeighbour);
            sendMessage(ip, port, "setFirstNeighbour()," + peer.getIp() + "," + peer.getPort());
            sendMessage(ip, port, "setPriorNeighbour()," + peer.getIp() + "," + peer.getPort());
            System.out.println("Success!");
            return;
        }
        if(peer.getSecondNeighbour() == null)
        {
            System.out.println("ONLY 2 EXISTS");
            Neighbour newNeighbour = new Neighbour(ip, port);
            Neighbour firstNeighbour = peer.getFirstNeighbour();
            sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "setPriorNeighbour()," + ip + "," + port);
            sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "setSecondNeighbour()," + ip + "," + port);
            sendMessage(newNeighbour.getIp(), newNeighbour.getPort(), "setPriorNeighbour()," + peer.getIp()+ "," + peer.getPort());
            sendMessage(newNeighbour.getIp(), newNeighbour.getPort(), "setFirstNeighbour()," + firstNeighbour.getIp()+ "," + firstNeighbour.getPort());
            sendMessage(newNeighbour.getIp(), newNeighbour.getPort(), "setSecondNeighbour()," + peer.getIp()+ "," + peer.getPort());

            peer.setSecondNeighbour(peer.getFirstNeighbour());

            peer.setFirstNeighbour(newNeighbour);

           peer.requestPingPong();

            System.out.println("Success!");
            return;
        }
        else
        {
            System.out.println("MORE THAN 2 EXISTS");
            Neighbour newNeighbour = new Neighbour(ip, port);
            sendMessage(newNeighbour.getIp(), newNeighbour.getPort(), "setPriorNeighbour()," + peer.getIp()+ "," + peer.getPort());
            Neighbour firstNeighbour = peer.getFirstNeighbour();
            sendMessage(newNeighbour.getIp(), newNeighbour.getPort(), "setFirstNeighbour()," + firstNeighbour.getIp()+ "," + firstNeighbour.getPort());
            Neighbour secondNeighbour = peer.getSecondNeighbour();
            sendMessage(newNeighbour.getIp(), newNeighbour.getPort(), "setSecondNeighbour()," + secondNeighbour.getIp()+ "," + secondNeighbour.getPort());
            peer.setSecondNeighbour(peer.getFirstNeighbour());
            peer.setFirstNeighbour(newNeighbour);
            sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "setPriorNeighbour()," + newNeighbour.getIp()+ "," + newNeighbour.getPort());
            sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "setPriorNeighbour()," + newNeighbour.getIp()+ "," + newNeighbour.getPort());
            Neighbour priorNeighbour = peer.getPriorNeighbour();
            sendMessage(priorNeighbour.getIp(), priorNeighbour.getPort(), "setSecondNeighbour()," + newNeighbour.getIp()+ "," + newNeighbour.getPort());
            getCurrentCountOfNetwork();
            System.out.println("Success!");
        }
    }
    public void secondNeighbourCrashHandling(Peer peer)
    {
        System.out.println("MY SECOND NEIGHBOUR CRASHED!");
        System.out.println("I am: " + peer.getPort());
        System.out.println("My second neighbour is: " + peer.getSecondNeighbour().getPort());
        Neighbour firstNeighbour = peer.getFirstNeighbour();
        sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "getNewSecondNeighbourOnCrash()," + peer.getIp() + "," + peer.getPort());
        sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "setFirstNeighbourToSecond()");
        sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "getFirstNeighbourOfSecondNeighbour()");
        sendMessage(peer.getFirstNeighbour().getIp(), peer.getFirstNeighbour().getPort(), "setThisToPriorOfSecondNeighbour(),");
    }

    private void getCurrentCountOfNetwork()
    {
        sendMessage(peer.getSecondNeighbour().getIp(), peer.getSecondNeighbour().getPort(), "getCountOfNetwork()");
    }

    public void setPriorNeighbour(String ip, int port)
    {
        peer.setPriorNeighbour(new Neighbour(ip, port));
    }

    public void setFirstNeighbour(String ip, int port)
    {
        peer.setFirstNeighbour(new Neighbour(ip, port));
    }

    public void setSecondNeighbour(String ip, int port) throws IOException {
        peer.setSecondNeighbour(new Neighbour(ip, port));
    }
    public void sendMessage(String ip, int port, String message)
    {
        if(message == null) return;
        try
        {
            Socket socket = new Socket(ip, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
        }
        catch (IOException e)
        {
            System.out.println(peer.getPort() + " ... " + port + "does not exist ... message: " + message + "to: " + port);
            e.printStackTrace();
        }
    }
}
