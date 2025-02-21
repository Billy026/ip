package bob.launcher;

import java.io.IOException;

import bob.gui.MainWindow;
import bob.managers.UiManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Represents the main application that should be opened.
 */
public class Main extends Application {
    private final UiManager uiManager = new UiManager();

    /**
     * Runs automatically when program is started.
     *
     * @param stage stage for which application is created on.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setBob(uiManager);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
