package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class CheckersController {

    @FXML
    GridPane gridPane;
    static GridPane gridPaneStatic;

    public static final int field_size = 100;

    public static Color light = Color.LIGHTBLUE;
    public static Color dark = Color.BROWN;
    public static Color highlight = Color.YELLOW;

    public static Image whitePawnImage;
    public static Image blackPawnImage;

    public static Image whiteQueenImage;
    public static Image blackQueenImage;

    private static Group tilesGrup = new Group();
    private static Group pawnsGrup = new Group();
    private static Group highlightGroup = new Group();

    // white:true, black:false
    public static boolean colorOfPlayerPawns;

    // the state of the game hold in table
    public static int[][] boardState = new int [8][8];

    // determines whether the player can move
    public static boolean isMyTurn;

    public static boolean isThisForcedMove;

    public void initialize(){

        Main.window.setMinHeight(840);
        Main.window.setMinWidth(830);
        Main.window.setResizable(false);

        Main.isPlayerInGame = true;

        gridPaneStatic = gridPane;

        whitePawnImage = new Image(getClass().getResourceAsStream("/media/whitePawn_classic_1.png"));
        blackPawnImage = new Image(getClass().getResourceAsStream("/media/blackPawn_classic_1.png"));

        whiteQueenImage = new Image(getClass().getResourceAsStream("/media/whiteQueen_classic_1.png"));
        blackQueenImage = new Image(getClass().getResourceAsStream("/media/blackQueen_classic_1.png"));



        // setting the beginning state of the board
        for( int i=0; i<8; i++ ){
            for( int j=0; j<8; j++ ){

                if( (i+j)%2 == 0 || (i>2 && i<5) ){ boardState[i][j]=0; }
                else if( i<3 ){ boardState[i][j]=3; }
                else if( i>4 ){ boardState[i][j]=1; }

            }
        }

        updatedLookOfTheBoard();
        updateBoardState();

        gridPaneStatic.setConstraints(tilesGrup, 0, 0);
        gridPaneStatic.getChildren().add(tilesGrup);

        gridPaneStatic.setConstraints(pawnsGrup,0,0);
        gridPaneStatic.getChildren().add(pawnsGrup);

        //gridPaneStatic.setAlignment(Pos.TOP_LEFT);

        gridPaneStatic.setConstraints(highlightGroup, 0, 0);
        gridPaneStatic.getChildren().add(highlightGroup);

        isThisForcedMove = false;

        Main.window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                if( Main.isPlayerInGame ){
                    displayUponEscPressed();
                }
            }
        });
    }


    public static void updateBoardState(){

        pawnsGrup.getChildren().clear();

        //Thanks to that pawns display properly
        CheckersPawn temp1 = new CheckersPawn(0, 0);
        temp1.toBack();
        pawnsGrup.getChildren().add(temp1);
        CheckersPawn temp2 = new CheckersPawn(8, 8);
        temp2.toBack();
        pawnsGrup.getChildren().add(temp2);

        for( int i=0; i<8; i++ ){
            for( int j=0; j<8; j++ ){

                if( boardState[i][j] != 0 ){
                    CheckersPawn pawn = new CheckersPawn(i, j, boardState[i][j]);
                    pawnsGrup.getChildren().add(pawn);
                }
            }
        }
    }


    //draw the board
    public static void updatedLookOfTheBoard(){

        tilesGrup.getChildren().clear();

        for( int i=0; i<8; i++ ){
            for( int j=0; j<8; j++ ){
                if( (i+j)%2 == 0 ){
                    CheckersTile tile = new CheckersTile(i, j, true);
                    tilesGrup.getChildren().add(tile);
                }else{
                    CheckersTile tile = new CheckersTile(i, j, false);
                    tilesGrup.getChildren().add(tile);
                }
            }
        }
    }

    public static void highlightPossibleMoves( int x, int y){

        highlightGroup.getChildren().clear();

        //Thanks to that highlighted rectangles display properly
        CheckersHighlight temp1 = new CheckersHighlight(0, 0);
        temp1.toBack();
        highlightGroup.getChildren().add(temp1);
        CheckersHighlight temp2 = new CheckersHighlight(8, 8);
        temp2.toBack();
        highlightGroup.getChildren().add(temp2);


        //checking if the player chooses his pawn. If so, check if it is queen.
        boolean isThisPlayerPawn = false;
        boolean isThisQueen = false;
        if( colorOfPlayerPawns == true ){
            if( boardState[x][y] == 1 ){ isThisPlayerPawn = true; }
            if( boardState[x][y] == 2 ){ isThisPlayerPawn = true; isThisQueen = true; }
        }else{
            if( boardState[x][y] == 3 ){ isThisPlayerPawn = true; }
            if( boardState[x][y] == 4 ){ isThisPlayerPawn = true; isThisQueen = true; }
        }


        if( isThisPlayerPawn == true ){


            //some variables
            int direction = colorOfPlayerPawns ? -1 : 1;
            int enemyPawn = colorOfPlayerPawns ? 3 : 1;
            int enemyQueen = colorOfPlayerPawns ? 4 : 2;
            int myPawn = colorOfPlayerPawns ? 1 : 3;
            int myQueen = colorOfPlayerPawns ? 2 : 4;


            if(isThisQueen==false){


                //possible movement to the left
                if( x + direction < 8 && x + direction >= 0 && y-1 < 8 && y-1 >= 0 ){

                    if( boardState[x + direction][y-1] == 0 ){

                        CheckersHighlight highlight = new CheckersHighlight(x + direction, y-1, x, y, -1, -1);
                        highlightGroup.getChildren().add(highlight);

                    }
                    if( boardState[x + direction][y-1] == enemyPawn || boardState[x + direction][y-1] == enemyQueen ){
                        if( x + (direction*2) <  8 && x + (direction*2)  >= 0 && y-2 < 8 && y-2 >= 0 ){
                            if( boardState[x + (direction*2 )][y-2] == 0 ){

                                CheckersHighlight highlight = new CheckersHighlight(x + (direction*2 ), y-2, x, y, x + direction, y-1);
                                highlightGroup.getChildren().add(highlight);
                            }
                        }
                    }
                }


                //possible movement to the right
                if( x + direction < 8 && x + direction >= 0 && y+1 < 8 && y+1 >= 0 ){

                    if( boardState[x + direction][y+1] == 0 ){

                        CheckersHighlight highlight = new CheckersHighlight(x + direction, y+1, x, y, -1, -1);
                        highlightGroup.getChildren().add(highlight);

                    }
                    if( boardState[x + direction][y+1] == enemyPawn || boardState[x + direction][y+1] == enemyQueen ){
                        if( x + (direction*2) <  8 && x + (direction*2)  >= 0 && y+2 < 8 && y+2 >= 0 ){
                            if( boardState[x + (direction*2 )][y+2] == 0 ){

                                CheckersHighlight highlight = new CheckersHighlight(x + (direction*2 ), y+2, x, y, x + direction, y+1);
                                highlightGroup.getChildren().add(highlight);

                            }
                        }
                    }
                }
            }

            if(isThisQueen==true){

                //Loop for front and back
                for( int i = -1; i <= 1; i = i+2 ){

                    //loop for left and right
                    for( int j = -1; j <= 1; j = j+2){

                        for( int k=0; k < 8 ; k++ ){

                            if( (x + i*k > 7) || ( x + i*k < 0 )  || ( y + j*k > 7) || ( y + j*k < 0) ){ break; }

                            if( boardState[x + i*k][y + j*k] == 0 ){
                                CheckersHighlight highlight = new CheckersHighlight(x + i*k, y + j*k, x, y, -1, -1);
                                highlightGroup.getChildren().add(highlight);
                            }

                            if( (boardState[x + i*k][y + j*k] == myPawn || boardState[x + i*k][y + j*k] == myQueen) && (x+i*k != x) && ( y+j*k != y ) ){
                                break;
                            }

                            if( boardState[x + i*k][y + j*k] == enemyPawn || boardState[x + i*k][y + j*k] == enemyQueen ){

                                if( (x + i*(k+1) > 7) || ( x + i*(k+1) < 0 )  || ( y + j*(k+1) > 7) || ( y + j*(k+1) < 0) ){ break; }

                                if( boardState[x + i*(k+1)][y + j*(k+1)] == 0 ){
                                    CheckersHighlight highlight = new CheckersHighlight(x + i*(k+1), y + j*(k+1), x, y, x + i*k, y + j*k);
                                    highlightGroup.getChildren().add(highlight);
                                    break;
                                }else{break;}
                            }
                        }
                    }
                }


            }
        }
    }

    public static void turnOfHighlight(){
        highlightGroup.getChildren().clear();

        //Thanks to that highlighted rectangles display properly
        CheckersHighlight temp1 = new CheckersHighlight(0, 0);
        temp1.toBack();
        highlightGroup.getChildren().add(temp1);
        CheckersHighlight temp2 = new CheckersHighlight(8, 8);
        temp2.toBack();
        highlightGroup.getChildren().add(temp2);
    }

    public static void checkIfThereIsNextMoveAvailable(int x, int y){

        isThisForcedMove = false;

        Boolean isThereAMove = false;

        Boolean isThisQueen = false;
        if( boardState[x][y] == 2 || boardState[x][y] == 4 ){ isThisQueen = true; }

        int direction = colorOfPlayerPawns ? -1 : 1;
        int enemyPawn = colorOfPlayerPawns ? 3 : 1;
        int enemyQueen = colorOfPlayerPawns ? 4 : 2;

        if( !isThisQueen ){

            for(int i = -1; i <= 1; i=i+2){
                if( x + direction < 8 && x + direction >= 0 && y+i < 8 && y+i >= 0 ){
                    if( boardState[x + direction][y+i] == enemyPawn || boardState[x + direction][y+i] == enemyQueen ){
                        if( x + (direction*2) <  8 && x + (direction*2)  >= 0 && y+(2*i) < 8 && y+(2*i) >= 0 ){
                            if( boardState[x + (direction*2 )][y+(2*i)] == 0 ){
                                CheckersHighlight highlight = new CheckersHighlight(x + (direction*2 ), y+(2*i), x, y, x + direction, y+i);
                                highlightGroup.getChildren().add(highlight);
                                isThereAMove = true;
                            }
                        }
                    }
                }
            }
        }else{

            //Loop for front and back
            for( int i = -1; i <= 1; i = i+2 ){

                //loop for left and right
                for( int j = -1; j <= 1; j = j+2){

                    for( int k=0; k < 8 ; k++ ){

                        if( (x + i*k > 7) || ( x + i*k < 0 )  || ( y + j*k > 7) || ( y + j*k < 0) ){ break; }

                        if( boardState[x + i*k][y + j*k] == enemyPawn || boardState[x + i*k][y + j*k] == enemyQueen ){

                            if( (x + i*(k+1) > 7) || ( x + i*(k+1) < 0 )  || ( y + j*(k+1) > 7) || ( y + j*(k+1) < 0) ){ break; }

                            if( boardState[x + i*(k+1)][y + j*(k+1)] == 0 ){
                                CheckersHighlight highlight = new CheckersHighlight(x + i*(k+1), y + j*(k+1), x, y, x + i*k, y + j*k);
                                highlightGroup.getChildren().add(highlight);
                                isThereAMove = true;
                                break;
                            }else{break;}

                        }
                    }
                }
            }
        }

        if( isThereAMove == false ){
            endTurn();
        } else {
            isThisForcedMove = true;
        }

    }

    public static void endTurn(){

        isMyTurn = false;

        boolean isThatWin = true;

        for(int i=0; i<8 ; i++ ){
            for(int j=0; j<8; j++){
                if( colorOfPlayerPawns ){
                    if( boardState[i][j] == 3  || boardState[i][j] == 4){ isThatWin=false; }
                }else {
                    if( boardState[i][j] == 1  || boardState[i][j] == 2){ isThatWin=false; }
                }
            }
        }


        String boardStateString = "";

        for(int i=0; i<8 ; i++ ){
            for(int j=0; j<8; j++){
                if( !(i==7 && j==7 ) ){
                    boardStateString += String.valueOf( boardState[i][j] ) + "#";
                }
            }
        }

        boardStateString += String.valueOf( boardState[7][7] );

        if( !isThatWin ){
            Main.SerwerWriter.println( "iMakeAMove" + "█" + boardStateString );
        }else {
            Main.SerwerWriter.println( "iWon" + "█" + boardStateString );


            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Wiadomość");
            window.setWidth(400);
            window.setHeight(200);

            Label label = new Label();
            label.setWrapText(true);

            TextFlow flow = new TextFlow();
            flow.setTextAlignment(TextAlignment.CENTER);

            Text text1=new Text(" Gratulacje wygrałeś ");
            text1.setStyle("-fx-font-weight: bold");

            flow.getChildren().addAll(text1);

            Button button = new Button("OK");
            button.setOnAction(e->{
                window.close();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent MainWindowView = null;
                        try {
                            MainWindowView = FXMLLoader.load(getClass().getResource("MainWindowView.fxml"));
                        } catch (IOException e) { e.printStackTrace(); }
                        Main.isPlayerInGame = false;

                        Main.window.setMinWidth(600);
                        Main.window.setMinHeight(400);
                        Main.window.setWidth(600);
                        Main.window.setHeight(400);
                        Main.window.setResizable(true);
                        Main.window.setScene(new Scene(MainWindowView));
                        Main.window.show();
                        Main.window.setOnCloseRequest(e -> Main.closeProgram());

                    }
                });
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
    }



    public static void updateBoardAccordingToServer(String s){

        isMyTurn = true;

        String[] tokens = s.split("#");

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                boardState[i][j] = Integer.parseInt(tokens[(i*8)+j].trim());
            }
        }

        isThisForcedMove = false;

        updateBoardState();
    }

    public static void youLost(){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Wiadomość");
        window.setWidth(400);
        window.setHeight(200);

        Label label = new Label();
        label.setWrapText(true);

        TextFlow flow = new TextFlow();
        flow.setTextAlignment(TextAlignment.CENTER);

        Text text1=new Text(" Tym razem się nie udało. Powodzenia następnym razem. ");
        text1.setStyle("-fx-font-weight: bold");

        flow.getChildren().addAll(text1);

        Button button = new Button("OK");
        button.setOnAction(e->{
            window.close();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Parent MainWindowView = null;
                    try {
                        MainWindowView = FXMLLoader.load(getClass().getResource("MainWindowView.fxml"));
                    } catch (IOException e) { e.printStackTrace(); }
                    Main.isPlayerInGame = false;

                    Main.window.setMinWidth(600);
                    Main.window.setMinHeight(400);
                    Main.window.setWidth(600);
                    Main.window.setHeight(400);
                    Main.window.setResizable(true);
                    Main.window.setScene(new Scene(MainWindowView));
                    Main.window.show();
                    Main.window.setOnCloseRequest(e -> Main.closeProgram());
                }
            });
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

    public static void opponentGaveUp(){

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Wiadomość");
        window.setWidth(400);
        window.setHeight(200);

        Label label = new Label();
        label.setWrapText(true);

        TextFlow flow = new TextFlow();
        flow.setTextAlignment(TextAlignment.CENTER);

        Text text1=new Text(" Twój przeciwnik się poddał. Wygrałeś! ");
        text1.setStyle("-fx-font-weight: bold");

        flow.getChildren().addAll(text1);

        Button button = new Button("OK");
        button.setOnAction(e->{
            window.close();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Parent MainWindowView = null;
                    try {
                        MainWindowView = FXMLLoader.load(getClass().getResource("MainWindowView.fxml"));
                    } catch (IOException e) { e.printStackTrace(); }
                    Main.isPlayerInGame = false;

                    Main.window.setMinWidth(600);
                    Main.window.setMinHeight(400);
                    Main.window.setWidth(600);
                    Main.window.setHeight(400);
                    Main.window.setResizable(true);
                    Main.window.setScene(new Scene(MainWindowView));
                    Main.window.show();
                    Main.window.setOnCloseRequest(e -> Main.closeProgram());
                }
            });
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

    public static void displayUponEscPressed(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Menu");
        window.setWidth(400);
        window.setHeight(200);


        Button button = new Button("Poddaj partię");
        button.setOnAction(e->{
            Main.SerwerWriter.println( "iGiveUp" +  "█" + colorOfPlayerPawns );
            window.close();
            youLost();
        });


        String st[] = { "Klasyczny", "klasyczny_2", "weeb" };

        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList(st));
        choiceBox.getSelectionModel().select(0);

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue ov, Number value, Number new_value)
            {
                CheckersPawn pawn = new CheckersPawn(0,0);
                pawn.changeTheAppearanceOfPawns(st[new_value.intValue()]);
                choiceBox.getSelectionModel().select(new_value.intValue());
            }
        });

        VBox vBox = new VBox( 10 );
        HBox hBox = new HBox(10);
        vBox.getChildren().addAll(button, choiceBox);

        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        window.setScene(scene);

        window.show();
    }
}

