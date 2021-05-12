package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class CheckersHighlight extends  Rectangle{

    int x_height;
    int y_width;
    int pawnX;
    int pawnY;
    int enemyPawnX;
    int enemyPawnY;

    public CheckersHighlight(int x, int y){
        setOpacity(0);
        setWidth(0);
        setHeight(0);
        relocate(CheckersController.field_size * y, CheckersController.field_size * x);
    }

    public CheckersHighlight(int x, int y, int pawnX, int pawnY, int enemyPawnX, int enemyPawnY ){

        //UWAGA
        System.out.println("konstruktor highlight dla " + x + ", " + y);

        this.x_height = x;
        this.y_width = y;
        this.pawnX = pawnX;
        this.pawnY = pawnY;
        this.enemyPawnX = enemyPawnX;
        this.enemyPawnY = enemyPawnY;

        setOpacity(0.2);    //zabaczy się

        setWidth(CheckersController.field_size);
        setHeight(CheckersController.field_size);

        if( CheckersController.colorOfPlayerPawns == true ){
            relocate(CheckersController.field_size * y, CheckersController.field_size * x);
        }else {
            relocate(CheckersController.field_size * (7-y), CheckersController.field_size * (7-x));
        }

        setFill( CheckersController.highlight );

        setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                //move the pawn
                CheckersController.boardState[x_height][y_width] = CheckersController.boardState[pawnX][pawnY];
                //remove the pawn from old position
                CheckersController.boardState[pawnX][pawnY] = 0;
                //remove the enemy pawn
                if( enemyPawnX != -1 ){
                    CheckersController.boardState[enemyPawnX][enemyPawnY] = 0;
                }

                boolean isPawnEvolving = false;

                if( x_height == 7 || x_height == 0 ){

                    if( CheckersController.boardState[x_height][y_width] == 1 ){
                        CheckersController.boardState[x_height][y_width] = 2;
                    }else{
                        CheckersController.boardState[x_height][y_width] = 4;
                    }

                    isPawnEvolving = true;
                }

                CheckersController.updateBoardState();
                CheckersController.turnOfHighlight();

                if( enemyPawnX != -1 && isPawnEvolving == false ) {
                    CheckersController.checkIfThereIsNextMoveAvailable(x_height, y_width);
                }else {
                    CheckersController.endTurn();
                }

            }
        });
    }
}
