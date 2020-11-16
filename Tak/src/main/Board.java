package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import player.PlayerType;

public class Board {

    private static Tile[][] board = new Tile[Tak.boardSize][Tak.boardSize];
    private Win roads = new Win();
    public static int whiteCapCount = 0, blackCapCount = 0, moveCount = 0;
    public static int whiteTileCount = 0, blackTileCount = 0;
    public static int maxWhiteTiles = 0, maxBlackTiles = 0;
    public static Tile selectedTile;

	public Board()
	{
//		setupBoard();
//		setMaxTiles();
	}
	
	public void setupBoard()
	{
        for (int i = 0; i < Tak.boardSize; i++) {
            for (int j = 0; j < Tak.boardSize; j++) {

                Tile tile = new Tile(new Point(i, j));
                tile.setTranslateX(i * (double)(Tak.boardWidth/Tak.boardSize));
                tile.setTranslateY(j * (double)(Tak.boardHeight/Tak.boardSize));
                
                Tak.boardPane.getChildren().add(tile);
                roads.addToRoad(tile);
                board[j][i] = tile;
            }
        }
        setMaxTiles();
	}
	
	public void resetBoard()
	{
        //Clear Board
        for (int i = 0; i < Tak.boardSize; i++) {
            for (int j = 0; j < Tak.boardSize; j++) {
            	board[j][i].releaseTokenStack(board[j][i].getTokenStackSize());
                board[j][i].paintTile();
            }
        }
        whiteCapCount = 0;
        blackCapCount = 0;
        Tak.isCapUsed = false;
        setMaxTiles();
	}
	
	public Tile[][] getBoard()
	{
		return board;
	}
	
    public static List<Tile> getNeighbors(Tile tile){
    	
    	List<Tile> neighbors = new ArrayList<>();
    	int[] points = new int[]{
    		-1,-1,
    		-1,0,
    		-1,1,
    		0,-1,
    		0,1,
    		1,-1,
    		1,0,
    		1,1
    	};
    	
    	for(int i = 0; i < points.length; i++){
    		int dx = points[i];
    		int dy = points[++i];
    		
        	int newX = tile.getPoint().x + dx;
        	int newY = tile.getPoint().y + dy;
        	
        	if(newX >= 0 && newX < Tak.boardSize
        			&& newY >= 0 && newY < Tak.boardSize){
        		neighbors.add(board[newY][newX]);
        	}
        		
    	}
    	
		return neighbors;
    	
    };
    
    public static List<Tile> getValidMoves(Tile tile){
    	
    	List<Tile> validMoves = new ArrayList<>();
    	int[] points = new int[]{
    		-1,0,	//Left
    		0,-1,	//Up
    		0,1,	//Down
    		1,0		//Right
    	};
    	
    	for(int i = 0; i < points.length; i++){
    		int dx = points[i];
    		int dy = points[++i];
    		
        	int newX = tile.getPoint().x + dx;
        	int newY = tile.getPoint().y + dy;
        	
        	if(newX >= 0 && newX < Tak.boardSize
        			&& newY >= 0 && newY < Tak.boardSize){
        		validMoves.add(board[newY][newX]);
        		System.out.println("Valid: [" + newX + ", " + newY + "]");
        	}
        		
    	}
    	
		return validMoves;
    	
    };
	
	public boolean isRoad(Point[] path, Point initPoint, Point finalPoint) {
        Tile[][] tiles = getBoard();
        for (Point point : path) {
            if ((tiles[(int)point.getX()][(int)point.getY()].isOccupied())
                    && (!point.equals(initPoint))
                    && (!point.equals(finalPoint))) {
                return false;
            }
        }
        return true;
    }
	
    public static void checkState() {
        if (Win.isComplete()) {
        	System.out.println("Game Over!" + " Player " + PlayerType.WHITE.toString() + " Won!");
            Tak.playable = false;
            //playWinAnimation(road);
        }
    }
    
    public static void setCapCount(PlayerType player)
    {
    	if(player == PlayerType.BLACK) {
        	if(blackCapCount < Tak.capstoneLimit){
        		blackCapCount++;
        	System.out.println(player.toString() + ": " + blackCapCount);
        	}
    	}
    	else {
        	if(whiteCapCount < Tak.capstoneLimit){
        		whiteCapCount++;
        	System.out.println(player.toString() + ": " + blackCapCount);
        	}
    	}
    }
    
    public static void setMaxTiles(){
    	switch(Tak.boardSize)
    	{
    	case 3:
    		maxWhiteTiles = 10;
    		maxBlackTiles = 10;
    		break;
    	case 4:
    		maxWhiteTiles = 15;
    		maxBlackTiles = 15;
    		break;
    	case 5:
    		maxWhiteTiles = 21;
    		maxBlackTiles = 21;
    		break;
    	case 6:
    		maxWhiteTiles = 30;
    		maxBlackTiles = 30;
    		break;
    	case 7:
    		maxWhiteTiles = 40;
    		maxBlackTiles = 40;
    		break;
    	case 8:
    		maxWhiteTiles = 50;
    		maxBlackTiles = 50;
    		break;
    	}
    }
    
    public static void setTileCount(PlayerType player)
    {
    	if(player == PlayerType.BLACK) {
        	if(blackTileCount < maxBlackTiles){
        		blackTileCount++;
        	}
        	else
        		Tak.playable = false;
    	}
    	else {
        	if(whiteTileCount < maxWhiteTiles){
        		whiteTileCount++;
        	}
        	else
        		Tak.playable = false;
    	}
    }

}
