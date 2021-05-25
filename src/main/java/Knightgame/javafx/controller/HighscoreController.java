package Knightgame.javafx.controller;

import java.io.IOException;
import java.util.List;

import Knightgame.dao.HighScore;
import Knightgame.dao.HighScoreDAO;
import Knightgame.dao.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.tinylog.Logger;

import javax.inject.Inject;


/**
 * This class controls the leaderboard at the end of the game.
 */
public class HighscoreController {
    @FXML
    private TableView<Score> highScoreTable;

    @FXML
    private TableColumn<Score, String> name;

    @FXML
    private TableColumn<Score, String> score;

    @FXML
    private TableColumn<Score, String> date;


    /**
     * This function handles the restart button at the end of the game.
     * @param actionEvent
     * The actionEvent is related to OnClick, so when the button is pushed it return to opening screen.
     * @throws IOException
     * The IOException is about when the leaderboard cannot be opened.
     */
    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/opening.fxml"));
        Logger.debug("Loading Opening.fxml");
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void initialize(){
        HighScoreDAO highScoreDao = new HighScoreDAO();
        HighScore hs = new HighScore();
        hs = highScoreDao.getHighScores();
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        List<Score> highScoreList = hs.getHighscore();
        ObservableList<Score> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);
        highScoreTable.setItems(observableResult);
    }
}