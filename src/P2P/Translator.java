package P2P;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Translator
{
    private Peer peer;
    private Socket socket;
    private NeighbourSystem neighbourSystem;

    public Translator(Peer peer, Socket socket)
    {
        this.peer = peer;
        this.socket = socket;
        this.neighbourSystem = peer.getNeighbourSystem();
    }

    public void translate(String[] messages) throws IOException {
        Socket tempSocket = null;
        Neighbour priorNeighbour;
        Neighbour firstNeighbour;
        Neighbour secondNeighbour;
        switch(messages[0])
        {
            case("insertNewPeer()"):
                neighbourSystem.insertNewPeer(socket.getInetAddress().getHostAddress(), Integer.parseInt(messages[1]));
                return;
            case("setPriorNeighbour()"):
                Neighbour newPriorNeighbour = new Neighbour(messages[1], Integer.parseInt(messages[2]));
                peer.setPriorNeighbour(newPriorNeighbour);
                return;
            case("setThisToPriorOfSecondNeighbour()"):
                System.out.println("2. :" + peer.getSecondNeighbour().getPort());
                secondNeighbour = peer.getSecondNeighbour();
                tempSocket = createSocket(secondNeighbour.getIp(), secondNeighbour.getPort());
                try
                {
                    PrintWriter writer = new PrintWriter(tempSocket.getOutputStream(), true);
                    writer.println("setPriorNeighbour()," + peer.getIp() + "," + peer.getPort());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return;
            case("setFirstNeighbour()"):
                neighbourSystem.setFirstNeighbour(messages[1], Integer.parseInt(messages[2]));
                return;
            case("setSecondNeighbour()"):
                Neighbour newSecondNeighbour = new Neighbour(messages[1], Integer.parseInt(messages[2]));
                peer.setSecondNeighbour(newSecondNeighbour);
                return;
            case("setFirstNeighbourToSecond()"):
                peer.setFirstNeighbour(peer.getSecondNeighbour());
                return;
            case("getFirstNeighbourOfSecondNeighbour()"):
                secondNeighbour = peer.getSecondNeighbour();
                tempSocket = createSocket(secondNeighbour.getIp(), secondNeighbour.getPort());
                try
                {
                    PrintWriter writer = new PrintWriter(tempSocket.getOutputStream(), true);
                    writer.println("getFirstNeighbourOfSecondNeighbourResponse()," + peer.getIp() + "," + peer.getPort());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return;
            case("getFirstNeighbourOfSecondNeighbourResponse()"):
                Neighbour firstNeighbourOfSecond = peer.getFirstNeighbour();
                tempSocket = createSocket(messages[1], Integer.parseInt(messages[2])); // reference to N1
                try
                {
                    PrintWriter writer = new PrintWriter(tempSocket.getOutputStream(), true);
                    writer.println("setSecondNeighbour()," + firstNeighbourOfSecond.getIp() + "," + firstNeighbourOfSecond.getPort());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return;

            case("getFirstNeighbour()"):
                firstNeighbour = peer.getFirstNeighbour();
                tempSocket = createSocket(messages[1], Integer.parseInt(messages[2])); // send back to peer asking
                try
                {
                    PrintWriter writer = new PrintWriter(tempSocket.getOutputStream(), true);
                    writer.println("SecondNeighbourCrashed()," + firstNeighbour.getIp() + "," + firstNeighbour.getPort());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return;
            case("getNewSecondNeighbourOnCrash()"):
                secondNeighbour = peer.getSecondNeighbour();
                tempSocket = createSocket(messages[1], Integer.parseInt(messages[2])); // send back to peer asking
                try
                {
                    PrintWriter writer = new PrintWriter(tempSocket.getOutputStream(), true);
                    writer.println("setSecondNeighbour()," + secondNeighbour.getIp() + "," + secondNeighbour.getPort());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return;
            case("incrementCount()"):
                peer.incrementCount(Integer.parseInt(messages[1]));
                return;
            case("getCountOfNetwork()"):
                peer.getCountOfNetwork();
                return;
            case("setCount()"):
                peer.setCount(Integer.parseInt(messages[1]));
                return;
            case("ackPingPong()"):
                peer.ackPingPong(socket);
                return;
            case("networkStatus()"):
                peer.getNetworkStatus(messages[1], Integer.parseInt(messages[2]));
                return;
            case("printNetworkStatus()"):
                peer.printNetworkStatus(messages[1], Integer.parseInt(messages[2]), messages[3], Integer.parseInt(messages[4]),
                        messages[5], Integer.parseInt(messages[6]), messages[7], Integer.parseInt(messages[8]));
        }
    }

    private Socket createSocket(String ip, int port)
    {
        try
        {
            return new Socket(ip, port);
        }
        catch (IOException e)
        {
            // Connection has crashed, ignore
        }
        return null;
    }
}
