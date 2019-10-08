package tokens;

import player.PlayerType;

public class Wall extends Token{

    public Wall(PlayerType player) {
        super(player, TokenType.WALL);
    }

}
