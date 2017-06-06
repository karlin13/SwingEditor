package component;

public enum ComponentType {
	NONE, JLABEL, JBUTTON;
	
	public static ComponentType fromString(String type){
		ComponentType _type;
		if(type == "JLABEL"){
			_type = JLABEL;
		}
		else if(type == "JBUTTON"){
			_type = JBUTTON;
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
		else if(type == JBUTTON)
			code = "JButton";
		
		return code;
	}
}