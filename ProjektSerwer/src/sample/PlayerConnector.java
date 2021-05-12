package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerConnector extends Thread {

    private BufferedReader bufferedReader;
    public PrintWriter printWriter;

    private Socket socket;

    private Player player;

    private String playerName;


    public PlayerConnector(Socket socket) {

        this.socket = socket;
        this.playerName = "";
        this.player = null;
        System.out.println("Nowy klient!");
        //
    }

    @Override
    public void run(){
        System.out.println("hej tu PlayerConnector");

        try{

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            String msg;

            while(true){

                msg = bufferedReader.readLine();
                System.out.println(playerName + ": " + msg);

                String[] tokens = msg.split("█");
                switch (tokens[0]) {

                    case "letMeIn" : {

                        Boolean isNickUnique = true;

                        for( int i=0; i < Main.PlayerList.size(); i++ ){
                            if( tokens[1].equals( Main.PlayerList.get(i).getName() ) ){                        // == nie działa
                                System.out.println("Nick: " + tokens[1] + " jest zajęty");
                                printWriter.println("nickIsTaken");
                                isNickUnique = false;
                            }
                        }

                        if(isNickUnique == true){

                            for(int i=0; i < Main.PlayerList.size(); i++){
                                printWriter.println("addPlayerToList"+ "█" +
                                        Main.PlayerList.get(i).getName() + "█" +
                                        Main.PlayerList.get(i).getStatus());
                            }


                            for(int i=0; i < Main.PlayerList.size(); i++){
                                try{
                                    Main.PlayerList.get(i).getPlayerConnector().printWriter.println( "addPlayerToList" + "█" + tokens[1] + "█" + "ready" );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            playerName = tokens[1];
                            Player player = new Player(tokens[1], "ready", this);
                            Main.PlayerList.add( player );

                            printWriter.println("logIn");
                        }

                        break;
                    }
                    case "InvitePlayerToPlayCheckers" : {

                        for(int i=0; i < Main.PlayerList.size(); i++){
                            if( Main.PlayerList.get(i).getName().equals( tokens[2] ) ){

                                Boolean alreadyInGame= false;

                                for(int j=0; j<Main.CheckersGameList.size(); j++){
                                    if( Main.CheckersGameList.get(j).whitePlayerName.equals( tokens[2] ) || Main.CheckersGameList.get(j).blackPlayerName.equals( tokens[2] ) ){
                                        alreadyInGame = true;
                                        break;
                                    }
                                }

                                if( !alreadyInGame ){
                                    try{
                                        Main.PlayerList.get(i).getPlayerConnector().printWriter.println( "PlayerInvitesYouToPlay"+ "█" + tokens[1]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        break;
                    }

                    case "logOut" : {
                        for(int i=0; i < Main.PlayerList.size(); i++){
                            if( Main.PlayerList.get(i).getName().equals( tokens[1] ) ){
                                Main.PlayerList.remove(i);
                            }
                        }

                        for(int i=0; i < Main.PlayerList.size(); i++){
                            Main.PlayerList.get(i).getPlayerConnector().printWriter.println( "removePlayerFromList"+ "█" + tokens[1]);
                        }

                        this.socket = null;
                        this.playerName = "";
                        this.player = null;

                        break;
                    }

                    case "iRejectTheInvitationToCheckers" : {

                        for(int i=0; i < Main.PlayerList.size(); i++){
                            if( Main.PlayerList.get(i).getName().equals( tokens[2] ) ){
                                Main.PlayerList.get(i).getPlayerConnector().printWriter.println( "thePlayerRejectTheInvitationToPlayCheckers"+ "█" + tokens[1]);
                            }
                        }
                        break;
                    }

                    case "iAcceptTheInvitationToCheckers" : {
                        int p1=-1;
                        int p2=-1;

                        for(int i=0; i < Main.PlayerList.size(); i++) {
                            if( Main.PlayerList.get(i).getName().equals( tokens[1] ) ) { p1 = i; }
                            if( Main.PlayerList.get(i).getName().equals( tokens[2] ) ) { p2 = i; }
                        }

                        if( p1 != -1 && p2 != -1  ){
                            CheckersGame checkersGame = new CheckersGame(Main.PlayerList.get(p1), Main.PlayerList.get(p2));
                            Main.CheckersGameList.add( checkersGame );
                        }
                        break;
                    }

                    case "iMakeAMove" : {

                        for(int i=0; i<Main.CheckersGameList.size(); i++){
                            if( Main.CheckersGameList.get(i).whitePlayerName.equals( playerName ) || Main.CheckersGameList.get(i).blackPlayerName.equals( playerName ) ){
                                Main.CheckersGameList.get(i).makeAMove(tokens[1]);
                            }
                        }
                        break;
                    }

                    case "iWon" : {
                        for(int i=0; i<Main.CheckersGameList.size(); i++){
                            if( Main.CheckersGameList.get(i).whitePlayerName.equals( playerName ) || Main.CheckersGameList.get(i).blackPlayerName.equals( playerName ) ){
                                Main.CheckersGameList.get(i).informPlayerThatHeLost(tokens[1]);
                                Main.CheckersGameList.remove(i);
                            }
                        }
                        break;
                    }

                    case "iGiveUp" : {
                        for(int i=0; i<Main.CheckersGameList.size(); i++){
                            if( Main.CheckersGameList.get(i).whitePlayerName.equals( playerName ) || Main.CheckersGameList.get(i).blackPlayerName.equals( playerName ) ){
                                Main.CheckersGameList.get(i).informPlayerThatOponentGaveUp( tokens[1] );
                                Main.CheckersGameList.remove(i);
                            }
                        }
                        break;
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            for(int i=0; i < Main.PlayerList.size(); i++){
                Main.PlayerList.get(i).getPlayerConnector().printWriter.println( "removePlayerFromList"+ "█" + playerName);
            }

            for(int i=0; i < Main.PlayerList.size(); i++){
                if( Main.PlayerList.get(i).getName().equals( playerName ) ){
                    Main.PlayerList.remove(i);
                }
            }
        }

    }

}
