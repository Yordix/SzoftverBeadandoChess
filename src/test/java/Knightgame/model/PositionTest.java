package Knightgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position position;

    void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col())
        );
    }

    @BeforeEach
    void init() {
        position = new Position(0, 0);
    }

    @Test
    void moveTo() {
        assertPosition(position.row() - 1, position.col() - 2, position.moveTo(KnightDirection.LEFT_UP));
        assertPosition(position.row() + 1, position.col() - 2, position.moveTo(KnightDirection.LEFT_DOWN));
        assertPosition(position.row() - 2, position.col() - 1, position.moveTo(KnightDirection.UP_LEFT));
        assertPosition(position.row() - 2, position.col() + 1, position.moveTo(KnightDirection.UP_RIGHT));
        assertPosition(position.row() - 1, position.col() + 2, position.moveTo(KnightDirection.RIGHT_UP));
        assertPosition(position.row() + 1, position.col() + 2, position.moveTo(KnightDirection.RIGHT_DOWN));
        assertPosition(position.row() + 2, position.col() - 1, position.moveTo(KnightDirection.DOWN_LEFT));
        assertPosition(position.row() + 2, position.col() + 1, position.moveTo(KnightDirection.DOWN_RIGHT));
    }

    @Test
    void testToString() {
        assertEquals("(0,0)", position.toString());
    }


}