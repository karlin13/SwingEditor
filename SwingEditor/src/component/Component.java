package component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Vector;

public abstract class Component {
	
	//attributes
	protected Shape shape;
	
	protected Point start;
	protected int width;
	protected int height;
	protected String componentType;
	
	private Rectangle[] resizeHelper;
	private final int resizeHelperLen = 6;
	// operations
	public Component(){
		resizeHelper = new Rectangle[8];
				
		for(int i=0;i<8;i++)
			resizeHelper[i] = new Rectangle();
	
	}
	
	public abstract void setSize(Point start, int width, int height);
	public abstract void setSize(Point first, Point last);
	public abstract boolean selected(Point p);
	public abstract boolean selected(int x, int y);
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.draw(shape);
	}
	protected void setResizeHelper(){
		int startX = start.x;
		int midX = start.x+(width/2);
		int endX = start.x+width;
		int startY = start.y;
		int midY = start.y+(height/2);
		int endY = start.y+height;
			
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
	public Direction selectedResizeHelper(Point p){
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
	
	// getters
	public Point getStart(){return start;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public String getComponentType(){return componentType;}
}
