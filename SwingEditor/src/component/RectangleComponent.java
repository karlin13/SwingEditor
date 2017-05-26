package component;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class RectangleComponent extends Component{
	public RectangleComponent(String name){
		type = ComponentType.RECTANGLE;
		this.name = name;
		
		shape = new Rectangle(startP.x, startP.y, width, height);
	}
	public RectangleComponent(int x, int y, int width, int height, String name){
		super();
		
		type = ComponentType.RECTANGLE;
		startP.x = x;
		startP.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		
		shape = new Rectangle(startP.x, startP.y, width, height);
		
		setResizeHelper();
	}
	/**
	 * 시작 좌표, 너비, 높이를 인자로 받아서 컴포넌트의 startP, width, height를 설정한다
	 * @param start
	 * @param width
	 * @param height
	 */
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
	/**
	 * 두 Point 객체를 인자로 받아서 컴포넌트의 startP, width, height를 설정한다
	 * @param first
	 * @param last
	 */
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
	/**
	 * p가 컴포넌트 내부의 좌표인지 확인한다
	 * @param p 
	 * @return p가 컴포넌트 내부에 있으면 true 아니면 false
	 */
	@Override
	public boolean selected(Point p) {
		Rectangle rectangle = (Rectangle) shape;
		boolean isSelected = rectangle.contains(p);
		
		return isSelected;
	}

	/**
	 * (x, y)가 컴포넌트 내부의 좌표인지 확인한다
	 * @param x
	 * @param y
	 * @return (x, y)가 컴포넌트 내부에 있으면 true 아니면 false
	 */
	@Override
	public boolean selected(int x, int y) {
		Rectangle rectangle = (Rectangle) shape;
		boolean isSelected = rectangle.contains(x, y);
		
		return isSelected;
	}

}
