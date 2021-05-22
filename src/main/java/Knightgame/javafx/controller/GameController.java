package Knightgame.javafx.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import Knightgame.model.KnightGameModel;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;

import Knightgame.model.KnightDirection;
import Knightgame.model.Position;

/**
 * This class provides the main controlling unit of the game.
 */
public class GameController {

    /**
     * This is the declaration of the main label during the game.
     */
    @FXML
    private Label mainLabel;

    /**
     * This is the declaration of the game board.
     */
    @FXML
    private GridPane gameBoard;

    /**
     * This is the label of the step counter.
     */
    @FXML
    private Label stepsLabel;

    /**
     * Counter of the steps.
     */
    private IntegerProperty steps = new SimpleIntegerProperty();

    /**
     * Name of the player one in the game.
     */
    private String playerOneName;
    /**
     * Name of the player two in the game.
     */
    private String playerTwoName;

    /**
     * This function set the name of playerOneName to the given name.
     * @param playerOneName
     * This will be the display name of playerone.
     */
    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    /**
     * Ths function set the name of playerTwoName to the given name.
     * @param playerTwoName
     * This will be the display name of playertwo.
     */
    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    /**
     * This will store the position which is selected.
     */
    private Position selected;

    /**
     * This enumeration is decide the selected character and where it is moving.
     */
    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }

    /**
     * Set the selection_phase active to pick another position.
     */
    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    /**
     * This list contains the squares which are can be selected.
     */
    private List<Position> selectablePositions = new ArrayList<>();

    /**
     * This list contains the squares which are taken for the rest of the game.
     */
    private List<Position> takenPositions = new ArrayList<>();

    /**
     * Set the position of player one.
     */
    private Position P1pos = new Position(KnightGameModel.BOARD_SIZE - 1, KnightGameModel.BOARD_SIZE - 1);
    /**
     * Set the position of player two.
     */
    private Position P2pos = new Position(0, 0);


    /**
     * Create a model.
     */
    private KnightGameModel model = new KnightGameModel();

    /**
     * Set the two starter position as taken.
     */
    private void addTakenPosition() {
        takenPositions.add(P1pos);
        takenPositions.add(P2pos);
    }

    /**
     * Initialize the game board and the figures.
     */
    @FXML
    private void initialize() {
        createBoard();
        Logger.debug("Created the Board!");
        createPieces();
        addTakenPosition();
        setSelectablePositions();
        showSelectablePositions();
        Platform.runLater(() -> mainLabel.setText(String.format("Let's get to battle <%s> and <%s>", playerOneName, playerTwoName)));
        stepsLabel.textProperty().bind(steps.asString());
    }

    /**
     * Draw the play board.
     */
    private void createBoard() {
        for (int i = 0; i < gameBoard.getRowCount(); i++) {
            for (int j = 0; j < gameBoard.getColumnCount(); j++) {
                var square = createSquare();
                gameBoard.add(square, j, i);
            }
        }
    }

    /**
     * Creates square.
     * @return square.
     */
    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;


    }

    /**
     * Create the chess pieces.
     */
    private void createPieces() {
        for (int i = 0; i < model.getPieceCount(); i++) {
            model.positionProperty(i).addListener(this::piecePositionChange);
            var piece = createPiece(Color.valueOf(model.getPieceType(i).name()));
            getSquare(model.getPiecePosition(i)).getChildren().add(piece);
        }
    }

    /**
     * Create the character Circles.
     * @param color
     * It gets a color parameter which describe the color of the piece.
     * @return piece
     * Returns a piece which will be the player's character.
     */
    private Circle createPiece(Color color) {
        var piece = new Circle(25);
        piece.setFill(color);
        return piece;
    }

    /**
     * Handles the mouse events such as click.
     * @param event
     * The event now will be mouse clicks so the game can describe the positions by the mouse.
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Click on square {}", position);
        handleClickOnSquare(position);
    }

    /**
     * Handles the click on square.
     * @param position
     * Gets the parameter of a square's position such as row and column for player One and Two too.
     */
    private void handleClickOnSquare(Position position) {
        if (model.getNextPlayer() == KnightGameModel.Player.PLAYER1) {
            switch (selectionPhase) {
                case SELECT_FROM -> {
                    if (selectablePositions.contains(position)) {
                        selectPosition(position);
                        alterSelectionPhase();
                    }
                    if (selectablePositions.size() == 0) {
                        endGame();
                    }
                }

                case SELECT_TO -> {
                    if (selectablePositions.contains(position)) {
                        var pieceNumber = model.getPieceNumber(selected).getAsInt();
                        var direction = KnightDirection.of(position.row() - selected.row(), position.col() - selected.col());
                        Logger.debug("Moving piece {} {}", pieceNumber, direction);
                        model.move(pieceNumber, direction);
                        takenPositions.add(new Position(position.row(), position.col()));
                        P1pos = new Position(position.row(), position.col());
                        showTakenPositions();
                        steps.set(steps.get() + 1);
                        deselectSelectedPosition();
                        alterSelectionPhase();
                        model.getNextPlayer();
                        mainLabel.setText(String.format("{%s} is next.", playerTwoName));
                    }
                }
            }
        }
        else
        {
            switch (selectionPhase)
            {
                case SELECT_FROM -> {
                    if (selectablePositions.contains(position))
                    {
                        selectPosition(position);
                        alterSelectionPhase();
                    }
                    if (selectablePositions.size() == 0)
                    {
                        endGame();
                    }
                }
                case SELECT_TO -> {
                    if (selectablePositions.contains(position))
                    {
                        var pieceNumber = model.getPieceNumber(selected).getAsInt();
                        var direction = KnightDirection.of(position.row() - selected.row(), position.col() - selected.col());
                        model.move(pieceNumber, direction);
                        takenPositions.add(new Position(position.row(), position.col()));
                        P2pos = new Position(position.row(), position.col());
                        showTakenPositions();
                        steps.set(steps.get() + 1);
                        deselectSelectedPosition();
                        alterSelectionPhase();
                        model.getNextPlayer();
                        mainLabel.setText(String.format("{%s} is next.", playerOneName));
                    }
                }
            }
        }
    }

    /**
     * Remove the select style from characters after movement.
     */
    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    /**
     * Select a position to take.
     * @param position
     * Select a position on the board.
     */
    private void selectPosition(Position position) {
        selected = position;
    }

    /**
     * Remove style from selected position after selection.
     */
    private void deselectSelectedPosition() {
        selected = null;
    }

    /**
     * Show all the taken positions on the board.
     */
    private void showTakenPositions()
    {
        for (var takenPos : takenPositions)
        {
            var square = getSquare(takenPos);
            square.getStyleClass().add("taken");
        }
    }

    /**
     * Set all squares as selectable position which are not selected before or good for the direction.
     */
    private void setSelectablePositions() {
        selectablePositions.clear();
        if (model.getNextPlayer() == KnightGameModel.Player.PLAYER1) {
            switch (selectionPhase) {
                case SELECT_FROM -> selectablePositions.add(P1pos);

                case SELECT_TO -> {
                    var pieceNumber = model.getPieceNumber(selected).getAsInt();

                    for (var direction : model.getValidMoves(pieceNumber)) {
                        if (takenPositions.contains(selected.moveTo(direction))) {
                            continue;
                        }
                        selectablePositions.add(selected.moveTo(direction));

                    }
                }
            }
        }
        else{
            switch (selectionPhase) {
                case SELECT_FROM -> selectablePositions.add(P2pos);

                case SELECT_TO -> {
                    var pieceNumber = model.getPieceNumber(selected).getAsInt();

                    for (var direction : model.getValidMoves(pieceNumber)) {
                        if (takenPositions.contains(selected.moveTo(direction))) {
                            continue;
                        }
                        selectablePositions.add(selected.moveTo(direction));

                    }
                }
            }
        }

    }

    /**
     * Show every selectable positions on the table.
     */
    private void showSelectablePositions() {
        int i = 0;
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().add("selectable");
            Logger.info(i + ". selectablePosition: " + selectablePosition);
            i++;
        }
        Logger.debug("Number of selectable positions: " + i);
    }

    /**
     * After a position is selected remove the style for all selectable positions.
     */
    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

    /**
     * Get the square on the board.
     * @param position
     * Get the position of the square as position.
     * @return (StackPane) child;
     * Returns the square as the child of the StackPane board.
     */
    private StackPane getSquare(Position position) {
        for (var child : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(child) == null || GridPane.getColumnIndex(child) == null){
                continue;
            }
            else if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    /**
     * Change the position of the piece.
     * @param observable
     * See if a position is observable.
     * @param oldPosition
     * Get the old position of the piece.
     * @param newPosition
     * Get the new position of the piece.
     */
    private void piecePositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }

    /**
     * Finish the game.
     * Open up leaderboard.
     */
    private void endGame(){
        if (model.getNextPlayer() == KnightGameModel.Player.PLAYER1){
            Platform.runLater(() -> mainLabel.setText(String.format("%s has triumph this day!", playerTwoName)));
            Logger.info("{} winned the game!",playerTwoName);
            var Winner = playerTwoName;
        }
        else{
            Platform.runLater(() -> mainLabel.setText(String.format("%s has triumph this day!", playerOneName)));
            Logger.info("{} winned the game!",playerOneName);
            var Winner = playerOneName;
        }
    }

}
