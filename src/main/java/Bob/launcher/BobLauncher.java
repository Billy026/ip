package bob.launcher;

import javafx.application.Application;

/**
 * Represents the transition from Java main function to JavaFX start function.
 */
public class BobLauncher {
    /**
     * Bypasses restrictions on start up with JavaFX.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
