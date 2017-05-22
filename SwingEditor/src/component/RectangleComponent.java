package component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class RectangleComponent extends Component{
	public RectangleComponent(){
		shape = new Rectangle(start.x, start.y, width, height);
	}

	@Override
	public void setSize(Point start, int width, int height) {
		Rectangle rectangle = (Rectangle) shape;
		rectangle.setBounds(start.x, start.y, width, height);
	}

	@Override
	public boolean selected(Point p) {
		Rectangle rectangle = (Rectangle) shape;
		boolean isSelected = rectangle.contains(p);
		
		return isSelected;
	}

}
