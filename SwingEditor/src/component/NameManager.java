package component;

public class NameManager {
	private static int countComp;
	
	public static void initCountComp(){
		countComp = -1;
	}
	public static void setCountComp(int num){
		countComp = num;
	}
	public static String genName(){
		countComp++;
		return "var"+countComp;
	}
	public static int getCountComp(){
		return countComp;
	}
}
