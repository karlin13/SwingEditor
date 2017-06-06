package component;

public enum ComponentType {
	NONE, JLABEL;
	
	public static ComponentType fromString(String type){
		ComponentType _type;
		if(type == "JLABEL"){
			_type = JLABEL;
		}
		else{
			_type = NONE;
		}
		
		return _type;
	}
	public static String toCode(ComponentType type){
		String code = null;
		
		if(type == JLABEL)
			code = "JLabel";
		
		return code;
	}
}