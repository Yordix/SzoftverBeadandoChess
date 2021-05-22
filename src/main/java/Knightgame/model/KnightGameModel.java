package Knightgame.model;

import javafx.beans.property.ObjectProperty;

import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.*;

/**
 * This class provides the model of the game.
 */
public class KnightGameModel {

    /**
     * Provides the size of the board.
     */
    public static int BOARD_SIZE = 8;

    /**
     * Store the pieces.
     */
    private final Piece[] pieces;

    /**
     * This is an enumeration for the Players.
     */
    public enum Player {
        PLAYER1, PLAYER2;

        /**
         * Get the next player after move.
         * @return this
         */
        public Player next(){
            return switch (this){
                case PLAYER1 -> PLAYER2;
                case PLAYER2 -> PLAYER1;
            };
        }
    }

    /**
     * Get the next player.
     * @return nextPlayer
     */
    public Player getNextPlayer()
    {
        return nextPlayer.get();
    }

    /**
     * Store the nextPlayer.
     */
    public static ReadOnlyObjectWrapper<Player> nextPlayer = new ReadOnlyObjectWrapper<>();

    /**
     * The knightgame model.
     */
    public KnightGameModel() {
        this(new Piece(PieceType.BLACK, new Position(0, 0)),
                new Piece(PieceType.WHITE, new Position(BOARD_SIZE - 1, BOARD_SIZE - 1)));
        nextPlayer.set(Player.PLAYER1);
    }

    /**
     * Knight pieces.
     * @param pieces
     * Get the pieces to build up the game.
     */
    public KnightGameModel(Piece... pieces) {
        checkPieces(pieces);
        this.pieces = pieces.clone();
    }

    /**
     * Check the pieces if available.
     * @param pieces
     * Throw error if a piece is not available.
     */
    private void checkPieces(Piece[] pieces) {
        var seen = new HashSet<Position>();
        for (var piece : pieces) {
            if (! isOnBoard(piece.getPosition()) || seen.contains(piece.getPosition())) {
                throw new IllegalArgumentException();
            }
            seen.add(piece.getPosition());
        }
    }

    /**
     * Get the count of the pieces.
     * @return pieces.length
     */
    public int getPieceCount() {
        return pieces.length;
    }

    /**
     * Get the type of the Piece.
     * @param pieceNumber
     * To know how much type it will take.
     * @return pieces[pieceNumber].getType()
     */
    public PieceType getPieceType(int pieceNumber) {
        return pieces[pieceNumber].getType();
    }

    /**
     * Get the Position of the pieces.
     * @param pieceNumber
     * To know how much positions it will take.
     * @return pieces[pieceNumber].getPosition()
     */
    public Position getPiecePosition(int pieceNumber) {
        return pieces[pieceNumber].getPosition();
    }

    /**
     * Set up Positions.
     * @param pieceNumber
     * To know how much Positions.
     * @return pieces[pieceNumber].positionProperty()
     */
    public ObjectProperty<Position> positionProperty(int pieceNumber)
    {
        return pieces[pieceNumber].positionProperty();
    }

    /**
     * Check if a move is valid or not.
     * @param pieceNumber
     * Get pieceNumber as parameter.
     * @param direction
     * Get the direction as parameter.
     * @return false/true
     */
    public boolean isValidMove(int pieceNumber, KnightDirection direction) {
        if (pieceNumber < 0 || pieceNumber >= pieces.length) {
            throw new IllegalArgumentException();
        }
        Position newPosition = pieces[pieceNumber].getPosition().moveTo(direction);
        if (! isOnBoard(newPosition)) {
            return false;
        }
        for (var piece : pieces) {
            if (piece.getPosition().equals(newPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get all the valid moves.
     * @param pieceNumber
     * To know how much valid moves can we make.
     * @return validMoves
     */
    public Set<KnightDirection> getValidMoves(int pieceNumber) {
        EnumSet<KnightDirection> validMoves = EnumSet.noneOf(KnightDirection.class);
        for (var direction : KnightDirection.values()) {
            if (isValidMove(pieceNumber, direction)) {
                validMoves.add(direction);
            }
        }
        return validMoves;
    }

    /**
     * Move the piece.
     * @param pieceNumber
     * To know which piece shall we move.
     * @param direction
     * To know where to move the piece.
     */
    public void move(int pieceNumber, KnightDirection direction)
    {
        pieces[pieceNumber].moveTo(direction);
        nextPlayer.set(nextPlayer.get().next());
    }

    /**
     * Is the move position on the board.
     * @param position
     * Get the position as parameter.
     * @return true/false
     */
    public static boolean isOnBoard(Position position) {
        return 0 <= position.row() && position.row() < BOARD_SIZE
                && 0 <= position.col() && position.col() < BOARD_SIZE;
    }

    /**
     * Get the number of Pieces.
     * @param position
     * Get the position of pieces as parameter.
     * @return OptionalInt.of()
     */
    public OptionalInt getPieceNumber(Position position) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Write it as String.
     * @return joiner.toString()
     */
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (var piece : pieces) {
            joiner.add(piece.toString());
        }
        return joiner.toString();
    }

    /**
     * The main function.
     * @param args
     * The running parameters.
     */
    public static void main(String[] args) {
        KnightGameModel model = new KnightGameModel();
        System.out.println(model);
    }
}
