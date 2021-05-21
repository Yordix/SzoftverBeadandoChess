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


public class GameController {

    @FXML
    private Label mainLabel;

    @FXML
    private GridPane gameBoard;

    @FXML
    private Label stepsLabel;

    private KnightGameModel gameState;

    private int turnCount;

    private IntegerProperty steps = new SimpleIntegerProperty();

    private String playerOneName;
    private String playerTwoName;

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }
    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    private Position selected;

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

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private List<Position> takenPositions = new ArrayList<>();

    private Position P1pos = new Position(KnightGameModel.BOARD_SIZE - 1, KnightGameModel.BOARD_SIZE - 1);
    private Position P2pos = new Position(0, 0);



    private KnightGameModel model = new KnightGameModel();

    private void addTakenPosition() {
        takenPositions.add(P1pos);
        takenPositions.add(P2pos);
    }

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

    private void createBoard() {
        for (int i = 0; i < gameBoard.getRowCount(); i++) {
            for (int j = 0; j < gameBoard.getColumnCount(); j++) {
                var square = createSquare();
                gameBoard.add(square, j, i);
            }
        }
    }

    /**
     * Creates square
     * @return square
     */
    private StackPane createSquare() {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);
        return square;


    }

    private void createPieces() {
        for (int i = 0; i < model.getPieceCount(); i++) {
            model.positionProperty(i).addListener(this::piecePositionChange);
            var piece = createPiece(Color.valueOf(model.getPieceType(i).name()));
            getSquare(model.getPiecePosition(i)).getChildren().add(piece);
        }
    }

    private Circle createPiece(Color color) {
        var piece = new Circle(25);
        piece.setFill(color);
        return piece;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        var position = new Position(row, col);
        Logger.debug("Click on square {}", position);
        handleClickOnSquare(position);
    }

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
                    }
                }
            }
        }
    }

    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void selectPosition(Position position) {
        selected = position;
    }

    private void deselectSelectedPosition() {
        selected = null;
    }

    private void showTakenPositions()
    {
        for (var takenPos : takenPositions)
        {
            var square = getSquare(takenPos);
            square.getStyleClass().add("taken");
        }
    }

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

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var square = getSquare(selectablePosition);
            square.getStyleClass().remove("selectable");
        }
    }

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

    private void piecePositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldSquare = getSquare(oldPosition);
        StackPane newSquare = getSquare(newPosition);
        newSquare.getChildren().addAll(oldSquare.getChildren());
        oldSquare.getChildren().clear();
    }

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
