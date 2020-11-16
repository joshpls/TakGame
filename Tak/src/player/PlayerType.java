package player;

public enum PlayerType {
    WHITE("W"),
    BLACK("B");

    private String value;
    private int selection = 1;

    PlayerType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    public int getSelection(){return selection;}
    public void setSelection(int num){this.selection = num;}
}
