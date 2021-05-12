package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Main extends Application {

    public static BufferedReader SerwerReader;
    public static PrintWriter SerwerWriter;
    public static Socket SerwerSocket;
    public static SerwerConnector serwerConnector;

    public static ArrayList<Player> PlayerList;

    public static String WindowTile = "Warcaby delux";

    public static Stage window;

    public Scene ConnectView;

    public static String PlayerName = "";
    public static boolean colorOfPlayerPawns;

    public static boolean isPlayerConnected;
    public static boolean isPlayerInGame = false ;

    @Override
    public void start(Stage primaryStage) throws Exception{

        PlayerList = new ArrayList< Player >();

        //
        PlayerList.add(new Player("Player0001", "Offline"));
        PlayerList.add(new Player("Player0002", "Offline"));
        PlayerList.add(new Player("Player0003", "Offline"));
        PlayerList.add(new Player("Player0004", "Offline"));
        //


        window = primaryStage;
        window.setMinWidth(600);
        window.setMinHeight(400);
        Parent root = FXMLLoader.load(getClass().getResource("ConnectView.fxml"));
        ConnectView = new Scene (root);
        window.setTitle(Main.WindowTile);

        window.setScene(ConnectView);
        window.show();


        window.setOnCloseRequest(e -> closeProgram());
    }


    public static void main(String[] args) {
        serwerConnector = new SerwerConnector();
        launch(args);
    }

    public static void closeProgram(){
        if( isPlayerConnected ){
            Main.SerwerWriter.println( "logOut" + "█" + Main.PlayerName );
        }
        if( isPlayerInGame ){
            Main.SerwerWriter.println( "iGiveUp" +  "█" + CheckersController.colorOfPlayerPawns );
        }
        window.close();
    }



    public static void synchronizePlayersList() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                final ObservableList<Player> playerObservableList = FXCollections.observableArrayList();
                playerObservableList.addAll(Main.PlayerList);


                if (MainWindowViewController.PlayerListViewStatic != null) {
                    MainWindowViewController.PlayerListViewStatic.setItems(playerObservableList);
                    MainWindowViewController.PlayerListViewStatic.scrollTo(Main.PlayerList.size()-1);
                }
            }
        });
    }
}
