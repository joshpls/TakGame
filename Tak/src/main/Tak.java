package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import player.PlayerType;
import tokens.TokenType;

public class Tak extends Application {

    protected static boolean playable = true;
    protected static int playersTurn = 0, capstoneLimit = 0;
    protected static PlayerType currentPlayer;
    protected static String currentToken;
    protected static boolean isCapUsed = false;
    protected static int boardWidth = 600;
    protected static int boardHeight = 600;
    protected static int boardSize;

    protected BorderPane root = new BorderPane();
    protected static Pane boardPane = new Pane();
    protected static TextFlow bottomPane = new TextFlow();
    protected static ListView<String> leftArea = new ListView<String>();
    protected static ListView<String> rightArea = new ListView<String>();
    protected static MenuBar menu;
    private Board board;

    private Parent createContent() {

        setupGame();
        board = new Board();
        board.setupBoard();
        boardPane.setPrefSize(boardWidth, boardHeight);
        
        root.setCenter(boardPane);
        
        setupBottomPane();
        setupLeftPane();
        
        rightArea.setPrefSize(100, 600);
        rightArea.getItems().add("White's Turn");
        root.setRight(rightArea);
        
        setupMenu();
//        menu.setBackground(new Background(new BackgroundFill(Paint.valueOf("Grey"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        return root;
    }
    
    private void startNewGame() {
    	setupGame();
    	board.resetBoard();
//    	board.setupBoard();
    	System.out.println("Board Size: " + boardSize);
    }
    
    private void setupMenu(){
    	menu = new MenuBar();
    	menu.prefWidthProperty().bind(root.widthProperty());
    	Menu fileMenu = new Menu("File");
        MenuItem newGameMenuItem = new MenuItem("New Game");
        MenuItem undoMenuItem = new MenuItem("Undo");
        MenuItem settingsMenuItem = new MenuItem("Settings");
        MenuItem exitMenuItem = new MenuItem("Exit");
        
    	Menu boardMenu = new Menu("Board");
    	MenuItem board3MenuItem = new MenuItem("Board 3x3");
        MenuItem board4MenuItem = new MenuItem("Board 4x4");
        MenuItem board5MenuItem = new MenuItem("Board 5x5");
        MenuItem board6MenuItem = new MenuItem("Board 6x6");
        MenuItem board7MenuItem = new MenuItem("Board 7x7");
        MenuItem board8MenuItem = new MenuItem("Board 8x8");
        boardMenu.getItems().addAll(board3MenuItem, board4MenuItem, board5MenuItem, board6MenuItem, board7MenuItem, board8MenuItem);
    	
    	fileMenu.getItems().addAll(newGameMenuItem, undoMenuItem, settingsMenuItem, exitMenuItem);
        
    	Menu multiMenu = new Menu("Multiplayer");
        MenuItem hostGameMenuItem = new MenuItem("Host Game");
        MenuItem joinGameMenuItem = new MenuItem("Join Game");
        multiMenu.getItems().addAll(hostGameMenuItem, joinGameMenuItem);
        
    	Menu playerMenu = new Menu("Player");
        MenuItem colorMenuItem = new MenuItem("Colors");
        MenuItem controlsMenuItem = new MenuItem("Controls");
        playerMenu.getItems().addAll(colorMenuItem, controlsMenuItem);
        
        newGameMenuItem.setOnAction(actionEvent -> startNewGame());
        board3MenuItem.setOnAction(actionEvent -> changeBoardSize(3));
        board4MenuItem.setOnAction(actionEvent -> changeBoardSize(4));
        board5MenuItem.setOnAction(actionEvent -> changeBoardSize(5));
        board6MenuItem.setOnAction(actionEvent -> changeBoardSize(6));
        board7MenuItem.setOnAction(actionEvent -> changeBoardSize(7));
        board8MenuItem.setOnAction(actionEvent -> changeBoardSize(8));
        
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        menu.getMenus().addAll(fileMenu, boardMenu, multiMenu, playerMenu);
        
        root.setTop(menu);
    }
    
    private void setupBottomPane(){
        bottomPane.setPrefSize(800, 150);
        root.setBottom(bottomPane);
        Text header = new Text("Move List:\n");
        header.setUnderline(true);
        bottomPane.getChildren().add(header); 
    }
    
    private void setupLeftPane(){
        leftArea.setPrefSize(100, 600);
        leftArea.getItems().add("Stack List:");
        leftArea.setEditable(false);       
        root.setLeft(leftArea);
        
        leftArea.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("ListView selection changed from oldValue = " 
                        + oldValue + " to newValue = " + newValue);
                currentPlayer.setSelection(leftArea.getItems().indexOf(newValue));
                System.out.println("Selected:" + currentPlayer.getSelection());
            }
        });
    }
    
    private void setupGame(){
    	System.out.println("New Game Started.");
        playable = true;
        playersTurn = 0;
        currentPlayer = PlayerType.WHITE;
        currentToken = TokenType.FLATSTONE.toString();
        if(boardSize == 8)
        	setCapstoneLimit(2);
        else
        	setCapstoneLimit(1);
        
        //Clear displays
    	leftArea.getItems().clear();
    	bottomPane.getChildren().clear();
    	rightArea.getItems().clear();
    }
    
    public static void changeBoardSize(int size)
    {
    	boardSize = size;
    	System.out.println("Board Size Set: " + size);
    }
    
    public static void setCapstoneLimit(int limit)
    {
    	capstoneLimit = limit;
    }
    
    public int getCapstoneLimit()
    {
    	return capstoneLimit;
    }
    
    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("Tak: The Game");
    	
        Scene scene = new Scene(createContent());
        
    	root.setOnKeyPressed(event -> {
        	KeyCode keyCode = event.getCode();
        	if (keyCode.equals(KeyCode.R))
        	{
        		cycleToken();
        	}
        	if (keyCode.equals(KeyCode.ENTER))
        	{
        		System.out.println("Enter Key Pressed!");
        		startNewGame();
        	}
        	if (keyCode.equals(KeyCode.ESCAPE))
        	{
        		primaryStage.close();
        	}
        });
    	
        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
        primaryStage.show();
        System.out.println("Start");
    }
    
	public static void cycleToken() {
		if(currentToken == TokenType.FLATSTONE.toString())
			currentToken = TokenType.WALL.toString();
		else if(currentToken == TokenType.WALL.toString())
		{
			if(currentPlayer == PlayerType.WHITE){
				if(Board.whiteCapCount < Tak.capstoneLimit)
					currentToken = TokenType.CAPSTONE.toString();
				else
					currentToken = TokenType.FLATSTONE.toString();
			}
			else
			{
				if(Board.blackCapCount < Tak.capstoneLimit)
					currentToken = TokenType.CAPSTONE.toString();
				else
					currentToken = TokenType.FLATSTONE.toString();
			}
		}
		else
			currentToken = TokenType.FLATSTONE.toString();
		System.out.println("Changed token to: " + currentToken);
	}
	
	public static void switchCurrentPlayer() {
		if (currentPlayer == PlayerType.WHITE){
			currentPlayer = PlayerType.BLACK;
			rightArea.getItems().set(0, "Black's Turn");	//temporary
		}
        else{
        	currentPlayer = PlayerType.WHITE;
    		rightArea.getItems().set(0,"White's Turn");	//temporary
        }
		
	}
    
    public static void main(String[] args) {
      boardSize = 5;
      launch(args);
    }
}