package bob.launcher;

import java.io.IOException;

import bob.GUI.MainWindow;
import bob.managers.UiManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    private UiManager uiManager = new UiManager();

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
