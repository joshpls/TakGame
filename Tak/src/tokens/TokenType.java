package tokens;
 
public enum TokenType {
    FLATSTONE("f"),
    WALL("w"),
    CAPSTONE("c");

    private String value;

    TokenType(String value) {
        this.value = value;

    }

    @Override
    public String toString() {
        return this.value;
    }

    public static TokenType fromString(String value){
        for (TokenType piece :TokenType.values()) {
            if (piece.value.equalsIgnoreCase(value)) {
              return piece;
            }
        }
        return null;
    }

}