package P2P;

public class Neighbour
{
    private String ip;
    private int port;

    public Neighbour(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
