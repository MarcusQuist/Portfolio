package P2P;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Peer
{
    private int count;
    private int port;
    private String ip;

    private Neighbour priorNeighbour;
    private Neighbour firstNeighbour;
    private Neighbour secondNeighbour;
    private PingPongThread secondNeighbourPingPongThread;
    private PingPongThread secondLastNeighbourPingPongThread;

    private NeighbourSystem neighbourSystem;

    private ServerSocket serverSocket;

    public Peer(int port)
    {
        this.port = port;
        this.neighbourSystem = new NeighbourSystem(this);
    }

    public void init()
    {
        listenForConnections();
    }

    private void listenForConnections()
    {
        try
        {
            serverSocket = new ServerSocket(port);
            ip = "localhost";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        while (true)
        {
            try
            {
                Socket requester = serverSocket.accept();
                new Thread(() -> handleRequest(requester)).start();
            }
            catch (IOException e)
            {
                System.out.println("Peer: " + port + " crashed");
                return;
            }
        }
    }

    private void handleRequest(Socket requester)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(requester.getInputStream()));
            Translator translator = new Translator(this, requester);
            while (true)
            {
                String message = reader.readLine();
                if (message == null) return;
                String[] messages = message.split(",");
                translator.translate(messages);
            }
        }
        catch(SocketException e)
        {
            // Connection lost due to crash
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void requestPingPong()
    {
        if(secondNeighbourPingPongThread != null) secondNeighbourPingPongThread.setShouldExecute(false);
        Socket secondNeighbourSocket = null;
        try
        {
            secondNeighbourSocket  = new Socket(secondNeighbour.getIp(), secondNeighbour.getPort());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        secondNeighbourPingPongThread = new PingPongThread(secondNeighbourSocket, this, false);
        secondNeighbourPingPongThread.start();
    }

    public void ackPingPong(Socket requester)
    {
        if(secondLastNeighbourPingPongThread != null) secondLastNeighbourPingPongThread.setShouldExecute(false);
        secondLastNeighbourPingPongThread = new PingPongThread(requester, this, true);
        secondLastNeighbourPingPongThread.start();
    }

    public String getIp()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }

    public NeighbourSystem getNeighbourSystem()
    {
        return neighbourSystem;
    }

    public void setPriorNeighbour(Neighbour neighbour)
    {
        this.priorNeighbour = neighbour;
    }

    public void setFirstNeighbour(Neighbour neighbour)
    {
        this.firstNeighbour = neighbour;
    }

    public void setSecondNeighbour(Neighbour neighbour) {
        this.secondNeighbour = neighbour;
        requestPingPong();
    }

    public Neighbour getPriorNeighbour()
    {
        return priorNeighbour;
    }

    public Neighbour getFirstNeighbour()
    {
        return firstNeighbour;
    }

    public Neighbour getSecondNeighbour()
    {
        return secondNeighbour;
    }

    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void getCountOfNetwork()
    {
        neighbourSystem.sendMessage(priorNeighbour.getIp(), priorNeighbour.getPort(), "setCount()," + count);
    }

    public void initIncrementCount()
    {
        count++;
        neighbourSystem.sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "incrementCount()," + port);
    }

    public void incrementCount(int port)
    {
        if(this.port != port)
        {
            count++;
            neighbourSystem.sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "incrementCount()," + port);
        }
    }

    public void initGetNetworkStatus(int port)
    {
            System.out.println("Peer: " + ip + ": " + port +
                    "\nprior neighbour: " + priorNeighbour.getIp() + ": " + priorNeighbour.getPort() +
                    "\nfirst neighbour: " + firstNeighbour.getIp() + ": " + firstNeighbour.getPort() +
                    "\nsecond neighbour: " + secondNeighbour.getIp() + ": " + secondNeighbour.getPort() + "\n");
            neighbourSystem.sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "networkStatus()," + ip + "," + port);
    }

    public void getNetworkStatus(String ip, int port)
    {
        if(this.port != port)
        {
            Neighbour requesterNeighbour = new Neighbour(ip, port);
            neighbourSystem.sendMessage(requesterNeighbour.getIp(), requesterNeighbour.getPort(), "printNetworkStatus()," +
                    this.ip + "," + this.port + "," + priorNeighbour.getIp() + "," + priorNeighbour.getPort() + "," +
                    firstNeighbour.getIp() + "," + firstNeighbour.getPort() + "," +
                    secondNeighbour.getIp() + "," + secondNeighbour.getPort()
            );
            neighbourSystem.sendMessage(firstNeighbour.getIp(), firstNeighbour.getPort(), "networkStatus()," + ip + "," + port);
        }
    }

    public void printNetworkStatus(String ip, int port, String priorIp, int priorPort, String firstIp, int firstPort, String secondIp, int secondPort)
    {
        System.out.println("Peer: " + ip + ": " + port +
                "\nprior neighbour: " + priorIp + ": " + priorPort +
                "\nfirst neighbour: " + firstIp + ": " + firstPort +
                "\nsecond neighbour " + secondIp + ": " + secondPort + "\n");
    }
}
