package sample;

import java.io.PrintWriter;
import java.util.Random;

public class CheckersGame {

    public Player whitePlayer;
    public Player blackPlayer;

    public PrintWriter whitePlayerWriter;
    public PrintWriter blacPlayerWriter;

    public String whitePlayerName;
    public String blackPlayerName;

    public boolean isNowWhitesTurn;

    public CheckersGame(Player player1, Player player2){

        Random rand = new Random();
        int randomInt = rand.nextInt(2);
        if( randomInt == 0 ){
            whitePlayer = player1;
            blackPlayer = player2;

            whitePlayerName = player1.getName();
            blackPlayerName = player2.getName();

        }else {
            whitePlayer = player2;
            blackPlayer = player1;

            whitePlayerName = player2.getName();
            blackPlayerName = player1.getName();
        }

        isNowWhitesTurn = true;

        whitePlayer.getPlayerConnector().printWriter.println( "StartCheckersGame"+ "█" + "white");
        blackPlayer.getPlayerConnector().printWriter.println( "StartCheckersGame"+ "█" + "black");
    }

    public void makeAMove(String boardState){

        if(isNowWhitesTurn){
            blackPlayer.getPlayerConnector().printWriter.println("UpdateBoard" + "█" + boardState );
            isNowWhitesTurn = false;
        }else {
            whitePlayer.getPlayerConnector().printWriter.println("UpdateBoard" + "█" + boardState );
            isNowWhitesTurn = true;
        }
    }

    public void informPlayerThatHeLost(String boardState){
        if(isNowWhitesTurn){
            blackPlayer.getPlayerConnector().printWriter.println("youLost"  + "█" + boardState );
        }else {
            whitePlayer.getPlayerConnector().printWriter.println("youLost"  + "█" + boardState );
        }
    }

    public void informPlayerThatOponentGaveUp(String s) {

        System.out.println(" inform from checkers ");

        if(s.equals("true")){
            blackPlayer.getPlayerConnector().printWriter.println("oponentGaveUp");
        }else {
            whitePlayer.getPlayerConnector().printWriter.println("oponentGaveUp");
        }
    }
}
