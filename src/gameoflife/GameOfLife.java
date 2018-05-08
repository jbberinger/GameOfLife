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
 * @author Justin Beringer
 */
public class GameOfLife extends Application {

    final int WIDTH = 500; // window width
    final int HEIGHT = 500; // window height
    final int SCALE = 5; // grid size
    final int COLUMNS = WIDTH / SCALE;
    final int ROWS = HEIGHT / SCALE;

    // maps cells to position on board
    Map<String, Cell> boardMap = new HashMap<>();
    // allows boardMap to be iterated non-destructively
    boolean[][] lifeGrid = new boolean[COLUMNS][ROWS];

    // tracks the number of generations
    int generation = 0;

    StackPane table;
    Pane board;
    Text text;

    @Override
    public void start(Stage primaryStage) {

        // Initializes timeline
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler() {
            @Override
            public void handle(Event event) {
                updateBoard();
            }
        }));

        // Timeline runs indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);

        table = new StackPane();
        board = new Pane();

        text = new Text(Integer.toString(generation));
        text.setFont(new Font(100));
        text.setFill(Color.rgb(255, 255, 0, 0.75));

        table.getChildren().add(board);
        Scene scene = new Scene(table);
        scene.setFill(Color.BLACK);

        seed();

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

                boolean isAlive;
                // randomly spawns living cells
                isAlive = ThreadLocalRandom.current().nextInt(0, 1 + 1) == 1;

                cell.setIsAlive(isAlive);

                boardMap.put(position, cell);

                board.getChildren().add(cell);

            }
        }
        generation++;

         //displayes generation counter
        table.getChildren().remove(text);
        text.setText(Integer.toString(generation));
        table.getChildren().add(text);
    }

    int liveNeighbours = 0; // tracks living neighbours
    String cellPosition;
    String checkPosition;

    private void updateBoard() {

        // iterates over each cell
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {

                liveNeighbours = 0;
                cellPosition = c + " " + r;
                Cell bufferCell = boardMap.get(cellPosition);
                // gets initial life state
                lifeGrid[c][r] = bufferCell.getIsAlive();

                // counts living neighbours
                // index ternary adjusts for edges
                int neighbourColumn;
                int neighbourRow;
                for (int rowBuffer = -1; rowBuffer < 2; rowBuffer++) {
                    for (int columnBuffer = -1; columnBuffer < 2; columnBuffer++) {

                        neighbourColumn = (c + columnBuffer + COLUMNS) % COLUMNS;
                        neighbourRow = (r + rowBuffer + ROWS) % ROWS;

                        checkPosition = neighbourColumn + " " + neighbourRow;

                        if (boardMap.get(checkPosition).getIsAlive() && !checkPosition.equals(cellPosition)) {
                            liveNeighbours++;
                        }
                    }
                }

                // enforces rules on the cells
                if (bufferCell.getIsAlive()) {
                    if (liveNeighbours < 2 || liveNeighbours > 3) {
                        // cell dies due to underpopulation OR overpopulation
                        lifeGrid[c][r] = false;
                    }
                } else if (liveNeighbours == 3) {
                    // neighbours reproduce
                    lifeGrid[c][r] = true;
                }
            }
        }

        for (int i = 0; i < lifeGrid.length; i++) {
            for (int j = 0; j < lifeGrid.length; j++) {
                boardMap.get(j + " " + i).setIsAlive(lifeGrid[j][i]);
            }
        }

        // updates board with new cells
        board.getChildren().clear();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                cellPosition = c + " " + r;
                board.getChildren().add(boardMap.get(cellPosition));
            }
        }

        generation++;

         // displayes a yellow generation counter
        table.getChildren().remove(text);
        text.setText(Integer.toString(generation));
        table.getChildren().add(text);
    }
}
