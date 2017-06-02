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
}