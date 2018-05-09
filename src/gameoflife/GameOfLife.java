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
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Justin Beringer
 *
 * JavaFX Implementation of Conway's Game of Life Inspired by:
 * https://www.youtube.com/watch?v=FWSR_7kZuYg&t=855s
 *
 */
public class GameOfLife extends Application {

    final int WIDTH = 1000; // grid width
    final int HEIGHT = 700; // grid height
    final int SCALE = 20; // cell scale factor
    final int COLUMNS = WIDTH / SCALE;
    final int ROWS = HEIGHT / SCALE;

    // maps cells to position on grid
    Map<Position, Cell> gridMap = new HashMap<>();

    // allows gridMap to be iterated non-destructively
    boolean[][] lifeGrid = new boolean[COLUMNS][ROWS];

    // tracks the number of generations aka 'ticks'
    int generation = 0;

    VBox vBox;
    Pane grid;
    HBox controls;
    Button startButton;
    Button pauseButton;
    Button clearButton;
    Button nukeButton;
    Text generationCounter;

    @Override
    public void start(Stage primaryStage) {

        // Initializes timeline
        @SuppressWarnings("Convert2Lambda")
        final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(32), new EventHandler() {
            @Override
            public void handle(Event event) {
                updateGrid();
            }
        }));

        grid = new Pane();
        grid.setMaxWidth(WIDTH);
        //grid.setStyle("-fx-background-color: gray;");

        vBox = new VBox();
        vBox.getChildren().add(grid);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.setStyle("-fx-background-color: rgb(15, 17, 16)");
        vBox.setPadding(new Insets(10, 10, 10, 10));

        controls = new HBox();
        controls.setMaxWidth(WIDTH);
        controls.setSpacing(100);
        controls.setAlignment(Pos.CENTER);

        int buttonFontSize = 12;
        int buttonWidth = 75;
        startButton = new Button("start");
        startButton.setFont(new Font(buttonFontSize));
        startButton.setPrefWidth(buttonWidth);
        //startButton.setStyle("-fx-focus-color: transparent");

        pauseButton = new Button("pause");
        pauseButton.setFont(new Font(buttonFontSize));
        pauseButton.setPrefWidth(buttonWidth);
        //pauseButton.setStyle("-fx-focus-color: transparent");

        clearButton = new Button("clear");
        clearButton.setFont(new Font(buttonFontSize));
        clearButton.setPrefWidth(buttonWidth);
        //pauseButton.setStyle("-fx-focus-color: transparent");

        nukeButton = new Button("☢ nuke ☢");
        nukeButton.setFont(new Font(buttonFontSize));
        nukeButton.setPrefWidth(buttonWidth);
        //pauseButton.setStyle("-fx-focus-color: transparent");

        initializeButtonListeners(timeline);

        controls.getChildren().add(startButton);
        controls.getChildren().add(pauseButton);
        controls.getChildren().add(clearButton);
        controls.getChildren().add(nukeButton);

        generationCounter = new Text("generation: " + Integer.toString(generation));
        generationCounter.setFont(new Font(15));
        generationCounter.setFill(Color.WHITE);

        /**
         * adds generation counter to controls
         */
        //controls.getChildren().add(generationCounter);
        vBox.getChildren().add(controls);
        Scene scene = new Scene(vBox);
        scene.setFill(Color.BLACK);
        seed(scene, primaryStage);
    }

    // initializes cells
    private void seed(Scene scene, Stage primaryStage) {

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {

                int xPos = column * SCALE;
                int yPos = row * SCALE;

                Cell cell = new Cell(xPos, yPos, SCALE, SCALE);

                cell.setOnMouseClicked((MouseEvent t) -> {
                    if (t.getButton() == MouseButton.SECONDARY) {
                        cell.setIsAlive(false);
                    } else {
                        cell.setIsAlive(true);
                    }
                });

                cell.setOnMouseDragEntered((MouseEvent t) -> {
                    if (t.getButton() == MouseButton.SECONDARY) {
                        cell.setIsAlive(false);
                    } else {
                        cell.setIsAlive(true);
                    }
                });

                Position cellPosition = new Position(column, row);
                gridMap.put(cellPosition, cell);
                grid.getChildren().add(cell);
            }
        }

        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        //primaryStage.sizeToScene();
        primaryStage.show();
    }

    // track living neighbours
    private void updateGrid() {

        int liveNeighbours;
        Position gridPosition = new Position(0, 0);

        gridPosition.clear();

        // iterates over each cell
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {

                gridPosition.setPosition(column, row);

                // gets initial life state
                lifeGrid[column][row] = gridMap.get(gridPosition).getIsAlive();

                liveNeighbours = checkNeighboursAround(column, row);

                // enforces rules on the cells
                if (gridMap.get(gridPosition).getIsAlive()) {
                    if (liveNeighbours < 2 || liveNeighbours > 3) {
                        // cell dies due to underpopulation OR overpopulation
                        lifeGrid[column][row] = false;
                    }
                } else if (liveNeighbours == 3) {
                    // neighbours reproduce
                    lifeGrid[column][row] = true;
                }
            }
        }

        // updates hashmap with cell life states
        gridPosition.clear();
        grid.getChildren().clear();
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                gridPosition.setPosition(column, row);
                gridMap.get(gridPosition).setIsAlive(lifeGrid[column][row]);
                grid.getChildren().add(gridMap.get(gridPosition));
            }
        }
        generation++;
        generationCounter.setText("generation: " + Integer.toString(generation));
    }

    Position checkPosition = new Position(0, 0);
    Position aroundPosition = new Position(0, 0);

    private int checkNeighboursAround(int column, int row) {

        int neighbourColumn;
        int neighbourRow;
        int liveNeighbours = 0;

        aroundPosition.clear();
        aroundPosition.setPosition(column, row);

        checkPosition.clear();
        for (int columnBuffer = -1; columnBuffer < 2; columnBuffer++) {
            for (int rowBuffer = -1; rowBuffer < 2; rowBuffer++) {
                neighbourColumn = (column + columnBuffer + COLUMNS) % COLUMNS;
                neighbourRow = (row + rowBuffer + ROWS) % ROWS;
                checkPosition.setPosition(neighbourColumn, neighbourRow);
                if (gridMap.get(checkPosition).getIsAlive() && !checkPosition.equals(aroundPosition)) {
                    liveNeighbours++;
                }
            }
        }
        return liveNeighbours;
    }

    private void clearCells() {

        generation = 0;
        generationCounter.setText("generation: " + Integer.toString(generation));
        Position cellPosition = new Position(0, 0);
        grid.getChildren().clear();

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                cellPosition.setPosition(column, row);
                gridMap.get(cellPosition).setIsAlive(false);
                grid.getChildren().add(gridMap.get(cellPosition));
            }
        }
    }

    // randomly spawns living cells
    private void nuke() {
        boolean isAlive;
        Position cellPosition = new Position(0, 0);

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                cellPosition.setPosition(column, row);
                isAlive = ThreadLocalRandom.current().nextInt(0, 2) == 1;
                gridMap.get(cellPosition).setIsAlive(isAlive);
            }
        }
    }

    private void initializeButtonListeners(Timeline timeline) {
        grid.setOnDragDetected((MouseEvent t) -> {
            grid.startFullDrag();
        });

        startButton.setOnAction((ActionEvent t) -> {
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });

        pauseButton.setOnAction((ActionEvent t) -> {
            timeline.pause();
        });

        clearButton.setOnAction((ActionEvent t) -> {
            timeline.pause();
            clearCells();
        });

        nukeButton.setOnAction((ActionEvent t) -> {
            nuke();
        });
    }
}
