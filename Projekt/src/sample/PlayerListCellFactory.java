package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;

public class PlayerListCellFactory extends ListCell<Player> {

    private HBox hbox = new HBox(4.0); // 8 points of gap between controls
    private Label label = new Label();


    public PlayerListCellFactory(){

        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(30);

        label.setWrapText(false);
        label.setTextAlignment(TextAlignment.LEFT);
        hbox.getChildren().add(label);

        HBox.setMargin(label,new Insets(0,4,0,4));
        setPrefWidth(USE_PREF_SIZE);
    }

    @Override
    protected void updateItem(Player player, boolean b) {
        super.updateItem(player, b);

        if (b || player == null) {
            setGraphic(null); // don't display anything
        }
        else{
            String playerName = player.getName();
            label.setText(playerName);

            setGraphic(hbox);
        }
    }
}
