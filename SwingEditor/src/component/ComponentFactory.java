package component;

public class ComponentFactory {
	public static Component createComponent(ComponentType type, String name){
		Component component = null;
		
		switch(type){
			case RECTANGLE:
				component = new RectangleComponent(name);
				break;
		}
		
		return component;
	}
	public static Component createComponent(ComponentType type, int x, int y, int width, int height, String name){
		Component component = null;
		
		switch(type){
			case RECTANGLE:
				component = new RectangleComponent(x, y, width, height, name);
				break;
		}
		
		return component;
	}
}
