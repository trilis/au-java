package me.trilis.au.java.test5;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static int n;
    private static Button[][] buttons;
    private static Controller controller;
    private static List<Thread> sleepingThreads = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        var grid = new GridPane();
        for (var r = 0; r < n; r++) {
            for (var c = 0; c < n; c++) {
                buttons[r][c] = new Button("");
                buttons[r][c].setMinWidth(50);
                buttons[r][c].setMinHeight(50);

                var i = r;
                var j = c;

                buttons[r][c].setOnAction(event -> {
                    for (var thread : sleepingThreads) {
                        thread.interrupt();
                    }
                    sleepingThreads.clear();

                    var button = (Button) event.getSource();
                    button.setText(String.valueOf(controller.getNumber(i, j)));

                    var result = controller.pushButton(i, j);
                    if (result == Controller.Result.NOTHING) {
                        return;
                    }

                    var lastCoordinates = controller.getLastCoordinatesId();
                    var lastButton = buttons[lastCoordinates.getKey()]
                            [lastCoordinates.getValue()];

                    if (result == Controller.Result.MATCH ||
                            result == Controller.Result.WIN) {
                        button.setDisable(true);
                        lastButton.setDisable(true);
                        button.setStyle("-fx-background-color: #ff0000; ");
                        lastButton.setStyle("-fx-background-color: #ff0000; ");
                    } else {
                        var th = new Thread(() -> {
                            button.setDisable(true);
                            lastButton.setDisable(true);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ignored) {

                            }

                            Platform.runLater(() -> {
                                button.setText("");
                                lastButton.setText("");
                                button.setDisable(false);
                                lastButton.setDisable(false);
                            });
                        });
                        th.start();
                        sleepingThreads.add(th);
                    }
                    if (result == Controller.Result.WIN) {
                        stage.setTitle("YOU WIN");
                    }
                });
                grid.add(buttons[r][c], c, r);
            }
        }
        var scrollPane = new ScrollPane(grid);
        stage.setScene(new Scene(scrollPane));
        stage.show();
    }


    public static void main(String[] args) {
        n = Integer.parseInt(args[0]);
        if (n < 4 || n % 2 != 0) {
            System.out.println("Invalid n: " + n +
                    ", it should be even and greater than 2.");
            System.exit(0);
        }
        buttons = new Button[n][n];
        controller = new Controller(n);
        launch();
    }
}
