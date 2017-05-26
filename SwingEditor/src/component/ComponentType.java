package component;

public enum ComponentType {
	NONE, RECTANGLE;
	
	/**
	 * str로 부터 적절한 ComponentType을 반환한다
	 * @param str
	 * @return
	 */
	public static ComponentType fromString(String str){
		ComponentType type = NONE;
		
		if(str.equals("RECTANGLE"))
			type = RECTANGLE;
		
		return type;
	} 
}
