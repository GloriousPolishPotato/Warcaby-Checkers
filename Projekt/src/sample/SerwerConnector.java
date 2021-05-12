package sample;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.Timer;
import java.util.TimerTask;


public class SerwerConnector extends Thread {

    private Boolean isPlayerConnected;

    public SerwerConnector(){
        isPlayerConnected = false;
    }

    public void connect (String address) throws Exception {

        if (isPlayerConnected == false) {
            try {

                Main.SerwerSocket = new Socket(address, 29090);
                Main.SerwerReader = new BufferedReader(new InputStreamReader(Main.SerwerSocket.getInputStream()));
                Main.SerwerWriter = new PrintWriter(Main.SerwerSocket.getOutputStream(), true);
                isPlayerConnected = true;

                start();
            }
            catch (Exception error) {
                reset();
                throw error;
            }
        }
    }

    private void reset() {

        Main.SerwerSocket = null;
        Main.SerwerReader = null;
        Main.SerwerWriter = null;
        isPlayerConnected = false;
    }

    public void run(){
        String messageForSerwer = "";

        //
        try{
            while (true) {
                messageForSerwer = Main.SerwerReader.readLine();
                System.out.println("Serwer: " + messageForSerwer);

                String[] tokens = messageForSerwer.split("█");

                switch (tokens[0].trim()) {

                    case "logIn" : {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Parent MainWindowView = null;
                                try {
                                    MainWindowView = FXMLLoader.load(getClass().getResource("MainWindowView.fxml"));
                                } catch (IOException e) { e.printStackTrace(); }
                                Main.window.setScene(new Scene(MainWindowView));
                                Main.window.show();
                                Main.window.setOnCloseRequest(e -> Main.closeProgram());
                            }
                        });

                    }
                    break;

                    case "nickIsTaken" : {
                        ConnectViewController.NickTakenErrorStatic.setOpacity(1);
                    }
                    break;

                    case "addPlayerToList" : {

                        Player player = new Player(tokens[1], tokens[2]);
                        Main.PlayerList.add(player);
                        Main.synchronizePlayersList();
                    }
                    break;

                    case "removePlayerFromList" : {

                        for( int i=0; i< Main.PlayerList.size(); i++){
                            if( Main.PlayerList.get(i).getName().equals( tokens[1] ) ){
                                Main.PlayerList.remove(i);
                            }
                        }
                        Main.synchronizePlayersList();
                    }
                    break;

                    case "StartCheckersGame" : {


                        if(  tokens[1].equals("white") ) {
                            CheckersController.colorOfPlayerPawns = true;
                            CheckersController.isMyTurn = true;
                        }
                        else {
                            CheckersController.colorOfPlayerPawns = false;
                            CheckersController.isMyTurn = false;
                        }


                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Parent Checkers = null;
                                try {
                                    Checkers = FXMLLoader.load(getClass().getResource("Checkers.fxml"));
                                } catch (IOException e) { e.printStackTrace(); }
                                Main.window.setScene(new Scene(Checkers));
                                Main.window.show();
                                Main.window.setOnCloseRequest(e -> Main.closeProgram());
                            }
                        });
                        break;
                    }

                    case "UpdateBoard" : {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                CheckersController.updateBoardAccordingToServer(tokens[1]);
                            }
                        });
                        break;
                    }

                    case "youLost" : {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                CheckersController.updateBoardAccordingToServer(tokens[1]);
                                CheckersController.youLost();
                            }
                        });
                        break;
                    }

                    case "oponentGaveUp" : {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                CheckersController.opponentGaveUp();
                            }
                        });
                        break;
                    }

                    case "thePlayerRejectTheInvitationToPlayCheckers" : {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                Stage window = new Stage();
                                window.initModality(Modality.APPLICATION_MODAL);
                                window.setTitle("Wiadomość");
                                window.setWidth(400);
                                window.setHeight(200);

                                Label label = new Label();
                                label.setWrapText(true);

                                TextFlow flow = new TextFlow();
                                flow.setTextAlignment(TextAlignment.CENTER);

                                Text text1=new Text("   " + tokens[1]);
                                text1.setStyle("-fx-font-weight: bold");
                                Text text2=new Text(" odrzucił twoje zaproszenie do gry.");
                                text2.setStyle("-fx-font-weight: regular");
                                flow.getChildren().addAll(text1, text2);

                                Button button = new Button("OK");
                                button.setOnAction(e->{
                                    window.close();
                                });

                                VBox vBox = new VBox( 10 );
                                HBox hBox = new HBox(10);
                                hBox.setAlignment(Pos.CENTER);
                                hBox.getChildren().addAll(button);
                                vBox.getChildren().addAll(flow, hBox);

                                vBox.setAlignment(Pos.CENTER);

                                Scene scene = new Scene(vBox);
                                window.setScene(scene);

                                window.show();
                            }
                        });

                        break;
                    }


                    case "PlayerInvitesYouToPlay" : {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                Stage window = new Stage();
                                window.initModality(Modality.APPLICATION_MODAL);
                                window.setTitle("Zaproszenie do gry!");
                                window.setWidth(400);
                                window.setHeight(200);

                                Label label = new Label();
                                label.setWrapText(true);

                                TextFlow flow = new TextFlow();
                                flow.setTextAlignment(TextAlignment.CENTER);

                                Text text1=new Text("   " + tokens[1]);
                                text1.setStyle("-fx-font-weight: bold");
                                Text text2=new Text(" zaprosił cię do gry w warcaby. Zaakceptować? ");
                                text2.setStyle("-fx-font-weight: regular");
                                flow.getChildren().addAll(text1, text2);

                                Button button = new Button("TAK");
                                button.setOnAction(e->{
                                    Main.SerwerWriter.println( "iAcceptTheInvitationToCheckers" + "█" + Main.PlayerName + "█" + tokens[1] );
                                    window.close();
                                });
                                Button button2 = new Button("NIE");
                                button2.setOnAction(e->{
                                    Main.SerwerWriter.println( "iRejectTheInvitationToCheckers" + "█" + Main.PlayerName + "█" + tokens[1] );
                                    window.close();
                                });

                                VBox vBox = new VBox( 10 );
                                HBox hBox = new HBox(10);
                                hBox.setAlignment(Pos.CENTER);
                                hBox.getChildren().addAll(button, button2);
                                vBox.getChildren().addAll(flow, hBox);

                                vBox.setAlignment(Pos.CENTER);

                                Scene scene = new Scene(vBox);
                                window.setScene(scene);

                                window.show();
                            }
                        });
                    }
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
