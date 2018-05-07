/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameoflife;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author jbber
 */
public class GameOfLife extends Application {

    final int WIDTH = 500; // window width
    final int HEIGHT = 500; // window height
    final int SCALE = 10; // grid size
    final int COLUMNS = WIDTH / SCALE;
    final int ROWS = HEIGHT / SCALE;

    // maps cells to position on board
    Map<String, Cell> boardMap = new HashMap<>();
    // allows boardMap to be iterated non-destructively
    Map<String, Cell> boardMapBuffer = new HashMap<>();

    // tracks the number of generations
    int generation = 0;

    StackPane table;
    Pane board;
    Text text;

    @Override
    public void start(Stage primaryStage) {

        // Initializes timeline
        final Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new EventHandler() {
            @Override
            public void handle(Event event) {
                updateBoard();
            }
        }), new KeyFrame(Duration.seconds(.1)));
        // }), new KeyFrame(Duration.millis(100)));

        // Timeline runs indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);

        table = new StackPane();
        board = new Pane();
        
        text = new Text(Integer.toString(generation));
        text.setFont(new Font(100));
        text.setFill(Color.rgb(255, 255, 0));

        seed();

        table.getChildren().add(board);
        Scene scene = new Scene(table);
        scene.setFill(Color.BLACK);

        //primaryStage.setResizable(false);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        //draw();
        // move this into draw later
        timeline.play();
    }

    private void seed() {

        for (int c = 0; c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {

                String position = c + " " + r;

                int xPos = c * SCALE;
                int yPos = r * SCALE;

                Cell cell = new Cell(xPos, yPos, SCALE - 1, SCALE - 1);

                // randomly spawns living cells
                boolean isAlive = ThreadLocalRandom.current().nextInt(0, 1 + 1) == 1;

                cell.setIsAlive(isAlive);

                boardMap.put(position, cell);

                board.getChildren().add(cell);

                
            }
        }
        generation++;
        text.setText(Integer.toString(generation));
        table.getChildren().add(text);
    }

    int liveNeighbours = 0; // tracks living neighbours
    int columnBuffer = 0;
    int rowBuffer = 0;
    String cellPosition;
    String checkPosition;

    private void updateBoard() {

        // loads map buffer with current state
        boardMapBuffer = boardMap;

        // iterates over each cell
        for (int c = 0; c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {

                liveNeighbours = 0;
                cellPosition = r + " " + c;
                Cell bufferCell = boardMapBuffer.get(cellPosition);

                // counts living neighbours
                // index ternary adjusts for edges
                for (columnBuffer = (c == 0) ? c : c - 1; columnBuffer < c + 2 && columnBuffer < COLUMNS - 1; columnBuffer++) {

                    for (rowBuffer = (r == 0) ? r : r - 1; rowBuffer < r + 2 && rowBuffer < ROWS - 1; rowBuffer++) {

                        checkPosition = columnBuffer + " " + rowBuffer;

                        if (boardMapBuffer.get(checkPosition).isAlive && !checkPosition.equals(cellPosition)) {
                            liveNeighbours++;
                        }
                    }
                }

                if (bufferCell.isAlive) {
                    if (liveNeighbours < 2 || liveNeighbours > 3) {
                        // cell dies due to underpopulation OR overpopulation
                        bufferCell.setIsAlive(false);
                    }
                } else if (liveNeighbours == 3) {
                    // neighbours reproduce
                    bufferCell.setIsAlive(true);
                }

            }
        }

        boardMap = boardMapBuffer;
        board.getChildren().clear();

        // updates board with new cells
        for (int c = 0; c < COLUMNS; c++) {
            for (int r = 0; r < ROWS; r++) {
                cellPosition = r + " " + c;
                board.getChildren().add(boardMap.get(cellPosition));
            }
        }
            
        generation++;
        text.setText(Integer.toString(generation));
        table.getChildren().add(text);
        
    }
}
