package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import component.Component;
import component.Direction;
import component.RectangleComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorPanel extends JPanel {
	//attributes
	JPopupMenu contextMenu;
	
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
		initContextMenu();
		
		components = new LinkedList<Component>();
		tempComponent = new RectangleComponent();
		selectedComponent = null;
		
		firstP = new Point();
		lastP = new Point();
		tempP = new Point();
		
		addMouseListener(new EditorMouseAdapter());
		addMouseMotionListener(new EditorMouseMotionAdapter());
	}
	private void initContextMenu(){
		contextMenu = new JPopupMenu();
		JMenuItem deleteComponentMenu = new JMenuItem("delete");
		
		deleteComponentMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteComponentItemAction();
			}
        });
		
		contextMenu.add(deleteComponentMenu);
	}
	private void deleteComponentItemAction(){
		if(selectedComponent != null){
			deleteComponent();
		}
		repaint();
	}
	
	private void addComponent(Component component){
		components.add(component);
	}
	private void deleteComponent(){
		components.remove(selectedComponent);
		selectedComponent = null;
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
			if(e.getButton() == MouseEvent.BUTTON1){
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
			else if(e.getButton() == MouseEvent.BUTTON3 && selectedComponent != null){
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
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
				 Direction dir = tempComponent.getResizeHelperDirection(e.getX(), e.getY());
				 
				 int width=tempComponent.getWidth();
				 int height=tempComponent.getHeight();
				 
				 switch(dir){
					 case NONE:
						 tempP.x = e.getX()+dx;
						 tempP.y = e.getY()+dy;
						 width = tempComponent.getWidth();
						 height = tempComponent.getHeight();
						 break;
					 case UL:
						 tempP.x = e.getX();
						 tempP.y = e.getY();
						 width = selectedComponent.getWidth()+selectedComponent.getStartP().x-e.getX();
						 height = selectedComponent.getHeight()+selectedComponent.getStartP().y-e.getY();
						 break;
					 case U:
						 tempP.x = tempComponent.getStartP().x;
						 tempP.y = e.getY();
						 width = selectedComponent.getWidth();
						 height = selectedComponent.getHeight()+selectedComponent.getStartP().y-e.getY();
						 break;
					 case UR:
						 tempP.x = tempComponent.getStartP().x;
						 tempP.y = e.getY();
						 width = e.getX()-selectedComponent.getStartP().x;
						 height = selectedComponent.getHeight()+selectedComponent.getStartP().y-e.getY();
						 break;
					 case R:
						 tempP.x = tempComponent.getStartP().x;
						 tempP.y = tempComponent.getStartP().y;
						 width = e.getX()-selectedComponent.getStartP().x;
						 height = selectedComponent.getHeight();
						 break;
					 case DR:
						 tempP.x = tempComponent.getStartP().x;
						 tempP.y = tempComponent.getStartP().y;
						 width = e.getX()-selectedComponent.getStartP().x;
						 height = e.getY()-selectedComponent.getStartP().y;
						 break;
					 case D:
						 tempP.x = tempComponent.getStartP().x;
						 tempP.y = tempComponent.getStartP().y;
						 width = selectedComponent.getWidth();
						 height = e.getY()-selectedComponent.getStartP().y;
						 break;
					 case DL:
						 tempP.x = e.getX();
						 tempP.y = tempComponent.getStartP().y;
						 width = selectedComponent.getWidth()+selectedComponent.getStartP().x-e.getX();
						 height = e.getY()-selectedComponent.getStartP().y;
						 break;
					 case L:
						 tempP.x = e.getX();
						 tempP.y = tempComponent.getStartP().y;
						 width = selectedComponent.getWidth()+selectedComponent.getStartP().x-e.getX();
						 height = selectedComponent.getHeight();
						 break;
					default:
						break;
				 }
				 
				 tempComponent.setSize(tempP, width, height);
			 }
			 repaint();
		 }
	}
	
}
