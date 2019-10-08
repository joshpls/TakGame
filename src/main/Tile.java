package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Stack;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import player.PlayerType;
import tokens.Capstone;
import tokens.Flatstone;
import tokens.Wall;
import tokens.Token;
import tokens.TokenType;


public class Tile extends StackPane {
    private Text text = new Text();
//	private Token token = null;
	private Stack<Token> tokenStack;
	private Coordinate coordinate;
	private Image BFImage, WFImage, BwImage, WwImage, BcImage, WcImage;
	private ImageView selectedImage;
	private double tileSize;
	private Integer selectStack = 0;

	public Tile(Coordinate coordinate){
		this.coordinate = coordinate;
		tokenStack = new Stack<Token>();
		
		tileSize = (Tak.boardWidth/Tak.boardSize);
		
		Rectangle border = new Rectangle(tileSize, tileSize);
		border.setFill(null);
		border.setStroke(Color.BLACK);	
		text.setFont(Font.font(((1/(double)Tak.boardSize)*250)));
		
		setupImages();

    	Group imageGroup = new Group(selectedImage);
    	
		getChildren().addAll(border, imageGroup, text);
		
		setAlignment(Pos.CENTER);
		
		setOnMouseClicked(event -> 
		{
		    if (!Tak.playable)
		    	return;
		     
		    if (event.getButton() == MouseButton.PRIMARY) {
		    	if (Tak.playersTurn == 1)
		    		return;
	
		    	if(!isOccupied())
		    	{
		    		if(Tak.currentToken == TokenType.FLATSTONE.toString())
		    			setToken(new Flatstone(Tak.currentPlayer));
		    		else if(Tak.currentToken == TokenType.WALL.toString())
		    			setToken(new Wall(Tak.currentPlayer));
		    		else{
		    			setToken(new Capstone(Tak.currentPlayer));
		    			Tak.isCapUsed = true;
		    			Tak.cycleToken();
		    		}
		    		selectStack = 1;
		    		paintTile();
		    		Board.checkState();
		    		Tak.switchCurrentPlayer();	
		    	}
		    	else{
		    		if(selectStack < tokenStack.size())
		    			selectStack++;
		    		else
		    			selectStack = 1;
	    			System.out.println("Selected " + selectStack + " Tokens.");
		    		
		    		System.out.println("Stack Size: " + tokenStack.size());
		    		if(tokenStack.size() == 1)
		    		{
		    			System.out.println("Tile contains: " + getToken());
		    		}
		    		else
		    			System.out.println("Stack contents: " + getTokenStackString(tokenStack.size()));
		    	}
		    }
		});
		
		setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
            	if(tokenStack.isEmpty())
            		return;
                Dragboard db = startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(getTokenStackString(selectStack));
                System.out.println(getTokenStackString(selectStack));
                db.setContent(content);
                
                event.consume();
            }
        });
         
        setOnDragOver(new EventHandler <DragEvent>() {
             public void handle(DragEvent event) {
                 if (event.getGestureSource() != event.getGestureTarget() &&
                         event.getDragboard().hasString()) {
                     event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                 }
                 
                 event.consume();
             }
         });
        
        setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getGestureSource() != event.getGestureTarget() &&
                        event.getDragboard().hasString()) {
                    border.setFill(Color.GREEN);
                }
                
                event.consume();
            }
        });
        
        setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                border.setFill(Color.WHITE);
                
                event.consume();
            }
        });
        
        setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if(event.getGestureSource() != event.getGestureTarget())
	                if (db.hasString()) {
	                	setTokenStack(db.getString());
	                	
	                	Board.checkState();
	                	paintTile();
	                	Tak.switchCurrentPlayer();
	                	selectStack = 1;
	                    success = true;
	                    Board.checkState();
	                }
                event.setDropCompleted(success);
                
                event.consume();
            }
        });
        
        setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                if (event.getTransferMode() == TransferMode.MOVE) {
                    releaseTokenStack(selectStack);
                    paintTile();
                    if(tokenStack.isEmpty())
                    	selectStack = 0;
                    else
                    	selectStack = 1;
                }
                
                event.consume();
            }
        });
		
     }

    public Tile(Token token) {
        tokenStack.add(token);
    }
    
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Token getToken() {
    	if(tokenStack.size() > 0)
    		return tokenStack.peek();
    	else
    		return null;
    }

    public boolean isOccupied() {
        if (tokenStack.isEmpty()) {
            return false;
        }
        return true;
    }

    public String getTokenString() {
        if (tokenStack.isEmpty()) {
            return " ";
        }
        return tokenStack.peek().toString();
    }

    public void releaseToken() {
        tokenStack.pop();
    }
    
    public void releaseTokenStack(int num){
    	if(tokenStack.isEmpty())
    		return;
    	if(num > tokenStack.size())
    		num = tokenStack.size();
    	
    	for(int i = 0; i < num; i++){
    		tokenStack.pop();
    	}
    }
    
    // Returns the stack list from top to bottom of stack for amount requested. Last in first out.
    public String getTokenStackString(int num){
    	String stack = "";
    	if(tokenStack.isEmpty())
    		return stack;
    	if(num > tokenStack.size())
    		num = tokenStack.size();
    	int currentToken = tokenStack.size()-1;
    	for(int i = 0; i < num; i++){
    		stack = tokenStack.elementAt(currentToken).toString() + stack;
    		currentToken--;
    	}
		return stack;
    }
    
    public int getTokenStackSize(){
    	return tokenStack.size();
    }
    
    public void setTokenStack(String stack)
    {
    	String token = "";
    	if(stack.isEmpty())
    		return;
    	else
    	{
    		 for(char ch : stack.toCharArray()){
    			 token = token + ch;
    			 if(token.length() == 2)
    			 {
    				 if(token.equals("Wf"))
	                		setToken(new Flatstone(PlayerType.WHITE));
	                	else if(token.equals("Bf"))
	                		setToken(new Flatstone(PlayerType.BLACK));
	                	else if(token.equals("Ww"))
	                		setToken(new Wall(PlayerType.WHITE));
	                	else if(token.equals("Bw"))
	                		setToken(new Wall(PlayerType.BLACK));
	                	else if(token.equals("Wc"))
	                		setToken(new Capstone(PlayerType.WHITE));
	                	else if(token.equals("Bc"))
	                		setToken(new Capstone(PlayerType.BLACK));
	                	else
	                		System.out.println("Token:" + token + "\n GetToken: " + getToken());
    				 token = "";
    			 }
    		 }

    	}
    }

    public void setToken(Token token) {
        tokenStack.add(token);
    }
    
    public double getCenterX() {
        return getTranslateX() + 100;
    }

    public double getCenterY() {
        return getTranslateY() + 100;
    }
    
    public void paintTile(){
    	if(!tokenStack.isEmpty())
    	{
			if(tokenStack.peek().getType() == TokenType.FLATSTONE)
	    		if(tokenStack.peek().getPlayer() == PlayerType.WHITE)
	    			selectedImage.setImage(WFImage);
				else
					selectedImage.setImage(BFImage);
	    		
			else if(tokenStack.peek().getType() == TokenType.WALL)
				if(tokenStack.peek().getPlayer() == PlayerType.WHITE)
					selectedImage.setImage(WwImage);
				else
					selectedImage.setImage(BwImage);
    		
			else
				if(tokenStack.peek().getPlayer() == PlayerType.WHITE)
					selectedImage.setImage(WFImage);
				else
					selectedImage.setImage(BFImage);
    	}
    	else
		{
			text.setText("");
			selectedImage.setImage(null);
		}
    }
    
    public void setupImages()
    {
    	String imageSize;
    	if(Tak.boardSize >= 3 && Tak.boardSize < 5)
    		imageSize = "120"; //160
    	else if(Tak.boardSize >=5 && Tak.boardSize < 7)
    		imageSize = "120";
    	else
    		imageSize = "32";
    		
    	selectedImage = new ImageView();
    	BFImage = new Image(Tak.class.getResourceAsStream("/Bf_"+imageSize+".png"));
		WFImage = new Image(Tak.class.getResourceAsStream("/Wf_"+imageSize+".png"));
		BwImage = new Image(Tak.class.getResourceAsStream("/Bw_"+imageSize+".png"));
		WwImage = new Image(Tak.class.getResourceAsStream("/Ww_"+imageSize+".png"));
		BcImage = new Image(Tak.class.getResourceAsStream("/Wf_"+imageSize+".png"));
		WcImage = new Image(Tak.class.getResourceAsStream("/Wf_"+imageSize+".png"));
    	selectedImage.setX(getTranslateX());
    	selectedImage.setY(getTranslateY());
    }
}