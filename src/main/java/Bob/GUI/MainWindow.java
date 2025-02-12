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
     * Injects the UiManager instance
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
     * Creates two dialog boxes, one executing user input and the other containing Bob's reply and then
     * appends them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        //Exit when user types "bye"
        if (input.equalsIgnoreCase("bye")) {
            dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBobDialog("\nOk! Bye. See you soon.", bobImage)
            );
            userInput.clear();

            // Exit after 3 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished((event) -> {
                Platform.exit();
                System.exit(0);
            });
            delay.play();
            return;
        }

        String response = "\n" + this.uiManager.executeUserCommand(input);
        dialogContainer.getChildren().addAll(
            DialogBox.getUserDialog("\n" + input, userImage),
            DialogBox.getBobDialog(response, bobImage)
        );
        userInput.clear();
    }
}
