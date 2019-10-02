package tak;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Tak extends Application {

    private boolean playable = true;
    private int playersTurn = 0;
    private Tile[][] board = new Tile[5][5];
    private List<Combo> combos = new ArrayList<>();
    Line line = new Line();
    
    private Pane root = new Pane();
    private Pane boardPane = new Pane();
    Integer tileIDCount = 0;

    private Parent createContent() {
        root.setPrefSize(600, 600);
        
        setupGame();

        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 120);
                tile.setTranslateY(i * 120);
//                tile.setId(tileIDCount.toString());
//                System.out.println(tile.getId());

                boardPane.getChildren().add(tile);

                board[j][i] = tile;
            }
        }
        root.getChildren().add(boardPane);

        //Check Wins
        // horizontal
        for (int y = 0; y < 5; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y], board[3][y], board[4][y]));
//            System.out.println("Create Content 2");
        }

        // vertical
        for (int x = 0; x < 5; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2], board[x][3], board[x][4]));
//            System.out.println("Create Content 3");
        }

        // diagonals
        //combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        //combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return root;
    }
    
    private void startNewGame() {
    	setupGame();
    	
        //Clear Board
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[j][i].paintTile("");
                board[j][i].setValue(0, null);
                board[j][i].setValue(1, null);
            }
        }
    }
    
    private void setupGame(){
    	System.out.println("New Game Started.");
        playable = true;
        playersTurn = 0;
        
        Token.setToken(0, "flatstone");
        Token.setToken(1, "flatstone");
        Token.setCapstoneStatus(0, false);
        Token.setCapstoneStatus(1, false);
        
        if(line.isVisible())
        	root.getChildren().remove(line);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	primaryStage.setTitle("Tak: The Game");
    	
        Scene scene = new Scene(createContent());
        

        
    	scene.setOnKeyPressed(event -> {
//        	System.out.println("A Key is Pressed.");
        	KeyCode keyCode = event.getCode();
        	if (keyCode.equals(KeyCode.R))
        	{
        		Token.cycleToken(Token.getToken(playersTurn), playersTurn, Token.getCapstoneStatus(playersTurn));
        		System.out.println("Current Token: " + Token.getToken(playersTurn));
        	}
        	if (keyCode.equals(KeyCode.ENTER))
        	{
        		startNewGame();
        	}
        	if (keyCode.equals(KeyCode.ESCAPE))
        	{
        		primaryStage.close();
        	}
        });
    	
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("Start");
    }

    private void checkState() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
            	
            	System.out.println("Game Over!" + " Player " + (playersTurn+1) + " Won!");
                playable = false;
                playWinAnimation(combo);
                break;
            }
        }
    }

    private void playWinAnimation(Combo combo) {
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[0].getCenterX());
        line.setEndY(combo.tiles[0].getCenterY());

        root.getChildren().add(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.5),
                new KeyValue(line.endXProperty(), combo.tiles[4].getCenterX()),
                new KeyValue(line.endYProperty(), combo.tiles[4].getCenterY())));
        timeline.play();
    }

    private class Combo {
        private Tile[] tiles;
        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete() {
            if (tiles[0].getValue() == null)
                return false;
            
            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue())
                    && tiles[0].getValue().equals(tiles[3].getValue())
                    && tiles[0].getValue().equals(tiles[4].getValue());
        }
    }
    
    private class Tile extends StackPane {
        private Text text = new Text();
    	private String[] tileValue = new String[2];
    	private Stack<String[]> tileStack = new Stack<String[]>();

        public Tile() {
            Rectangle border = new Rectangle(120, 120);
            border.setFill(null);
            border.setStroke(Color.BLACK);
            
            setId(tileIDCount.toString());
            tileIDCount++;

            text.setFont(Font.font(72));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {
            	
                if (!playable)
                    return;
                
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (playersTurn == 1)
                        return;
                    if(text.getText().isEmpty()){
                    	text.setFill(Color.RED);
                    	drawP1Token();
                    	checkState();
                    	playersTurn = 1;
                    }
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    if (playersTurn == 0)
                        return;
                    if(text.getText().isEmpty()){
                    	text.setFill(Color.BLUE);
	                    drawP2Token();
	                    checkState();
	                    playersTurn = 0;
                    }
                }
            });
            
            setOnDragDetected(new EventHandler <MouseEvent>() {
                public void handle(MouseEvent event) {
                    /* drag was detected, start drag-and-drop gesture*/
                    System.out.println("onDragDetected");
                    
                    /* allow any transfer mode */
                    Dragboard db = startDragAndDrop(TransferMode.ANY);
                    
                    System.out.println("ID: " + getId());
                    
                    /* put a string on dragboard */
                    ClipboardContent content = new ClipboardContent();
//                    content.putString(text.getText());
                    content.putString(getValue());
                    db.setContent(content);
                    
                    ClipboardContent playerValue = new ClipboardContent();
//                  content.putString(text.getText());
                    if(!tileValue[0].isEmpty())
                    	playerValue.putString(tileValue[0]);
                    else
                    	playerValue.putString(tileValue[1]);
                  db.setContent(playerValue);
                    
                    System.out.println("ID: " + getId());
                    
                    event.consume();
                }
            });
             
            setOnDragOver(new EventHandler <DragEvent>() {
                 public void handle(DragEvent event) {
                     /* data is dragged over the target */

                     /* accept it only if it is  not dragged from the same node 
                      * and if it has a string data */
                     if (event.getGestureSource() != event.getGestureTarget() &&
                             event.getDragboard().hasString()) {
                         /* allow for both copying and moving, whatever user chooses */
                         event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                     }
                     
                     event.consume();
                 }
             });
            
            setOnDragEntered(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* the drag-and-drop gesture entered the target */
                    System.out.println("onDragEntered");
                    /* show to the user that it is an actual gesture target */
                    if (event.getGestureSource() != event.getGestureTarget() &&
                            event.getDragboard().hasString()) {
                        border.setFill(Color.GREEN);
//                        System.out.println("ID: " + getId());
                    }
                    
                    event.consume();
                }
            });
            
            setOnDragExited(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* mouse moved away, remove the graphical cues */
                    border.setFill(Color.WHITE);
                    
                    event.consume();
                }
            });
            
            setOnDragDropped(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* data dropped */
                    System.out.println("onDragDropped");
                    /* if there is a string data on dragboard, read it and use it */
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {
//                        text.setText(db.getString());
                    	setValue(playersTurn, db.getString());
                        success = true;
                    }
                    /* let the source know whether the string was successfully 
                     * transferred and used */
                    event.setDropCompleted(success);
                    
                    event.consume();
                }
            });
            
            setOnDragDone(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* the drag-and-drop gesture ended */
                    System.out.println("onDragDone");
                    /* if the data was successfully moved, clear it */
                    if (event.getTransferMode() == TransferMode.MOVE) {
                        text.setText("");
                        
                    }
                    
                    event.consume();
                }
            });
            
        }
        

        public double getCenterX() {
            return getTranslateX() + 100;
        }

        public double getCenterY() {
            return getTranslateY() + 100;
        }

        public void setValue(int playersTurn, String text)
        {
        	paintTile(text);
        	tileValue[playersTurn] = text;
        	tileStack.push(tileValue);
        }
        
        public String getValue() {
        	if (!tileStack.isEmpty()){
        		String[] topOfStack = tileStack.peek();
//            	return tileValue[playersTurn];
//	        	System.out.println("Top of Stack: " + topOfStack[playersTurn]);
	        	return topOfStack[playersTurn];
        	}
        	else return tileValue[playersTurn];
        }
        
        private void paintTile(String value){
        	text.setText(value);
        }
        
        private void drawP1Token() {
        	if(Token.getToken(playersTurn) == "flatstone"){        		
        		setValue(0, "[]");
        	}
        	else if(Token.getToken(playersTurn) == "wall"){
        		setValue(0, "|");
        	}
        	else
        	{
        		setValue(0, "O");
        		Token.setCapstoneStatus(0, true);
        		Token.cycleToken(Token.getToken(0), 0, Token.getCapstoneStatus(0));
        	}
        }
        
        private void drawP2Token() {
        	if(Token.getToken(1) == "flatstone"){
        		setValue(1, "[]");
        	}
        	else if(Token.getToken(1) == "wall"){
        		setValue(1, "|");
        	}
        	else
        	{
        		setValue(1, "O");
        		Token.setCapstoneStatus(1, true);
        		Token.cycleToken(Token.getToken(1), 1, Token.getCapstoneStatus(1));
        	}
        }
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}