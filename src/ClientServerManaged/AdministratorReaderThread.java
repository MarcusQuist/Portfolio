package ClientServerManaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class AdministratorReaderThread extends Thread{

    private ArrayList<String> channels;
    private Socket socket;
    private boolean hasExecuted = false;

    public AdministratorReaderThread(Socket socket)
    {
        channels = new ArrayList<>();
        this.socket = socket;
    }

    public ArrayList<String> getChannels()
    {
        return channels;
    }

    public boolean hasExecuted(){
        return this.hasExecuted;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            boolean checkForChannels = false;
            while ((message = reader.readLine()) != null) {
                if (!message.contains("%")) {
                    if (!checkForChannels) {
                        if (message.equals("The following channels exist:")) {
                            System.out.println(message);
                            checkForChannels = true;
                        }
                    } else {
                        if (!message.equals("No more channels")) {
                            System.out.println(message);
                            channels.add(message);
                            hasExecuted = true;
                        } else {
                            checkForChannels = false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
