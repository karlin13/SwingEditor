package component;

public enum ComponentType {
	NONE, RECTANGLE;
	
	public static ComponentType fromString(String str){
		ComponentType type = NONE;
		
		if(str.equals("RECTANGLE"))
			type = RECTANGLE;
		
		return type;
	} 
}
