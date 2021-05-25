package Knightgame.model;

import javafx.beans.property.ReadOnlyObjectWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class KnightGameModelTest {

    private ReadOnlyObjectWrapper<KnightGameModel.Player> nextPlayer = new ReadOnlyObjectWrapper<>();
    KnightGameModel model = new KnightGameModel();




    @BeforeEach
    void init() {
        nextPlayer.set(KnightGameModel.Player.PLAYER1);
    }

    @Test
    void getNextPlayer(){
        assertEquals(nextPlayer.get(), KnightGameModel.Player.PLAYER1);
        nextPlayer.set(nextPlayer.get().next());
        assertEquals(nextPlayer.get(), KnightGameModel.Player.PLAYER2);
        nextPlayer.set(nextPlayer.get().next());
        assertEquals(nextPlayer.get(), KnightGameModel.Player.PLAYER1);
    }

    @Test
    void getPieceCount(){
        KnightGameModel model = new KnightGameModel();
        assertEquals(model.getPieceCount(),2);
        KnightGameModel model1 = new KnightGameModel(new Piece(PieceType.WHITE, new Position(0, 0)),
                new Piece(PieceType.BLACK, new Position(1, 1)),new Piece(PieceType.WHITE,new Position(2,2)));
        assertEquals(model1.getPieceCount(),3);
    }

    @Test
    void getPieceType() {
        KnightGameModel model = new KnightGameModel();
        assertEquals(model.getPieceType(0),PieceType.BLACK);
        assertEquals(model.getPieceType(1),PieceType.WHITE);
    }

    @Test
    void getPiecePosition(){
        KnightGameModel model = new KnightGameModel();
        assertEquals(model.getPiecePosition(0),new Position(0,0));
        assertEquals(model.getPiecePosition(1),new Position(7,7));
    }

    @Test
    void isValidMove(){
        KnightGameModel model = new KnightGameModel();
        assertThrows(IllegalArgumentException.class, () -> model.isValidMove(-2,KnightDirection.LEFT_UP));
        assertThrows(IllegalArgumentException.class, () -> model.isValidMove(2,KnightDirection.LEFT_UP));
        assertEquals(model.isValidMove(0,KnightDirection.LEFT_UP),false);
        assertEquals(model.isValidMove(0,KnightDirection.RIGHT_DOWN),true);
        assertEquals(model.isValidMove(1,KnightDirection.LEFT_UP),true);
    }

    @Test
    void getValidMoves(){
        KnightGameModel model = new KnightGameModel();
        EnumSet<KnightDirection> validMovestest0 = EnumSet.noneOf(KnightDirection.class);
        validMovestest0.add(KnightDirection.RIGHT_DOWN);
        validMovestest0.add(KnightDirection.DOWN_RIGHT);
        assertEquals(model.getValidMoves(0),validMovestest0);
        EnumSet<KnightDirection> validMovestest1 = EnumSet.noneOf(KnightDirection.class);
        validMovestest1.add(KnightDirection.UP_LEFT);
        validMovestest1.add(KnightDirection.LEFT_UP);
        assertEquals(model.getValidMoves(1),validMovestest1);
    }

    @Test
    void move(){
        KnightGameModel model = new KnightGameModel();
        model.move(0,KnightDirection.RIGHT_DOWN);
        assertEquals(model.getPiecePosition(0),new Position(1,2));
        model.move(0,KnightDirection.RIGHT_DOWN);
        assertEquals(model.getPiecePosition(0),new Position(2,4));
        model.move(1,KnightDirection.LEFT_UP);
        assertEquals(model.getPiecePosition(1),new Position(6,5));
        model.move(1,KnightDirection.LEFT_UP);
        assertEquals(model.getPiecePosition(1),new Position(5,3));
    }

    @Test
    void getPieceNumber(){
        KnightGameModel model = new KnightGameModel();
        assertEquals(model.getPieceNumber(new Position(0,0)), OptionalInt.of(0));
        assertEquals(model.getPieceNumber(new Position(7,7)), OptionalInt.of(1));
        assertEquals(model.getPieceNumber(new Position(5,5)),OptionalInt.empty());
    }



    @Test
    void testToString() {
        assertEquals(model.toString(),"[BLACK(0,0),WHITE(7,7)]");
    }

    @Test
    void testConstructor_invalid() {
        assertThrows(IllegalArgumentException.class, () -> new KnightGameModel(new Piece(PieceType.WHITE, new Position(0, 0)),
                new Piece(PieceType.BLACK, new Position(0, 0))));
    }

}