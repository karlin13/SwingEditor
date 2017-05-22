package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

import component.Component;
import component.RectangleComponent;

public class EditorPanel extends JPanel {
	//attributes
	private LinkedList<Component> components;
	private Component tempComponent;
	private Component selectedComponent;

	private Point firstP;
	private Point lastP;
	private Point tempP;

	private boolean isTempDraw;
	
	//operations
	public EditorPanel() {
		components = new LinkedList<Component>();
		tempComponent = null;
		selectedComponent = null;
		
		firstP = new Point();
		lastP = new Point();
		tempP = new Point();
		
		addMouseListener(new EditorMouseAdapter());
		addMouseMotionListener(new EditorMouseMotionAdapter());
	}
	public void addComponent(Component component){
		components.add(component);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(200,200);
	}
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//현재 저장된 컴포너트 그림
		for(Component component:components){
			component.draw(g);
		}
		//임시 컴포넌트 그림
		if(isTempDraw){
			tempComponent.draw(g);
		}
		//선택된 컴포넌트 resizeHelper 표시
		if(selectedComponent != null){
			selectedComponent.drawResizeHelper(g);
		}
	}
	
	class EditorMouseAdapter extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			Iterator<Component> iterator = components.iterator();
			Component component;
			
			//아무 컴포넌트도 선택되지 않았으면 null
			selectedComponent = null;
			
			while(iterator.hasNext()){
				component = iterator.next();
				
				if(component.selected(e.getX(), e.getY())){
					selectedComponent = component;
					break;
				}
			}
			
			repaint();
		}
		public void mousePressed(MouseEvent e){
			firstP.x = e.getX();
			firstP.y = e.getY();
			
			//컴포넌트를 선택하고 있을 땐 리사이징 또는 이동만 가능하므로 새 객체를 생성안한다
			if(selectedComponent == null){
				tempComponent = new RectangleComponent();
			}
		}
		public void mouseReleased(MouseEvent e){
			
			isTempDraw = false;
			
			lastP.x = e.getX();
			lastP.y = e.getY();
			
			//새 컴포넌트를 리스트에 추가한다
			Component newComponent = new RectangleComponent();
			newComponent.setSize(firstP , lastP);
			
			addComponent(newComponent);
			
			//에디터 패널 갱신
			repaint();
		}
	}
	class EditorMouseMotionAdapter extends MouseMotionAdapter{
		 public void mouseDragged(MouseEvent e){
			 isTempDraw = true;
			 
			 tempP.x = e.getX();
			 tempP.y = e.getY();
			 
			 tempComponent.setSize(firstP, tempP);
			 
			 repaint();
		 }
	}
	
}
