package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

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
	
	private Vector<Component> components;
	private Component tempComponent;
	private Component selectedComponent;

	private Point firstP;
	private Point lastP;
	private Point tempP;
	
	private int dx, dy;//컴포넌트 이동할 때 쓰는 변수
	
	private boolean drawTemp;
	
	private int index;
	
	//operations
	public EditorPanel() {
		initContextMenu();
		
		components = new Vector<Component>();
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
		components.add(index, component);
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
				//이전 selectedComponent의 색을 원래대로 돌려노흔다
				if(selectedComponent != null)
					selectedComponent.setDefaultColor();
				
				//아무 컴포넌트도 선택되지 않았으면 null
				selectedComponent = null;
				
				for(Component component:components){
					if(component.selected(e.getX(), e.getY())){
						selectedComponent = component;
						selectedComponent.setHighlightColor();
						
						/* resizeHelper를 보여줄 때 tempComponent의 것을 보여주므로 selectedComponent와 tempComponent르 동기화 해야함*/
						Point startP = selectedComponent.getStartP();
						int width = selectedComponent.getWidth();
						int height = selectedComponent.getHeight();
						
						tempComponent.setSize(startP, width, height);
						/******************************************************************************/
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
				index = components.indexOf(selectedComponent);
		
				//components에서 selectedComponent제거
				components.remove(selectedComponent);
		
				Point startP = selectedComponent.getStartP();
				int width = selectedComponent.getWidth();
				int height = selectedComponent.getHeight();
				
				tempComponent.setSize(startP, width, height);
				tempComponent.setHighlightColor();
				
				dx = selectedComponent.getStartP().x-firstP.x;
				dy = selectedComponent.getStartP().y-firstP.y;
			}
			else{
				tempComponent.setDefaultColor();
			}
		}
		public void mouseReleased(MouseEvent e){	
			lastP.x = e.getX();
			lastP.y = e.getY();
			
			if(selectedComponent == null){
				Component newComponent;
				
				//새 컴포넌트를 리스트에 추가한다
				newComponent = new RectangleComponent();
				newComponent.setSize(firstP , lastP);
				
				addComponent(newComponent);
			}
			else{
				Point startP = tempComponent.getStartP();
				int width = tempComponent.getWidth();
				int height = tempComponent.getHeight();
				
				selectedComponent.setSize(startP, width, height);
				addComponent(selectedComponent);
			}
			
			//에디터 패널 갱신
			repaint();
			
			drawTemp = false;
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
				 
				 width = Math.max(width, 12);
				 height = Math.max(height, 12);
				 
				 tempComponent.setSize(tempP, width, height);
			 }
			 repaint();
		 }
	}
	
}
