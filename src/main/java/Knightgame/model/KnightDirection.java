package Knightgame.model;


/**
 * This enumeration is for the Knight movement.
 */
public enum KnightDirection implements Direction {
    /**
     * The "list" of movements.
     */
    LEFT_UP(-1, -2),
    UP_LEFT(-2, -1),
    UP_RIGHT(-2, 1),
    RIGHT_UP(-1, 2),
    RIGHT_DOWN(1, 2),
    DOWN_RIGHT(2, 1),
    DOWN_LEFT(2, -1),
    LEFT_DOWN(1, -2);

    /**
     * Change of the rows.
     */
    private final int rowChange;
    /**
     * Change of the columns.
     */
    private final int colChange;

    /**
     * Get the Direction.
     * @param rowChange
     * How much the row change.
     * @param colChange
     * How much the col change.
     */
    KnightDirection(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * Gets the row change.
     * @return rowChange
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * Gets the column change.
     * @return colChange
     */
    public int getColChange() {
        return colChange;
    }

    /**
     * Make the knightdirection.
     * @param rowChange
     * Get the rowchange.
     * @param colChange
     * Get the colchange.
     * @return direction
     * As a result get the direction of the piece.
     */
    public static KnightDirection of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * The main function.
     * @param args
     * Running parameters.
     */
    public static void main(String[] args) {
        System.out.println(of(1, 2));
    }

}


