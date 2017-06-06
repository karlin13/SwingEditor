package component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class MockComponent extends JLabel{
	private ComponentType type;
	private String variableName;
	
	private Rectangle[] resizeHelper;
	private final int resizeHelperLen = 12;
	
	private boolean isSelected;
	
	private int dx;
	private int dy;
	
	public static final int MIN_WIDTH = 18;
	public static final int MIN_HEIGHT = 18;
	
	public MockComponent(String text){
		setText(text);
		
		this.type = ComponentType.JLABEL;
		this.variableName = text+this.type;
		
		unhighlightBorder();
		
		addMouseListener(new MockCompMouseAdapter());
		addMouseMotionListener(new MockCompMouseMotionAdapter());
		
		resizeHelper = new Rectangle[8];
		for(int i=0;i<8;i++)
			resizeHelper[i] = new Rectangle();
	}
		
	private void unhighlightBorder(){
		Border border = BorderFactory.createLineBorder(Color.BLACK);

		setBorder(border);
	}
	private void highlightBorder(){
		Border border = BorderFactory.createLineBorder(Color.BLUE);
		
		setBorder(border);
	}
	
	public void select(){
		isSelected = true;
		highlightBorder();
	}
	public void unselect(){
		isSelected = false;
		unhighlightBorder();
	}
	
	public String toCode(){
		final String OPENBRACKET = "(";
		final String CLOSEBRACKET = ")";
		final String QUOTE = "\"";
		final String COMMA = ",";
		final String DOT = ".";
		final String EQUAL = "=";
		final String SPACE =" ";
	    final String SEMICOLON = ";";
	    final String NEWLINE = "\n";
		
		final String CLASS = ComponentType.toCode(type);
		
		String code = NEWLINE+"\t\t"+
					  CLASS+SPACE+variableName+EQUAL+"new"+SPACE+CLASS
					  +OPENBRACKET+QUOTE+getText()+QUOTE+CLOSEBRACKET+SEMICOLON+NEWLINE+"\t\t"
					  +variableName+DOT+"setSize"+OPENBRACKET+getWidth()+COMMA+getHeight()+CLOSEBRACKET+SEMICOLON+NEWLINE+"\t\t"
					  +variableName+DOT+"setLocation"+OPENBRACKET+getX()+COMMA+getY()+CLOSEBRACKET+SEMICOLON+NEWLINE+"\t\t"
					  +"contentPane.add("+variableName+")"+SEMICOLON+NEWLINE;
		return code;
	}
	public String toJson(){
		//x, y, width, height, componentType
		final String OPEN_BRACKET = "{";
		final String CLOSE_BRACKET = "}";
		final String QUOTE = "\"";
		final String COMMA = ",";
		final String COLON = ":";
		
		//TOOD: add component name
		String jsonString = OPEN_BRACKET
							+QUOTE+"x"+QUOTE+COLON+QUOTE+getX()+QUOTE+COMMA
							+QUOTE+"y"+QUOTE+COLON+QUOTE+getY()+QUOTE+COMMA
							+QUOTE+"width"+QUOTE+COLON+QUOTE+getWidth()+QUOTE+COMMA
							+QUOTE+"height"+QUOTE+COLON+QUOTE+getHeight()+QUOTE+COMMA
							+QUOTE+"text"+QUOTE+COLON+QUOTE+getText()+QUOTE+COMMA
							+QUOTE+"type"+QUOTE+COLON+QUOTE+type+QUOTE+COMMA
							+QUOTE+"variableName"+QUOTE+COLON+QUOTE+variableName+QUOTE
							+CLOSE_BRACKET;
		
		return jsonString;
	}
	
	public ComponentType getType(){return type;}
	public String getVariableName(){return variableName;}
	public int getDx(){return dx;}
	public int getDy(){return dy;}
	
	public void setType(ComponentType type){this.type = type;}
	public void setVariableName(String variableName){this.variableName = variableName;}
	public void setDiff(int startX, int startY, int curX, int curY){
		dx = startX-curX;
		dy = startY-curY;
	}
	public void setSizeNLocation(Point start, int width, int height, int MIN_X, int MIN_Y, int MAX_X, int MAX_Y) {
		int x = start.x;
		int y = start.y;
		int _width = Math.max(width, MIN_WIDTH);
		int _height = Math.max(height, MIN_HEIGHT);
		
		x = (x<MIN_X)?MIN_X:x;
		x = (x+_width>MAX_X)?MAX_X-_width:x;
		y = (y<MIN_Y)?MIN_Y:y;
		y = (y+_height>MAX_Y)?MAX_Y-_height:y;
		
		setLocation(x, y);
		setSize(_width, _height);
		setResizeHelper();
		
		repaint();
	}
	public void setSizeNLocation(Point first, Point last, int MIN_X, int MIN_Y, int MAX_X, int MAX_Y) {
		int x = (first.x>=last.x)?last.x:first.x;
		int y = (first.y>=last.y)?last.y:first.y;
		int _width = Math.max(Math.abs(first.x-last.x), MIN_WIDTH);
		int _height = Math.max(Math.abs(first.y-last.y), MIN_HEIGHT);
		
		x = (x<MIN_X)?MIN_X:x;
		x = (x+_width>MAX_X)?MAX_X:x;
		y = (y<MIN_Y)?MIN_Y:y;
		y = (y+_height>MAX_Y)?MAX_Y:y;
		
		setLocation(x, y);
		setSize(_width, _height);
		setResizeHelper();
		
		repaint();
	}

	private void setResizeHelper(){
		Point startP = getLocation();
		int width = getWidth();
		int height = getHeight();
		
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
	public Direction getResizeHelperDirection(int x, int y){
		Direction type;
		
		if(resizeHelper[0].contains(x, y)){
			type = Direction.UL;
		}
		else if(resizeHelper[1].contains(x, y)){
			type = Direction.U;
		}
		else if(resizeHelper[2].contains(x, y)){
			type = Direction.UR;
		}
		else if(resizeHelper[3].contains(x, y)){
			type = Direction.R;
		}
		else if(resizeHelper[4].contains(x, y)){
			type = Direction.DR;
		}
		else if(resizeHelper[5].contains(x, y)){
			type = Direction.D;
		}
		else if(resizeHelper[6].contains(x, y)){
			type = Direction.DL;
		}
		else if(resizeHelper[7].contains(x, y)){
			type = Direction.L;
		}
		else{
			type = Direction.NONE;
		}
		
		return type;
	}
	//컴포넌트 선택
	class MockCompMouseAdapter extends MouseAdapter{
		public void mousePressed(MouseEvent e){
			redispatchToParent(e);
		}
		public void mouseReleased(MouseEvent e){
			redispatchToParent(e);
		}
		public void mouseClicked(MouseEvent e){
			redispatchToParent(e);
		}
	}
	class MockCompMouseMotionAdapter extends MouseMotionAdapter{
		public void mouseDragged(MouseEvent e){
			redispatchToParent(e);
		}
	}
	private void redispatchToParent(MouseEvent e){
		Component source = (Component)e.getSource();
		
		MouseEvent parentEvent = SwingUtilities.convertMouseEvent(source, e, source.getParent());
	    parentEvent.setSource(e.getSource());
	    source.getParent().dispatchEvent(parentEvent);
	}
}