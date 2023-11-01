package com.tiktaktoe;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {

    private final int BUTTON_SIZE = 100;
    private final int TABLE_SIZE = 3;
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 300;
    private int playerIndex = 1;
    private Stage window;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        GridPane root = new GridPane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        Label playerInfo = new Label(playerInfo());
        playerInfo.setFont(Font.font(20));

        // Spielfield initialisieren
        int[][] table = new int[TABLE_SIZE][TABLE_SIZE];

        for (int col = 0; col < TABLE_SIZE; col++) {
            for (int row = 0; row < TABLE_SIZE; row++) {
                Button field = new Button();
                field.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
                field.setStyle("-fx-border-color: black");

                int finalRow = row;
                int finalCol = col;

                field.setOnMouseClicked(e -> {
                    table[finalRow][finalCol] = playerIndex;
                    handlePlayerIndex(field, playerInfo);
                    int winner = checkCombination(table);

                    if (winner != -1) {
                        gameOverScreen(scene, table);
                    }
                });
                root.add(field, col, row);
            }
        }

        root.add(playerInfo, 3, 0);
        GridPane.setMargin(playerInfo, new Insets(10));

        primaryStage.setScene(scene);
        primaryStage.setTitle("TikTakToe");
        primaryStage.show();
    }

    private void gameOverScreen(Scene scene, int[][] table) {
        BorderPane gameOverScreen = new BorderPane();
        scene.setRoot(gameOverScreen);
        scene.getStylesheets().add(Main.class.getResource("/gameOverScreen.css").toExternalForm());

        String winner = String.valueOf(checkCombination(table));
        Label label = new Label("Game over \nPlayer " + winner + " won");
        label.setTextAlignment(TextAlignment.CENTER);
        Button retryButton = new Button("", new ImageView(new Image("restart_icon.png")));

        retryButton.setOnMouseClicked(e -> start(window));

        gameOverScreen.setCenter(label);
        gameOverScreen.setBottom(retryButton);
        BorderPane.setAlignment(retryButton, Pos.CENTER);
    }

    private int checkCombination(int[][] table) {
        // Überprüfe horizentale Gewinnkombination
        for (int[] i : table) {
            if (i[0] == i[1] && i[0] != 0 && i[1] == i[2]) {
                return i[0];
            }
        }

        // Überprüfe vertikale Gewinnkombination
        for (int c = 0; c < 3; c++) {
            if (table[0][c] == table[1][c] && table[0][c] != 0 && table[1][c] == table[2][c]) {
                return table[0][c];
            }
        }

        // Überprüfe diagonale Gewinnkombination
        if (table[0][0] == table[1][1] && table[0][0] != 0 && table[1][1] == table[2][2]) {
            return table[1][1];
        } else if (table[0][2] == table[1][1] && table[0][2] != 0 && table[1][1] == table[2][0]) {
            return table[1][1];
        }

        return -1;
    }

    private void handlePlayerIndex(Button b, Label turnLabel) {
        if (b.getText().isEmpty()) {
            b.setFont(Font.font(40));
            if (playerIndex == 1) {
                b.setText("❌");
                b.setTextFill(Color.RED);
                playerIndex++;
            } else {
                b.setText("⭕");
                b.setTextFill(Color.BLUE);
                playerIndex--;
            }
            turnLabel.setText(playerInfo());
        }
    }

    private String playerInfo() {
        return String.format("Player %d's turn", playerIndex);
    }

    public static void main(String[] args) {
        launch();
    }
}
