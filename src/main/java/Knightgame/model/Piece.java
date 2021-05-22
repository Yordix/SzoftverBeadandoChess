package Knightgame.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * This class if to provide the Pieces
 */
public class Piece {

    /**
     * Provide the type of the piece.
     */
    private final PieceType type;

    /**
     * Store the position.
     */
    private final ObjectProperty<Position> position = new SimpleObjectProperty<>();

    /**
     * Create the Piece.
     * @param type
     * Get the type of the creatable piece.
     * @param position
     * Get the position of the creatable piece.
     */
    public Piece(PieceType type, Position position) {
        this.type = type;
        this.position.set(position);
    }

    /**
     * Get the type of the Piece.
     * @return type
     */
    public PieceType getType() {
        return type;
    }

    /**
     * Get the position of the Piece.
     * @return position.get()
     */
    public Position getPosition() {
        return position.get();
    }

    /**
     * Move the Piece to the given direction.
     * @param direction
     * Get the parameter to where to move.
     */
    public void moveTo(Direction direction) {
        Position newPosition = position.get().moveTo(direction);
        position.set(newPosition);
    }

    /**
     * Get the property of the movement.
     * @return position
     */
    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    /**
     * Write the position as String.
     * @return type.toString() + position.get().toString()
     */
    public String toString() {
        return type.toString() + position.get().toString();
    }

    /**
     * The main function.
     * @param args
     * Get running arguments as parameter.
     */
    public static void main(String[] args) {
        Piece piece = new Piece(PieceType.BLACK, new Position(0, 0));
        piece.positionProperty().addListener((observableValue, oldPosition, newPosition) -> {
            System.out.printf("%s -> %s\n", oldPosition.toString(), newPosition.toString());
        });

    }
}


