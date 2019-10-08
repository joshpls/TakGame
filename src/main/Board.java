package main;

import java.util.List;

import player.PlayerType;
import tokens.Token;

import java.util.ArrayList;

public class Board {

    private Tile[][] board = new Tile[Tak.boardSize][Tak.boardSize];
    private Win roads = new Win();
    protected Token lastTokenPlaced;
	
	public Board()
	{
		setupBoard();
	}
	
	public void setupBoard()
	{
        for (int i = 0; i < Tak.boardSize; i++) {
            for (int j = 0; j < Tak.boardSize; j++) {
                Tile tile = new Tile(new Coordinate(i, j));
                tile.setTranslateX(j * (Tak.boardWidth/Tak.boardSize));
                tile.setTranslateY(i * (Tak.boardHeight/Tak.boardSize));
                
                Tak.boardPane.getChildren().add(tile);
                roads.addToRoad(tile);
                board[j][i] = tile;
            }
        }
        
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
        Tak.isCapUsed = false;
	}
	
	public Tile[][] getBoard()
	{
		return board;
	}
	
	public boolean isRoad(Coordinate[] path, Coordinate initCoordinate,
            Coordinate finalCoordinate) {
        Tile[][] tiles = getBoard();
        for (Coordinate coordinate : path) {
            if ((tiles[coordinate.getX()][coordinate.getY()].isOccupied())
                    && (!coordinate.equals(initCoordinate))
                    && (!coordinate.equals(finalCoordinate))) {
                return false;
            }
        }
        return true;
    }
	
    public static void checkState() {
        if (Win.isComplete()) {
        	System.out.println("Game Over!" + " Player " + PlayerType.WHITE.toString() + " Won!");
            Tak.playable = false;
//                playWinAnimation(road);
        }
    }

}