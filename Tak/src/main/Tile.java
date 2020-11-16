package main;

import java.awt.Point;
import java.util.List;
import java.util.Stack;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import player.PlayerType;
import tokens.Capstone;
import tokens.Flatstone;
import tokens.Token;
import tokens.TokenType;
import tokens.Wall;

public class Tile extends StackPane {

    private Text label = new Text();
	private Stack<Token> tokenStack;
	private Point point;
	private Image BfImage, WfImage, BwImage, WwImage, BcImage, WcImage;
	private ImageView selectedImage;
	private double tileSize;
	private Color tileColor, originalColor;
	private Rectangle border;
	

	public Tile(Point point){
		this.point = point;
		tokenStack = new Stack<>();
		
		tileSize = (double)(Tak.boardWidth/Tak.boardSize);
		
		border = new Rectangle(tileSize, tileSize);
		if(Math.abs(point.getX() + point.getY()) % 2 == 0)
			setTileColor(Color.WHITE);
		else
			setTileColor(Color.DARKGREY);

		originalColor = getTileColor();
		border.setFill(getTileColor());
		border.setStroke(Color.BLACK);	
//		label.setFont(Font.font(((1/(double)Tak.boardSize)*250)));
		label.setFont(Font.font(12));	//How do I put this in bottom left Corner :[
		label.setOpacity(0.50);
		label.setText(getCharLabel((int)point.getX()+1) + ((int)point.getY()+1));
		
		setupImages();

    	Group imageGroup = new Group(selectedImage);
    	
		getChildren().addAll(border, imageGroup, label);
		
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
		    		// If token selected, move token then return.
	    			if(Board.selectedTile != null && Board.selectedTile != this){
	    				System.out.println("Attempt Move to Empty Tile");
	    				//Valid Move
	    				if(Board.getValidMoves(Board.selectedTile).contains(this)){
			    			MoveInfo m = new MoveInfo((int) Board.selectedTile.getPoint().getX(), (int)Board.selectedTile.getPoint().getY()
			    					, (int) this.getPoint().getX(), (int) this.getPoint().getY());
			    			System.out.println(m.toString());
			    			
			    			System.out.println("Slection Size : " + Tak.currentPlayer.getSelection());
			    			setTokenStack(Board.selectedTile.getTokenStackString(Tak.currentPlayer.getSelection()));
			    			Board.selectedTile.releaseTokenStack(Tak.currentPlayer.getSelection());
			    			Board.selectedTile.paintTile();
		                	
		                	paintTile();
		                	setDisplayMove(Board.selectedTile.getPoint(), this.getPoint());
			    			
		                	Board.selectedTile.setTileColor(Board.selectedTile.originalColor);
		                	Board.selectedTile = null;
		                	clearStackList();
		                	
		                	//Check for Win
		                	Board.checkState();
		                	
		                	//Switch Players
		                	Tak.switchCurrentPlayer(); 
	    				}
		    			
		    			return;
	    			}
	    			else{
	    				//Set new Token
			    		if(Tak.currentToken == TokenType.FLATSTONE.toString()){
			    			setToken(new Flatstone(Tak.currentPlayer));	//Set Token
			    			Board.setTileCount(Tak.currentPlayer);	//Set Tile Count for Flatstone/Wall for Player
			    		}
			    		else if(Tak.currentToken == TokenType.WALL.toString())
			    		{
			    			setToken(new Wall(Tak.currentPlayer));	//Set Token
			    			Board.setTileCount(Tak.currentPlayer);	//Set Tile Count for Flatstone/Wall for Player
			    		}
			    		else{
			    			setToken(new Capstone(Tak.currentPlayer));
			    			Board.setCapCount(Tak.currentPlayer);	//Set Capstone Count for Player
			    			Tak.cycleToken();
			    		}
//			    		selectStack = 1;
			    		paintTile();
			    		setDisplayMove(getPoint(), null);
			    		clearStackList();
			    		Board.checkState();
			    		Tak.switchCurrentPlayer();
	    			}
		    	}
		    	else{
		    		// This tile contains a token.
		    		
		    		if(Board.selectedTile != null)
		    		{
		    			if(Board.selectedTile == this){
		    				//Same tile, unselect tile and return.
		    				System.out.println("Tile Unselected!");
		    				Board.selectedTile.setTileColor(Board.selectedTile.originalColor);
			    			Board.selectedTile = null;
			    			clearStackList();
			    			return;
		    			}
		    			else{
		    				//Different Tile selected, move token stack to new tile.
		    				
		    				//Valid Move
		    				if(Board.getValidMoves(Board.selectedTile).contains(this)){
				    			MoveInfo m = new MoveInfo((int) Board.selectedTile.getPoint().getX(), (int)Board.selectedTile.getPoint().getY()
				    					, (int) this.getPoint().getX(), (int) this.getPoint().getY());
				    			System.out.println(m.toString());
			    				
			    				setTokenStack(Board.selectedTile.getTokenStackString(Tak.currentPlayer.getSelection()));
				    			Board.selectedTile.releaseTokenStack(Tak.currentPlayer.getSelection());
				    			Board.selectedTile.paintTile();
			                	
			                	paintTile();
			                	setDisplayMove(Board.selectedTile.getPoint(), this.getPoint());
				    			
			    				//Update selected tile
			    				Board.selectedTile.setTileColor(Board.selectedTile.originalColor);
			    				this.setTileColor(this.originalColor);
				    			Board.selectedTile = null;
				    			clearStackList();
				    			
				    			
			                	//Check for Win
			                    Board.checkState();
				    			
				    			//Switch Players
			                	Tak.switchCurrentPlayer();
			                	
				    			return;
		    				}

			    			
		    			}
			    		
		    		}
		    		else
		    		{
			    		// Check if it's current player's token
			    		if(getToken().getPlayer() != Tak.currentPlayer)
			    			return;
		    			
		    			// Select Tile, highlight token.
	    				System.out.println("Tile Selected!");
		    			Board.selectedTile = this;
		    			setTileColor(Color.GREEN);
		    			
		    			//print stack to bottom pane
			    		// Check if stack.
			    		if(getTokenStackSize() > 1)
			    		{
			    			//Display Stack to Left Area.
			    			for(int i = 0; i < getTokenStackSize(); i++)
			    			{
			    				Tak.leftArea.getItems().add(i+1 + ". " + tokenStack.get(i).toString());
			    			}
			    		}
		    		}
		    		
		    	}
		    }
		});
		
	}

    public Tile(Token token) {
        tokenStack.add(token);
    }
    
    public void setTileColor(Color newColor){ tileColor = newColor; border.setFill(tileColor); }
    public Color getTileColor(){return tileColor;}
    
    public void clearStackList(){
    	// Clear Stack List
    	Tak.leftArea.getItems().remove(1, Tak.leftArea.getItems().size());
    	Tak.currentPlayer.setSelection(1);
    }
    
    public void highlightValidMoves(Tile thisTile){
    	List<Tile> validMoves = Board.getValidMoves(thisTile);
    	//neighbors.removeIf(t -> t.getTokenStackSize() == 0);	Use this to remove empty tiles
    	
    	for(Tile tile : validMoves)
    	{
			System.out.println("Highlighted green:" + tile.getPoint().toString());
	//    			tile.setTileColor(Color.GREEN);
    	}
    }
    
    public Point getPoint() {
        return point;
    }
    
    public void setDisplayMove(Point from, Point to)
    {
    	
    	Text move;
    	Board.moveCount++;
    	if(to == null){
    		move = new Text(Board.moveCount + ". " + getTokenString() + " [" + getCharLabel((int) point.getX()+1) + ((int) point.getY()+1) + "] ");
    	}
    	else{
    		move = new Text(Board.moveCount + ". " + getTokenString() + " -> [" + getCharLabel((int) point.getX()+1) + ((int) point.getY()+1) + "] ");
    	}
    	if(getToken().getPlayer() == PlayerType.WHITE)
    		move.setFill(Color.DIMGREY);
    	else
    		move.setFill(Color.BLACK);

    	Tak.bottomPane.getChildren().addAll(move);
        Tak.bottomPane.autosize();
    }
    
    // Converts x number poisition to character label
    private String getCharLabel(int i)
    {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
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
    
    // tokens

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
	                	{
	                		flattenWall();
	                		setToken(new Capstone(PlayerType.WHITE));
	                	}
	                	else if(token.equals("Bc")){
	                		flattenWall();
	                		setToken(new Capstone(PlayerType.BLACK));
	                	}
	                	else
	                		System.out.println("Token:" + token + "\n GetToken: " + getToken());
    				 token = "";
    			 }
    		 }

    	}
    }
    
    public void flattenWall()
    {
    	if(tokenStack.isEmpty())
    		return;
    	if(tokenStack.peek().getType() != TokenType.WALL)
    		return;
    	 
    	PlayerType prev = tokenStack.peek().getPlayer();
    	tokenStack.pop();
    	
    	if(prev == PlayerType.WHITE)
    		tokenStack.add(new Flatstone(prev));
    	else
    		tokenStack.add(new Flatstone(prev));
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
	    			selectedImage.setImage(WfImage);
				else
					selectedImage.setImage(BfImage);
	    		
			else if(tokenStack.peek().getType() == TokenType.WALL)
				if(tokenStack.peek().getPlayer() == PlayerType.WHITE)
					selectedImage.setImage(WwImage);
				else
					selectedImage.setImage(BwImage);
    		
			else
				if(tokenStack.peek().getPlayer() == PlayerType.WHITE)
					selectedImage.setImage(WcImage);
				else
					selectedImage.setImage(BcImage);
    	}
    	else
		{
//			text.setText("");
			selectedImage.setImage(null);
		}
    }
    
    public void setupImages()
    {
    	String imageSize;
    	if(Tak.boardSize == 3)
    		imageSize = "160"; //160
    	else if(Tak.boardSize == 4)
    		imageSize = "128"; //160
    	else if(Tak.boardSize == 5)
    		imageSize = "96"; //160
    	else if(Tak.boardSize == 6)
    		imageSize = "64"; //160
    	else if(Tak.boardSize == 7)
    		imageSize = "64"; //160
    	else
    		imageSize = "32";
    		
    	selectedImage = new ImageView();
    	BfImage = new Image(Tak.class.getResourceAsStream("/Bf_"+imageSize+".png"));
		WfImage = new Image(Tak.class.getResourceAsStream("/Wf_"+imageSize+".png"));
		BwImage = new Image(Tak.class.getResourceAsStream("/Bw_"+imageSize+".png"));
		WwImage = new Image(Tak.class.getResourceAsStream("/Ww_"+imageSize+".png"));
		BcImage = new Image(Tak.class.getResourceAsStream("/Bc_"+imageSize+".png"));
		WcImage = new Image(Tak.class.getResourceAsStream("/Wc_"+imageSize+".png"));
    	selectedImage.setX(getTranslateX());
    	selectedImage.setY(getTranslateY());
    }
}
