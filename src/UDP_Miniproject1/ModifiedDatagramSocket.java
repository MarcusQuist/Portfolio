package UDP_Miniproject1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ModifiedDatagramSocket extends DatagramSocket
{
    private Random random;
    public ModifiedDatagramSocket(int port, InetAddress ip) throws SocketException
    {
        super(port, ip);
        random = new Random();
    }

    public void send(DatagramPacket packet) throws IOException {
        String message = new String(packet.getData());
        String[] messages = message.split(" ");

        messages = pickMethod(messages);

        for (String m: messages)
        {
            DatagramPacket newPacket = new DatagramPacket(m.getBytes(), m.length(), packet.getAddress(), packet.getPort());
            super.send(newPacket);
        }
    }

    public String[] pickMethod(String[] messages) {
        int method = random.nextInt(4);
        switch (method)
        {
            case 0: return discard(messages);
            case 1: return duplicate(messages);
            case 2: return reorder(messages);
            default: return messages;
        }
    }

    private String[] discard(String[] messages)
    {
        String[] newMessages = new String[messages.length-1];
        int remove = random.nextInt(messages.length);

        int j = 0;
        for(int i = 0; i < messages.length; i++)
        {
            if(i != remove) {
                newMessages[j] = messages[i];
                j++;
            }
        }
        return newMessages;
    }

    private String[] duplicate(String[] messages) {
        String[] newMessages = new String[messages.length+1];
        int randomIndex = random.nextInt(messages.length);

        int j = 0;
        for(int i = 0; i < messages.length; i++)
        {
            if(i == randomIndex)
            {
                newMessages[j] = messages[i];
                j++;
            }
            newMessages[j] = messages[i];
            j++;
        }
        return newMessages;
    }

    private String[] reorder(String[] messages) {
        List<String> tempList = Arrays.asList(messages);
        Collections.shuffle(tempList);

        for(int i = 0; i < tempList.size(); i++)
        {
            messages[i] = tempList.get(i);
        }
        return messages;
    }
}
