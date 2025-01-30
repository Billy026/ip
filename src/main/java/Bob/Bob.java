package Bob;

import java.util.Scanner;

import Bob.managers.UiManager;

/**
 * Main class.
 */
public class Bob {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        UiManager uiManager = new UiManager();
        uiManager.executeUi(sc);

        // Clean up
        sc.close();
    }
}