package component;

public class ComponentFactory {
	public static Component createComponent(ComponentType type){
		Component component = null;
		
		switch(type){
			case RECTANGLE:
				component = new RectangleComponent();
				break;
		}
		
		return component;
	}
	public static Component createComponent(ComponentType type, int x, int y, int width, int height){
		Component component = null;
		
		switch(type){
			case RECTANGLE:
				component = new RectangleComponent(x, y, width, height);
				break;
		}
		
		return component;
	}
}
