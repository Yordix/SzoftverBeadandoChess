package Knightgame.javafx.controller;

import com.sun.xml.bind.v2.util.ByteArrayOutputStreamEx;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class PlayerController {

    public enum Player {
        PLAYER1, PLAYER2;

        public Player next(){
            return switch (this){
                case PLAYER1 -> PLAYER2;
                case PLAYER2 -> PLAYER1;
            };
        }
    }

    public static Player getNextPlayer()
    {
        return nextPlayer.get();
    }

    public static ReadOnlyObjectWrapper<Player> nextPlayer = new ReadOnlyObjectWrapper<>();
}
