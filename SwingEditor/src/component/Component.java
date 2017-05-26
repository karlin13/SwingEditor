package component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

public abstract class Component {
	//attributes
	protected Shape shape;
	
	/**
	 * 시작 좌표
	 */
	protected Point startP;
	/**
	 * 너비
	 */
	protected int width;
	/**
	 * 높이
	 */
	protected int height;
	/**
	 * 컴포넌트 종류
	 */
	protected ComponentType type;
	/**
	 * 컴포넌트 이름
	 */
	protected String name;
	/**
	 * 컴포넌트 색
	 */
	private Color color;
	
	/**
	 * 누르면 리사이징 할 수 있는 작은 정사각형
	 */
	private Rectangle[] resizeHelper;
	/**
	 * resizeHelper 변의 길이
	 */
	private final int resizeHelperLen = 12;
	
	public static final int MIN_WIDTH = 12;
	public static final int MIN_HEIGHT = 12;
	
	// operations
	public Component(){
		resizeHelper = new Rectangle[8];
				
		for(int i=0;i<8;i++)
			resizeHelper[i] = new Rectangle();
		
		startP = new Point();
		setDefaultColor();
	}
	
	public abstract void setSize(Point start, int width, int height);
	public abstract void setSize(Point first, Point last);
	public abstract boolean selected(Point p);
	public abstract boolean selected(int x, int y);
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.draw(shape);
	}
	
	/**
	 * 컴포넌트의 위치 정보(시작 좌표, 너비, 높이)가 변경됐을때 resizeHelper의 위치도 조정하기 위해 호출하는 함수
	 */
	protected void setResizeHelper(){
		int startX = startP.x;
		int midX = startP.x+(width/2);
		int endX = startP.x+width;
		int startY = startP.y;
		int midY = startP.y+(height/2);
		int endY = startP.y+height;
			
		resizeHelper[0].setBounds(startX-resizeHelperLen/2, startY-resizeHelperLen/2, resizeHelperLen, resizeHelperLen);
		resizeHelper[1].setBounds(midX-resizeHelperLen/2, startY-resizeHelperLen/2, resizeHelperLen, resizeHelperLen);
		resizeHelper[2].setBounds(endX-resizeHelperLen/2, startY-resizeHelperLen/2, resizeHelperLen, resizeHelperLen);
		resizeHelper[3].setBounds(endX-resizeHelperLen/2, midY-resizeHelperLen/2, resizeHelperLen, resizeHelperLen);
		resizeHelper[4].setBounds(endX-resizeHelperLen/2, endY-resizeHelperLen/2, resizeHelperLen, resizeHelperLen);
		resizeHelper[5].setBounds(midX-resizeHelperLen/2, endY-resizeHelperLen/2, resizeHelperLen, resizeHelperLen);
		resizeHelper[6].setBounds(startX-resizeHelperLen/2, endY-resizeHelperLen/2, resizeHelperLen, resizeHelperLen);
		resizeHelper[7].setBounds(startX-resizeHelperLen/2, midY-resizeHelperLen/2, resizeHelperLen, resizeHelperLen);
	}
	public void drawResizeHelper(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.GRAY);
		for(int i=0;i<resizeHelper.length;i++)
			g2.fill(resizeHelper[i]);
	}
	/**
	 * 현재 눌려진 resizeHelper의 방향을 반환한다
	 * @param p
	 * @return
	 */
	public Direction getResizeHelperDirection(Point p){
		if(resizeHelper[0].contains(p)){
			return Direction.UL;
		}
		else if(resizeHelper[1].contains(p)){
			return Direction.U;
		}
		else if(resizeHelper[2].contains(p)){
			return Direction.UR;
		}
		else if(resizeHelper[3].contains(p)){
			return Direction.R;
		}
		else if(resizeHelper[4].contains(p)){
			return Direction.DR;
		}
		else if(resizeHelper[5].contains(p)){
			return Direction.D;
		}
		else if(resizeHelper[6].contains(p)){
			return Direction.DL;
		}
		else if(resizeHelper[7].contains(p)){
			return Direction.L;
		}
		else{
			return Direction.NONE;
		}
	}
	/**
	 * 현재 눌려진 resizeHelper의 방향을 반환한다
	 * @param p
	 * @return
	 */
	public Direction getResizeHelperDirection(int x, int y){
		if(resizeHelper[0].contains(x, y)){
			return Direction.UL;
		}
		else if(resizeHelper[1].contains(x, y)){
			return Direction.U;
		}
		else if(resizeHelper[2].contains(x, y)){
			return Direction.UR;
		}
		else if(resizeHelper[3].contains(x, y)){
			return Direction.R;
		}
		else if(resizeHelper[4].contains(x, y)){
			return Direction.DR;
		}
		else if(resizeHelper[5].contains(x, y)){
			return Direction.D;
		}
		else if(resizeHelper[6].contains(x, y)){
			return Direction.DL;
		}
		else if(resizeHelper[7].contains(x, y)){
			return Direction.L;
		}
		else{
			return Direction.NONE;
		}
	}
	
	/**
	 * 선택 안됐을때 색
	 */
	public void setDefaultColor(){
		color = Color.BLACK;
	}
	/**
	 * 선택 됐을떄 색
	 */
	public void setHighlightColor(){
		color = Color.BLUE;
	}

	/**
	 * 컴포넌트의 시작 x좌표, 시작 y좌표, 너비, 높이, 이름, 종류를 json으로 만들어서 반환한다
	 * @return
	 */
	public String toJson(){
		//x, y, width, height, componentType
		final String OPEN_BRACKET = "{";
		final String CLOSE_BRACKET = "}";
		final String QUOTE = "\"";
		final String COMMA = ",";
		final String COLON = ":";
		
		//TOOD: add component name
		String jsonString = OPEN_BRACKET
							+QUOTE+"x"+QUOTE+COLON+QUOTE+startP.x+QUOTE+COMMA
							+QUOTE+"y"+QUOTE+COLON+QUOTE+startP.y+QUOTE+COMMA
							+QUOTE+"width"+QUOTE+COLON+QUOTE+width+QUOTE+COMMA
							+QUOTE+"height"+QUOTE+COLON+QUOTE+height+QUOTE+COMMA
							+QUOTE+"type"+QUOTE+COLON+QUOTE+type+QUOTE
							+QUOTE+"name"+QUOTE+COLON+QUOTE+name+QUOTE
							+CLOSE_BRACKET;
		
		return jsonString;
	}
	
	//getters
	public Point getStartP(){return startP;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public ComponentType getType(){return type;}
	public String getName(){return name;}
}
