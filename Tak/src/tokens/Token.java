package tokens;

import player.PlayerType;

public abstract class Token {
    private TokenType type;
    private PlayerType player;

    /*
    	A class that represents a single Token on the board. It handles the specific moves/attack moves of that token.
    */
    public Token(PlayerType player,TokenType type){
        this.type=type;
        this.player=player;
    }

    public String toString(){
        return player.toString()+type.toString();
    }    

    public PlayerType getPlayer(){return player;} 

    public TokenType getType(){return type;}
}
