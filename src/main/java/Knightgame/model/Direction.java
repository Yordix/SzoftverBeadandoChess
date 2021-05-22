package Knightgame.model;

/**
 * This interface is for the direction change.
 */
public interface Direction {

    /**
     * Get the change for the rows.
     * @return row
     */
    int getRowChange();

    /**
     * Get the change for the column.
     * @return col
     */
    int getColChange();

}
