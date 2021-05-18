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


public class OpeningController {



    @FXML
    private TextField playerOneNameTextField;

    @FXML
    private TextField playerTwoNameTextField;

    @FXML
    private Label errorLabel;

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

    public void goToLeaderboard(ActionEvent e) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/highscore.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



}
