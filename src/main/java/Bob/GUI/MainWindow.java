package bob.GUI;

import bob.managers.UiManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Represents a container for which dialog boxes will appear in.
 * Automatically scrolls downwards when new dialog box exceeds window height.
 * 
 * @param scrollPane scrollable view.
 * @param dialogContainer vertical box to arrange new dialog boxes.
 * @param userInput text field for users to enter input.
 * @param sendButton send button to send user input to the uiManager.
 * @param uiManager manages the user interface.
 * @param userImage image to be used for the user.
 * @param bobImage image to be used for the chatbot character.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private UiManager uiManager;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/man.png"));
    private Image bobImage = new Image(this.getClass().getResourceAsStream("/images/Bob my boy.png"));

    /**
     * Sets scrollPane height and output greeting on initialisation.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        String greeting = "\n" + "Hi, I'm Bob!\n" + 
                "...\n" + 
                "\n" + 
                "Can I do something for you?";
        dialogContainer.getChildren().addAll(
                DialogBox.getBobDialog(greeting, bobImage)
            );
    }

    /**
     * Display output on set up of Bob.
     * 
     * @param uiManager manages the user interface.
     */
    public void setBob(UiManager uiManager) {
        this.uiManager = uiManager;
        String savedList = "\n" + uiManager.getSavedListMessage();
        String todayTasks = "\n" + uiManager.getIncomingDeadlines();
        dialogContainer.getChildren().addAll(
                DialogBox.getBobDialog(savedList, bobImage),
                DialogBox.getBobDialog(todayTasks, bobImage)    
            );
    }

    /**
     * Displays output of user inputs as dialog boxes.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (checkForExit(input)) {
            return;
        }

        displayResponse(input);
    }

    /**
     * Checks whether user input indicates to exit the program.
     * 
     * @param input user input.
     * @return whether the program should exit.
     */
    @FXML
    private boolean checkForExit(String input) {
        if (input.equalsIgnoreCase("bye")) {
            displayExitDialog(input);
            delayExit();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates two dialog boxes: one echoing user input and another displaying the output of
     * the user input.
     * 
     * Clears user input.
     * 
     * @param input user input.
     */
    @FXML
    private void displayResponse(String input) {
        String response = "\n" + this.uiManager.executeUserCommand(input);
        dialogContainer.getChildren().addAll(
            DialogBox.getUserDialog("\n" + input, userImage),
            DialogBox.getBobDialog(response, bobImage)
        );
        userInput.clear();
    }

    /**
     * Displays output on exit of program as a dialog box.
     * 
     * @param input user input.
     */
    @FXML
    private void displayExitDialog(String input) {
        dialogContainer.getChildren().addAll(
            DialogBox.getUserDialog(input, userImage),
            DialogBox.getBobDialog("\nOk! Bye. See you soon.", bobImage)
        );
        userInput.clear();
    }

    /**
     * Delays exit for 2 seconds so that user can read output on exit.
     */
    @FXML
    private void delayExit() {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished((event) -> {
            Platform.exit();
            System.exit(0);
        });
        delay.play();
    }
}
