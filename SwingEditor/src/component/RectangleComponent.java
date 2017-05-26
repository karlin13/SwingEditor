package component;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class RectangleComponent extends Component{
	public RectangleComponent(){
		startP = new Point();
		type = ComponentType.RECTANGLE;
		setDefaultColor();
		
		shape = new Rectangle(startP.x, startP.y, width, height);
	}
	public RectangleComponent(int x, int y, int width, int height){
		super();
		
		startP = new Point();
		type = ComponentType.RECTANGLE;
		setDefaultColor();
		
		startP.x = x;
		startP.y = y;
		this.width = width;
		this.height = height;
		
		shape = new Rectangle(startP.x, startP.y, width, height);
		
		setResizeHelper();
	}

	@Override
	public void setSize(Point start, int width, int height) {
		startP.x = start.x;
		startP.y = start.y;
		this.width = width;
		this.height = height;
		
		Rectangle rectangle = (Rectangle) shape;
		rectangle.setBounds(start.x, start.y, width, height);
		
		setResizeHelper();
	}
	@Override
	public void setSize(Point first, Point last) {
		// 작은 x,y을 찾아 start* 변수에 저장
		startP.x = (first.x>=last.x)?last.x:first.x;
		startP.y = (first.y>=last.y)?last.y:first.y;
		width = Math.abs(first.x-last.x);
		height = Math.abs(first.y-last.y);
		
		Rectangle rectangle = (Rectangle) shape;
		rectangle.setBounds(startP.x, startP.y, width, height);		
		
		setResizeHelper();
	}
	@Override
	public boolean selected(Point p) {
		Rectangle rectangle = (Rectangle) shape;
		boolean isSelected = rectangle.contains(p);
		
		return isSelected;
	}

	@Override
	public boolean selected(int x, int y) {
		Rectangle rectangle = (Rectangle) shape;
		boolean isSelected = rectangle.contains(x, y);
		
		return isSelected;
	}

}
