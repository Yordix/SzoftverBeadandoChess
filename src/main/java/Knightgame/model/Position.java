package Knightgame.model;

/**
 * This record is to provide the positons of the Pieces.
 */
public record Position(int row, int col) {

    /**
     * Where to move the piece.
     * @param direction
     * Get direction as parameter to know where to move.
     * @return new Position(row + direction.getRowChange(), col + direction.getColChange())
     */
    public Position moveTo(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Get data as string.
     * @return String
     */
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}
