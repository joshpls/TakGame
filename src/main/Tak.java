package main;

import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import player.PlayerType;
import tokens.TokenType;

public class Tak extends Application {

    protected static boolean playable = true;
    protected static int playersTurn = 0;
    protected static PlayerType currentPlayer;
    protected static String currentToken;
    protected static boolean isCapUsed = false;
    protected static int boardWidth = 600;
    protected static int boardHeight = 600;
    protected static int boardSize;

    Line line = new Line();
    
    private Pane root = new Pane();
    protected static Pane boardPane = new Pane();
    private Board board;

    private Parent createContent() {
        root.setPrefSize(boardWidth, boardHeight);
        setupGame();
        board = new Board();
        root.getChildren().add(boardPane);

        return root;
    }
    
    private void startNewGame() {
    	setupGame();
    	board.resetBoard();
    }
    
    private void setupGame(){
    	System.out.println("New Game Started.");
        playable = true;
        playersTurn = 0;
        currentPlayer = PlayerType.WHITE;
        currentToken = TokenType.FLATSTONE.toString();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	primaryStage.setTitle("Tak: The Game");
    	
        Scene scene = new Scene(createContent());
        
    	scene.setOnKeyPressed(event -> {
        	KeyCode keyCode = event.getCode();
        	if (keyCode.equals(KeyCode.R))
        	{
        		cycleToken();
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
    
	public static void cycleToken(){
		if(currentToken == TokenType.FLATSTONE.toString())
			currentToken = TokenType.WALL.toString();
		else if(currentToken == TokenType.WALL.toString())
			if(!isCapUsed)
				currentToken = TokenType.CAPSTONE.toString();
			else
				currentToken = TokenType.FLATSTONE.toString();
		else
			currentToken = TokenType.FLATSTONE.toString();
		System.out.println("Changed token to: " + currentToken);
	}
	
	public static void switchCurrentPlayer() {
		if (currentPlayer == PlayerType.WHITE)
			currentPlayer = PlayerType.BLACK;
        else
        	currentPlayer = PlayerType.WHITE;
	}
    
	public static void main(String[] args) {
	      Scanner scan = new Scanner(System.in);
	      System.out.println(("Enter Board Size: "));
	      boardSize = scan.nextInt();
	      
	      while(boardSize < 3 || boardSize > 8){
	    	  System.out.println("Board Size must be between 3 & 8. Re-enter:");
	    	  boardSize = scan.nextInt();
	      }
	      
	      scan.close();
	    	
	        launch(args);
	    }
}