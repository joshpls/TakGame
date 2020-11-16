package main;

import java.util.ArrayList;
import java.util.List;
import player.PlayerType;
import tokens.Token;
import tokens.Wall;

public class Win {

    private static List<Tile> centerBoard = new ArrayList<>();
    private static List<Tile> upperBorder = new ArrayList<>();
    private static List<Tile> lowerBorder = new ArrayList<>();
    private static List<Tile> leftBorder = new ArrayList<>();
    private static List<Tile> rightBorder = new ArrayList<>();
    
    public void addToRoad(Tile tile)
    {
    	
    	if(tile.getPoint().getX() == 0)
    		upperBorder.add(tile);
    	if(tile.getPoint().getY() == 0)
    		leftBorder.add(tile);
    	if(tile.getPoint().getY() == Tak.boardSize-1)
    		rightBorder.add(tile);
    	if(tile.getPoint().getX() == Tak.boardSize-1)
    		lowerBorder.add(tile);
    	if(tile.getPoint().getX() != 0 && tile.getPoint().getY() != 0
    			&& tile.getPoint().getX() != Tak.boardSize-1 && tile.getPoint().getY() != Tak.boardSize-1)
        	centerBoard.add(tile);
    	
    }
    
    public static boolean checkVerticalRoad()
    {
    	Token Bw = new Wall(PlayerType.BLACK);
    	Token Ww = new Wall(PlayerType.WHITE);
    	
    	Tile startToken, endToken;
    	for(Tile upperTile : upperBorder){
//    		System.out.println("Coords: " + tile.getPoint());
    		if(upperTile.isOccupied()){
    			System.out.println("Coords: [" + upperTile.getPoint().getX() + ", " + upperTile.getPoint().getY() + "]");
    			if(!upperTile.getToken().toString().equals(Bw.toString())
    					&& !upperTile.getToken().toString().equals(Ww.toString()))
    			{
    				startToken = upperTile;
    				// Check lower Border for token of same player.
    				for(Tile lowerTile : lowerBorder){
    		    		if(lowerTile.isOccupied()) {
    		    			System.out.println("Coords: [" + lowerTile.getPoint().getX() + ", " + lowerTile.getPoint().getY() + "]");
    		    			if(lowerTile.getToken().getPlayer().equals(upperTile.getToken().getPlayer())
    		    					&& (!lowerTile.getToken().toString().equals(Bw.toString())
    		    							&& !lowerTile.getToken().toString().equals(Ww.toString())))
    		    			{
    		    				endToken = lowerTile;
    		    				return true;
    		    			}
    		    			
    		    		}
    		    	}
    			}
    		}
    	}
		return false;
    }
    
    public static boolean checkHorizontalRoad()
    {
    	Token Bw = new Wall(PlayerType.BLACK);
    	Token Ww = new Wall(PlayerType.WHITE);
    	
    	Tile startToken, endToken;
    	for(Tile leftTile : leftBorder) {
//    		System.out.println("Coords: " + tile.getPoint());
    		if(leftTile.isOccupied()) {
    			System.out.println("Coords: [" + leftTile.getPoint().getX() + ", " + leftTile.getPoint().getY() + "]");
    			if(!leftTile.getToken().toString().equals(Bw.toString())
    					&& !leftTile.getToken().toString().equals(Ww.toString()))
    			{
    				startToken = leftTile;
    				// Check right Border for token of same player.
    				for(Tile rightTile : rightBorder) {
    		    		if(rightTile.isOccupied()) {
    		    			System.out.println("Coords: [" + rightTile.getPoint().getX() + ", " + rightTile.getPoint().getY() + "]");
    		    			if(rightTile.getToken().getPlayer().equals(leftTile.getToken().getPlayer())
    		    					&& (!rightTile.getToken().toString().equals(Bw.toString())
    		    							&& !rightTile.getToken().toString().equals(Ww.toString())))
    		    			{
    		    				endToken = rightTile;
    		    				return true;
    		    			}
    		    			
    		    		}
    		    	}
    			}
    		}
    	}
		return false;
    	
    }
    
    private static void printBoard() {
    	System.out.print("Upper: ");
    	for(Tile tile : upperBorder)
    		System.out.print("[" + tile.getTokenString() + "]");
    	System.out.print("Right: ");
    	for(Tile tile : rightBorder)
    		System.out.print("[" + tile.getTokenString() + "]");
    	System.out.print("Left: ");
    	for(Tile tile : leftBorder)
    		System.out.print("[" + tile.getTokenString() + "]");
    	System.out.print("Lower: ");
    	for(Tile tile : lowerBorder)
    		System.out.print("[" + tile.getTokenString() + "]");
    	System.out.print("Center: ");
    	for(Tile tile : centerBoard) {
    		System.out.print("[" + tile.getTokenString() + "]");
    	}
    	System.out.println();
    }

    public static boolean isComplete() {
    	System.out.println("Check Complete");
    	if(centerBoard.isEmpty())
    		return false;
    	
//    	printBoard();
    	
    	if(checkHorizontalRoad() || checkVerticalRoad()) {
    		// Check middle tiles for complete road.
    		return true;
    	}
    	
    	return false;
    }

}
