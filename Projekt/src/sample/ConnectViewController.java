package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.swing.text.LabelView;
import java.io.IOException;

public class ConnectViewController{

    @FXML public TextField SerwerAdressTextField;
    @FXML public TextField PlayerNameTextField;

    @FXML public Label SerwerConnectionError;
    @FXML public Label NickTakenError;
    public static Label NickTakenErrorStatic;

    @FXML public Button ConnectToSerwerButton;
    @FXML public Button PlayButton;

    public void initialize(){

        Main.isPlayerConnected = false;

        NickTakenErrorStatic = NickTakenError;

        SerwerConnectionError.setOpacity(0);
        NickTakenErrorStatic.setOpacity(0);

        PlayerNameTextField.setDisable(true);
        PlayButton.setDisable(true);
    }


    public void onConnectToSerwerButtonClick(ActionEvent actionEvent) {

        SerwerConnectionError.setOpacity(0);

        try{
            Main.serwerConnector.connect(SerwerAdressTextField.getText().trim());

            if(SerwerAdressTextField.getText().equals("")){
                Main.serwerConnector.connect("localhost");
            }else{
                Main.serwerConnector.connect(SerwerAdressTextField.getText().trim());
            }

            SerwerConnectionError.setOpacity(1);
            SerwerConnectionError.setText("Połączono z serwerem");
            SerwerConnectionError.setTextFill( Color.valueOf("#006D14") );

            SerwerAdressTextField.setDisable(true);
            ConnectToSerwerButton.setDisable(true);

            PlayerNameTextField.setDisable(false);
            PlayButton.setDisable(false);

            Main.isPlayerConnected = true;

        } catch (Exception e) {
            SerwerConnectionError.setOpacity(1);
            SerwerConnectionError.setText("Nie udało się połączyć z serwerem");
            e.printStackTrace();
        }
    }

    public void onPlayButtonClick(ActionEvent actionEvent) throws IOException {

        if( PlayerNameTextField.getText().length() < 4 || PlayerNameTextField.getText().length() >16 ){
            NickTakenError.setText(" Wybrana nazwa gracza musi mieć długość od 4 do 16 znaków");

        }else {
            Main.PlayerName = PlayerNameTextField.getText();
            Main.SerwerWriter.println( "letMeIn" + "█" + Main.PlayerName );
        }
    }
}
