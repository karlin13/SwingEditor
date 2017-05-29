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
	 * �떆�옉 醫뚰몴
	 */
	protected Point startP;
	/**
	 * �꼫鍮�
	 */
	protected int width;
	/**
	 * �넂�씠
	 */
	protected int height;
	/**
	 * 而댄룷�꼳�듃 醫낅쪟
	 */
	protected ComponentType type;
	/**
	 * 而댄룷�꼳�듃 �씠由�
	 */
	protected String name;
	/**
	 * 而댄룷�꼳�듃 �깋
	 */
	private Color color;
	
	/**
	 * �늻瑜대㈃ 由ъ궗�씠吏� �븷 �닔 �엳�뒗 �옉�� �젙�궗媛곹삎
	 */
	private Rectangle[] resizeHelper;
	/**
	 * resizeHelper 蹂��쓽 湲몄씠
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
	 * 而댄룷�꼳�듃�쓽 �쐞移� �젙蹂�(�떆�옉 醫뚰몴, �꼫鍮�, �넂�씠)媛� 蹂�寃쎈릱�쓣�븣 resizeHelper�쓽 �쐞移섎룄 議곗젙�븯湲� �쐞�빐 �샇異쒗븯�뒗 �븿�닔
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
	 * �쁽�옱 �닃�젮吏� resizeHelper�쓽 諛⑺뼢�쓣 諛섑솚�븳�떎
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
	 * �쁽�옱 �닃�젮吏� resizeHelper�쓽 諛⑺뼢�쓣 諛섑솚�븳�떎
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
	 * �꽑�깮 �븞�릱�쓣�븣 �깋
	 */
	public void setDefaultColor(){
		color = Color.BLACK;
	}
	/**
	 * �꽑�깮 �릱�쓣�뻹 �깋
	 */
	public void setHighlightColor(){
		color = Color.BLUE;
	}

	/**
	 * 而댄룷�꼳�듃�쓽 �떆�옉 x醫뚰몴, �떆�옉 y醫뚰몴, �꼫鍮�, �넂�씠, �씠由�, 醫낅쪟瑜� json�쑝濡� 留뚮뱾�뼱�꽌 諛섑솚�븳�떎
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
							+QUOTE+"type"+QUOTE+COLON+QUOTE+type+QUOTE+COMMA
							+QUOTE+"name"+QUOTE+COLON+QUOTE+name+QUOTE+COMMA
							+CLOSE_BRACKET;
		
		return jsonString;
	}
	
	//getters
	public Point getStartP(){return startP;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public ComponentType getType(){return type;}
	public String getName(){return name;}
	
	//setters
	public void setName(String name){this.name = name;}
	
	public String toJavaCode(){
		final String OPENBRACKET = "(";
		final String CLOSEBRACKET = ")";
		final String QUOTE = "\"";
		final String COMMA = ",";
		final String DOT = ".";
		final String EQUAL = "=";
		final String SPACE =" ";
	    final String SEMICOLON = ";";
	    final String NEWLINE = "\n";
		
		final String COMPONENTCLASS = "Component";
		final String RECTANGLECLASS = "RectangleComponent";
		
		String code = COMPONENTCLASS+SPACE+name+EQUAL+"new"+SPACE+RECTANGLECLASS
					  +OPENBRACKET+QUOTE+name+QUOTE+CLOSEBRACKET+SEMICOLON+NEWLINE
					  +name+DOT+"setSize"+OPENBRACKET+"new"+SPACE+"Point"+OPENBRACKET+startP+CLOSEBRACKET
					  +COMMA+height+COMMA+width+CLOSEBRACKET+SEMICOLON;
		
		return code;
		

	}
}
