package Knightgame.model;

import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    Position position;
    PieceType WHITE;


    @BeforeEach
    void init() {
        position = new Position(0, 0);
        WHITE = PieceType.WHITE;

    }


    @Test
    void getType(){
        assertEquals(new Piece(PieceType.WHITE, new Position(0, 0)).getType(),PieceType.WHITE);
        assertEquals(new Piece(PieceType.WHITE, new Position(3, 4)).getType(),PieceType.WHITE);
        assertEquals(new Piece(PieceType.BLACK, new Position(0, 0)).getType(),PieceType.BLACK);
        assertEquals(new Piece(PieceType.BLACK, new Position(3, 4)).getType(),PieceType.BLACK);
    }
    @Test
    void getPosition(){
        assertEquals(new Piece(PieceType.WHITE,new Position(0,0)).getPosition(),new Position(0,0));
        assertEquals(new Piece(PieceType.WHITE,new Position(2,3)).getPosition(),new Position(2,3));
    }


    @Test
    void testToString() {
        assertEquals(new Piece(PieceType.WHITE, new Position(0, 0)).toString(),"WHITE(0,0)");
    }
}