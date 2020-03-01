package All;

import javafx.application.Application;

/**
 * Executes App
 */
public class Main {
    public static void main(String[] args){
        new Thread(() -> Application.launch(App.class)).start();
    }
}

