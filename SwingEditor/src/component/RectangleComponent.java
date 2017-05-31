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
	public RectangleComponent(Point startP, int width, int height, String name){
		super();
		
		type = ComponentType.RECTANGLE;
		this.startP.x = startP.x;
		this.startP.y = startP.y;
		this.width = width;
		this.height = height;
		this.name = name;
		
		shape = new Rectangle(startP.x, startP.y, width, height);
		
		setResizeHelper();
	}
	/**
	 * �떆�옉 醫뚰몴, �꼫鍮�, �넂�씠瑜� �씤�옄濡� 諛쏆븘�꽌 而댄룷�꼳�듃�쓽 startP, width, height瑜� �꽕�젙�븳�떎
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
	 * �몢 Point 媛앹껜瑜� �씤�옄濡� 諛쏆븘�꽌 而댄룷�꼳�듃�쓽 startP, width, height瑜� �꽕�젙�븳�떎
	 * @param first
	 * @param last
	 */
	@Override
	public void setSize(Point first, Point last) {
		// �옉�� x,y�쓣 李얠븘 start* 蹂��닔�뿉 ���옣
		startP.x = (first.x>=last.x)?last.x:first.x;
		startP.y = (first.y>=last.y)?last.y:first.y;
		width = Math.abs(first.x-last.x);
		height = Math.abs(first.y-last.y);
		
		Rectangle rectangle = (Rectangle) shape;
		rectangle.setBounds(startP.x, startP.y, width, height);		
		
		setResizeHelper();
	}
	/**
	 * p媛� 而댄룷�꼳�듃 �궡遺��쓽 醫뚰몴�씤吏� �솗�씤�븳�떎
	 * @param p 
	 * @return p媛� 而댄룷�꼳�듃 �궡遺��뿉 �엳�쑝硫� true �븘�땲硫� false
	 */
	@Override
	public boolean selected(Point p) {
		Rectangle rectangle = (Rectangle) shape;
		boolean isSelected = rectangle.contains(p);
		
		return isSelected;
	}

	/**
	 * (x, y)媛� 而댄룷�꼳�듃 �궡遺��쓽 醫뚰몴�씤吏� �솗�씤�븳�떎
	 * @param x
	 * @param y
	 * @return (x, y)媛� 而댄룷�꼳�듃 �궡遺��뿉 �엳�쑝硫� true �븘�땲硫� false
	 */
	@Override
	public boolean selected(int x, int y) {
		Rectangle rectangle = (Rectangle) shape;
		boolean isSelected = rectangle.contains(x, y);
		
		return isSelected;
	}
	@Override
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
		final String ACTUALCLASS = "RectangleComponent";
		
		String code = "\t\t\t\t\t"+
					  COMPONENTCLASS+SPACE+name+EQUAL+"new"+SPACE+ACTUALCLASS
					  +OPENBRACKET+QUOTE+name+QUOTE+CLOSEBRACKET+SEMICOLON+NEWLINE+"\t\t\t\t\t"
					  +name+DOT+"setSize"+OPENBRACKET+"new"+SPACE+"Point"+OPENBRACKET+startP.x+COMMA+startP.y+CLOSEBRACKET
					  +COMMA+height+COMMA+width+CLOSEBRACKET+SEMICOLON+NEWLINE+"\t\t\t\t\t"
					  +"frame.addComponent("+name+")"+SEMICOLON+NEWLINE+NEWLINE;
		return code;
	}
}
