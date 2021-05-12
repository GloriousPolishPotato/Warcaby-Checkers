package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class MainWindowViewController {

    @FXML public Label UserNameLabel;

    @FXML public ListView <Player> PlayersListView;

    @FXML public static ListView <Player> PlayerListViewStatic;

    @FXML public Button inviteToPlay;

    @FXML public Button logOutButton;

    public Player currentPlayer;

    public void initialize(){

        PlayerListViewStatic = PlayersListView;

        Main.synchronizePlayersList();

        UserNameLabel.setText( Main.PlayerName );

        inviteToPlay.setDisable(true);


        currentPlayer = null;

        PlayersListView.setCellFactory(new Callback<ListView<Player>, ListCell<Player>>() {
            @Override
            public ListCell<Player> call(ListView<Player> playerListView) {
                return new PlayerListCellFactory();
            }
        });



        PlayersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Player>() {
            @Override
            public void changed(ObservableValue<? extends Player> observableValue, Player oldVal, Player newVal) {

                if(newVal != null && newVal != currentPlayer) {

                    currentPlayer = newVal;
                    System.out.println("Current Player: " + currentPlayer.getName());
                    inviteToPlay.setText("Zaproś gracza " + currentPlayer.getName() + " do gry w warcaby");
                    inviteToPlay.setDisable(false);
                    }
                }
            }
        );
    }

    public void invitePlayerToPlayCheckers(ActionEvent actionEvent) {
        Main.SerwerWriter.println( "InvitePlayerToPlayCheckers" + "█" + Main.PlayerName + "█" + currentPlayer.getName() );
    }

    public void goToConnectview(ActionEvent actionEvent) {

        //Sending message to server
        Main.SerwerWriter.println( "logOut" + "█" + Main.PlayerName );

        //reset serverConnector and Player list
        Main.serwerConnector = new SerwerConnector();
        Main.PlayerList = new ArrayList<Player>();

        Parent ConnectView = null;
        try {
            ConnectView = FXMLLoader.load(getClass().getResource("ConnectView.fxml"));
        } catch (IOException e) { e.printStackTrace(); }
        Main.window.setScene(new Scene(ConnectView));
        Main.window.show();
        Main.window.setOnCloseRequest(e -> Main.closeProgram());
    }
}
