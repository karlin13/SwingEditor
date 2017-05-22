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
	
	private int dx, dy;//컴포넌트 이동할 때 쓰는 변수
	
	private boolean drawTemp;
	
	//operations
	public EditorPanel() {
		components = new LinkedList<Component>();
		tempComponent = new RectangleComponent();
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
		if(drawTemp){
			tempComponent.draw(g);
		}
		//선택된 컴포넌트 resizeHelper 표시
		if(selectedComponent != null){
			tempComponent.drawResizeHelper(g);
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
			
			if(selectedComponent != null){
				//components에서 selectedComponent제거
				components.remove(selectedComponent);
				//selectedComponent 속성값 tempComponent에 복사
				Point startP = selectedComponent.getStartP();
				int width = selectedComponent.getWidth();
				int height = selectedComponent.getHeight();
				
				tempComponent.setSize(startP, width, height);
				
				dx = selectedComponent.getStartP().x-firstP.x;
				dy = selectedComponent.getStartP().y-firstP.y;
			}
		}
		public void mouseReleased(MouseEvent e){
			
			drawTemp = false;
			
			lastP.x = e.getX();
			lastP.y = e.getY();
			
			Component newComponent;
			
			if(selectedComponent == null){
				//새 컴포넌트를 리스트에 추가한다
				newComponent = new RectangleComponent();
				newComponent.setSize(firstP , lastP);
			}
			else{
				Point startP = tempComponent.getStartP();
				int width = tempComponent.getWidth();
				int height = tempComponent.getHeight();
				
				selectedComponent.setSize(startP, width, height);
				newComponent = selectedComponent;
			}

			addComponent(newComponent);
			
			//에디터 패널 갱신
			repaint();
		}
	}
	class EditorMouseMotionAdapter extends MouseMotionAdapter{
		 public void mouseDragged(MouseEvent e){
			 drawTemp = true;
			 
			 if(selectedComponent == null){
				 tempP.x = e.getX();
				 tempP.y = e.getY();
				 
				 tempComponent.setSize(firstP, tempP);
			 }
			 else{
				 tempP.x = e.getX()+dx;
				 tempP.y = e.getY()+dy;
				 int width = tempComponent.getWidth();
				 int height = tempComponent.getHeight();
				 
				 tempComponent.setSize(tempP, width, height);
			 }
			 
			 repaint();
		 }
	}
	
}
