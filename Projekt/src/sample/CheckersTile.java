package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class CheckersTile extends Rectangle {

    int x_height;
    int y_width;

    boolean color;

    public CheckersTile(int x, int y, boolean color ){
        this.x_height = x;
        this.y_width = y;
        this.color = color;

        setWidth(CheckersController.field_size);
        setHeight(CheckersController.field_size);

        relocate(CheckersController.field_size * y, CheckersController.field_size * x);

        if(this.color){
            setFill( CheckersController.light );
        }else {
            setFill( CheckersController.dark );
        }


        setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {

                /*
                if(CheckersController.isMyTurn == true ){
                    if( CheckersController.colorOfPlayerPawns==true ){
                        if( CheckersController.boardState[x_height][y_width] == 1 && CheckersController.boardState[x_height][y_width] == 2 ){
                            CheckersController.highlightPossibleMoves(x_height, y_width);
                        }
                    }else {
                        if( CheckersController.boardState[x_height][y_width] == 3 && CheckersController.boardState[x_height][y_width] == 4 ) {
                            CheckersController.highlightPossibleMoves(x_height, y_width);
                        }
                    }
                }
                */
            }
        });

    }
}
