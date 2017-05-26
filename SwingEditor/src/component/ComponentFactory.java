package component;

public class ComponentFactory {
	/**
	 * 컴포넌트의 종류가 type인 컴포넌트를 생성하여 반환한다
	 * @param type
	 * @param name
	 * @return
	 */
	public static Component createComponent(ComponentType type, String name){
		Component component = null;
		
		switch(type){
			case RECTANGLE:
				component = new RectangleComponent(name);
				break;
		}
		
		return component;
	}
	/**
	 * 컴포넌트의 종류가 type인 컴포넌트를 생성하여 반환한다
	 * @param type
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param name
	 * @return
	 */
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
