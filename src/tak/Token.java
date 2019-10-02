package tak;

public class Token {
	private static String[] currentToken = new String[2];
	private static boolean[] capstoneStatus = new boolean [2];

	public Token() {
		// TODO Auto-generated constructor stub
		currentToken[0] = "flatstone";
		currentToken[1] = "flatstone";
	}
	
	public static void cycleToken(String current, int player, boolean isCapUsed){
		
		if (current == "flatstone")
			currentToken[player] = "wall";
		else if(current == "wall"){
			if(isCapUsed)
				currentToken[player] = "flatstone";
			else
				currentToken[player] = "capstone";
		}
		else{
			currentToken[player] = "flatstone";
		}
	}
	
	public static void setToken(int player, String current){
		
		currentToken[player] = current;
	}
	
	public static String getToken(int player){
		
		return currentToken[player];
	}
	
	public static boolean getCapstoneStatus(int player){
		return capstoneStatus[player];
	}
	
	public static void setCapstoneStatus(int player, boolean status){
		capstoneStatus[player] = status;
	}

}

