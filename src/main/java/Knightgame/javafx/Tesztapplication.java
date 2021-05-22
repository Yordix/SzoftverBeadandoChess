package Knightgame.javafx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class if for the game build.
 */
public class Tesztapplication extends Application {

    /**
     * This function is start up the game and opens the main menu.
     * @param stage
     * Get the Stage as parameter.
     * @throws IOException
     * Throws exception if the main menu does not exist.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/opening.fxml"));
        stage.setTitle("Knight-Game");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
