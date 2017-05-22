package component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class RectangleComponent extends Component{
	public RectangleComponent(){
		start = new Point();
	
		shape = new Rectangle(start.x, start.y, width, height);
	}

	@Override
	public void setSize(Point start, int width, int height) {
		Rectangle rectangle = (Rectangle) shape;
		rectangle.setBounds(start.x, start.y, width, height);
	}
	@Override
	public void setSize(Point first, Point last) {
		// 작은 x,y을 찾아 start* 변수에 저장
		start.x = (first.x>=last.x)?last.x:first.x;
		start.y = (first.y>=last.y)?last.y:first.y;
		width = Math.abs(first.x-last.x);
		height = Math.abs(first.y-last.y);
		
		Rectangle rectangle = (Rectangle) shape;
		rectangle.setBounds(start.x, start.y, width, height);		
		
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
