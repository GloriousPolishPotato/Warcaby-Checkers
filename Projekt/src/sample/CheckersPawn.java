package sample;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CheckersPawn extends ImageView {

    int x_height;
    int y_width;

    boolean color;
    boolean queen;

    Image image;

    public CheckersPawn(int x, int y){
        setFitHeight(0);
        setFitWidth(0);

        relocate(CheckersController.field_size * y, CheckersController.field_size * x);
    }

    public CheckersPawn(int x, int y, int type){
        this.x_height = x;
        this.y_width = y;

        switch (type){
            case 1 : { color=true; queen=false; image = CheckersController.whitePawnImage; break; }
            case 2 : { color=true; queen=true; image = CheckersController.whiteQueenImage; break; }

            case 3 : { color=false; queen=false; image = CheckersController.blackPawnImage; break; }
            case 4 : { color=false; queen=true; image = CheckersController.blackQueenImage; break; }
        }

        this.setImage(image);

        setFitHeight(CheckersController.field_size);
        setFitWidth(CheckersController.field_size);

        if( CheckersController.colorOfPlayerPawns == true ){
            relocate(CheckersController.field_size * y, CheckersController.field_size * x);
        }else {
            relocate(CheckersController.field_size * (7-y), CheckersController.field_size * (7-x));
        }


        if( CheckersController.isMyTurn == true && color==CheckersController.colorOfPlayerPawns ){
            setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                    if( CheckersController.isMyTurn == true && CheckersController.isThisForcedMove == false ){
                        CheckersController.highlightPossibleMoves(x_height, y_width);
                    }
                }
            });
        }
    }

    public void changeTheAppearanceOfPawns( String s ){

        System.out.println( s );

        if( s.equals( "Klasyczny" ) ){
            CheckersController.whitePawnImage = new Image(getClass().getResourceAsStream("/media/whitePawn_classic_1.png"));
            CheckersController.blackPawnImage = new Image(getClass().getResourceAsStream("/media/blackPawn_classic_1.png"));

            CheckersController.whiteQueenImage = new Image(getClass().getResourceAsStream("/media/whiteQueen_classic_1.png"));
            CheckersController.blackQueenImage = new Image(getClass().getResourceAsStream("/media/blackQueen_classic_1.png"));
            CheckersController.updateBoardState();
        }
        else if( s.equals("klasyczny_2") ){
            CheckersController.whitePawnImage = new Image(getClass().getResourceAsStream("/media/whitePawn_classic_2.png"));
            CheckersController.blackPawnImage = new Image(getClass().getResourceAsStream("/media/blackPawn_classic_2.png"));

            CheckersController.whiteQueenImage = new Image(getClass().getResourceAsStream("/media/whiteQueen_classic_2.png"));
            CheckersController.blackQueenImage = new Image(getClass().getResourceAsStream("/media/blackQueen_classic_2.png"));
            CheckersController.updateBoardState();
        }
        else if( s.equals("weeb") ){
            CheckersController.whitePawnImage = new Image(getClass().getResourceAsStream("/media/whitePawn_weeb.png"));
            CheckersController.blackPawnImage = new Image(getClass().getResourceAsStream("/media/blackPawn_weeb.png"));

            CheckersController.whiteQueenImage = new Image(getClass().getResourceAsStream("/media/whiteQueen_classic_2.png"));
            CheckersController.blackQueenImage = new Image(getClass().getResourceAsStream("/media/blackQueen_classic_2.png"));
            CheckersController.updateBoardState();
        }
    }
}
