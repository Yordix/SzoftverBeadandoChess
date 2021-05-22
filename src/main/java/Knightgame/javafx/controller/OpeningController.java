package Knightgame.javafx.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;

import org.tinylog.Logger;

/**
 * This class is for the main window at the beginning of the game.
 */
public class OpeningController {


    /**
     * This field is for the PlayerOne's name.
     */
    @FXML
    private TextField playerOneNameTextField;

    /**
     * This field is for the PlayerTwo's name.
     */
    @FXML
    private TextField playerTwoNameTextField;

    /**
     * This is for the label if a name is not given by the players.
     */
    @FXML
    private Label errorLabel;

    /**
     * Startaction is for to start the game.
     * @param actionEvent
     * Actionevent is related to the button OnClick.
     * @throws IOException
     * IOException is occurs when the game cannot be started.
     */
    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerOneNameTextField.getText().isEmpty() || playerTwoNameTextField.getText().isEmpty()) {
            errorLabel.setText("Please enter two names!");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));

            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayerOneName(playerOneNameTextField.getText());
            fxmlLoader.<GameController>getController().setPlayerTwoName(playerTwoNameTextField.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            Logger.info("The playerOne's name is set to {}, loading game scene", playerOneNameTextField.getText());
            Logger.info("The playerTwo's name is set to {}, loading game scene", playerTwoNameTextField.getText());
        }
    }

    /**
     * This function is for the leaderboard panel.
     * @param e
     * The e is for the button OnClick event.
     * @throws IOException
     * Throws exception if the leaderboard panel cannot be opened.
     */
    public void goToLeaderboard(ActionEvent e) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/highscore.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



}
