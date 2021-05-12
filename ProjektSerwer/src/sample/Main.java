package sample;

import java.util.ArrayList;

public class Main {

    public static ArrayList <Player> PlayerList;
    public static ArrayList <CheckersGame> CheckersGameList;

    public static void main(String[] args) {

        PlayerConnectorWrap playerConnectorWrap = new PlayerConnectorWrap();
        playerConnectorWrap.start();

        PlayerList = new ArrayList< Player>();
        CheckersGameList = new ArrayList<CheckersGame>();
    }
}
