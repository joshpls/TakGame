package main;

import java.util.ArrayList;
import java.util.List;

import player.PlayerType;
import tokens.Flatstone;
import tokens.Token;
import tokens.TokenType;

public class Win {
    private Tile[][] tiles;
    private static List<Tile> centerBoard = new ArrayList<>();
    private static List<Tile> upperBorder = new ArrayList<>();
    private static List<Tile> lowerBorder = new ArrayList<>();
    private static List<Tile> leftBorder = new ArrayList<>();
    private static List<Tile> rightBorder = new ArrayList<>();
    
    public void addToRoad(Tile tile)
    {
    	
    	if(tile.getCoordinate().getX() == 0)
    		upperBorder.add(tile);
    	if(tile.getCoordinate().getY() == 0)
    		leftBorder.add(tile);
    	if(tile.getCoordinate().getY() == Tak.boardSize)
    		rightBorder.add(tile);
    	if(tile.getCoordinate().getX() == Tak.boardSize)
    		lowerBorder.add(tile);
    	if(tile.getCoordinate().getX() != 0 && tile.getCoordinate().getY() != 0
    			&& tile.getCoordinate().getX() != Tak.boardSize && tile.getCoordinate().getY() != Tak.boardSize)
        	centerBoard.add(tile);
    }
    
    public static boolean checkVerticalRoad(Token token)
    {
    	
    	boolean containsToken = false;
    	for(Tile tile : upperBorder){
//    		System.out.println("Coords: " + tile.getCoordinate());
    		if(tile.isOccupied()){
        		System.out.println("Coords: " + tile.getCoordinate());
    			if(tile.getToken().toString().equals(token.toString()))
    				containsToken = true;
    		}
    	}
    	if(!containsToken){
    		System.out.println("Ruh Roh");
    		return false;
    	}
    	else
    		System.out.println("Contains Upper Border.");
    	containsToken = false;
    	for(Tile tile : lowerBorder){
    		if(tile.isOccupied())
    			if(tile.getToken().toString().equals(token.toString()))
    				containsToken = true;
    	}
    	if(containsToken)
        	System.out.println("Contains Lower Border.");
    	return containsToken;
    }
    
    public static boolean checkHorizontalRoad(Token token)
    {
    	boolean containsToken = false;
    	for(Tile tile : rightBorder){
    		if(tile.isOccupied()){
    			if(tile.getToken().toString().equals(token.toString()))
    				containsToken = true;
    		}
    	}
    	if(!containsToken)
    		return false;
    	else
    		System.out.println("Contains Right Border.");
    	containsToken = false;
    	for(Tile tile : leftBorder){
    		if(tile.isOccupied())
    			if(tile.getToken().toString().equals(token.toString()))
    				containsToken = true;
    	}
    	if(containsToken)
        	System.out.println("Contains Left Border.");
    	return containsToken;
    }

    public static boolean isComplete() {
    	System.out.println("Check Complete");
    	if(centerBoard.isEmpty())
    		return false;
    	
    	if((!checkHorizontalRoad(new Flatstone(PlayerType.WHITE)) && !checkVerticalRoad(new Flatstone(PlayerType.WHITE)) )
    			|| (!checkHorizontalRoad(new Flatstone(PlayerType.BLACK)) && !checkVerticalRoad(new Flatstone(PlayerType.BLACK))))
    		return false;

    	return true;
    }

}