package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;

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
	
	private List<Component> components;
	private Component newComponent;
	private Component selectedComponent;
	private Component unchangedComponent;
	
	private Point firstP;
	private Point lastP;
	private Point tempP;
	
	private int dx, dy;//컴포넌트 이동할 때 쓰는 변수
	
	private boolean drawNewComponent;
	
	//operations
	public EditorPanel() {
		initContextMenu();
		
		components = new LinkedList<Component>();
		newComponent = new RectangleComponent();
		selectedComponent = null;
		unchangedComponent = new RectangleComponent();
		
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
		if(drawNewComponent){
			newComponent.draw(g);
		}
		//선택된 컴포넌트 resizeHelper 표시
		if(selectedComponent != null){
			selectedComponent.drawResizeHelper(g);
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
				dx = selectedComponent.getStartP().x-firstP.x;
				dy = selectedComponent.getStartP().y-firstP.y;
				
				Point startP = selectedComponent.getStartP();
				int width = selectedComponent.getWidth();
				int height = selectedComponent.getHeight();
				
				unchangedComponent.setSize(startP, width, height);
			}
		}
		public void mouseReleased(MouseEvent e){
			lastP.x = e.getX();
			lastP.y = e.getY();
			
			if(selectedComponent == null){
				drawNewComponent = false;
				
				Component newComponent;
				
				//새 컴포넌트를 리스트에 추가한다
				newComponent = new RectangleComponent();
				newComponent.setSize(firstP , lastP);
				
				addComponent(newComponent);
			}
			
			//에디터 패널 갱신
			repaint();
		}
	}
	class EditorMouseMotionAdapter extends MouseMotionAdapter{
		 public void mouseDragged(MouseEvent e){ 
			if(selectedComponent == null){
				 drawNewComponent = true;
				 
				 tempP.x = e.getX();
				 tempP.y = e.getY();
				 
				 newComponent.setSize(firstP, tempP);
			 }
			 else{
				 Direction dir = selectedComponent.getResizeHelperDirection(e.getX(), e.getY());
				 
				 int width=selectedComponent.getWidth();
				 int height=selectedComponent.getHeight();
				 
				 switch(dir){
					 case NONE:
						 tempP.x = e.getX()+dx;
						 tempP.y = e.getY()+dy;
						 width = selectedComponent.getWidth();
						 height = selectedComponent.getHeight();
						 break;
					 case UL:
						 tempP.x = e.getX();
						 tempP.y = e.getY();
						 width = unchangedComponent.getWidth()+unchangedComponent.getStartP().x-e.getX();
						 height = unchangedComponent.getHeight()+unchangedComponent.getStartP().y-e.getY();
						 break;
					 case U:
						 tempP.x = selectedComponent.getStartP().x;
						 tempP.y = e.getY();
						 width = selectedComponent.getWidth();
						 height = unchangedComponent.getHeight()+unchangedComponent.getStartP().y-e.getY();
						 break;
					 case UR:
						 tempP.x = selectedComponent.getStartP().x;
						 tempP.y = e.getY();
						 width = e.getX()-unchangedComponent.getStartP().x;
						 height = unchangedComponent.getHeight()+unchangedComponent.getStartP().y-e.getY();
						 break;
					 case R:
						 tempP.x = selectedComponent.getStartP().x;
						 tempP.y = selectedComponent.getStartP().y;
						 width = e.getX()-unchangedComponent.getStartP().x;
						 height = selectedComponent.getHeight();
						 break;
					 case DR:
						 tempP.x = selectedComponent.getStartP().x;
						 tempP.y = selectedComponent.getStartP().y;
						 width = e.getX()-unchangedComponent.getStartP().x;
						 height = e.getY()-unchangedComponent.getStartP().y;
						 break;
					 case D:
						 tempP.x = selectedComponent.getStartP().x;
						 tempP.y = selectedComponent.getStartP().y;
						 width = selectedComponent.getWidth();
						 height = e.getY()-selectedComponent.getStartP().y;
						 break;
					 case DL:
						 tempP.x = e.getX();
						 tempP.y = selectedComponent.getStartP().y;
						 width = unchangedComponent.getWidth()+unchangedComponent.getStartP().x-e.getX();
						 height = e.getY()-selectedComponent.getStartP().y;
						 break;
					 case L:
						 tempP.x = e.getX();
						 tempP.y = selectedComponent.getStartP().y;
						 width = unchangedComponent.getWidth()+unchangedComponent.getStartP().x-e.getX();
						 height = selectedComponent.getHeight();
						 break;
					default:
						break;
				 }
				 
				 width = Math.max(width, 12);
				 height = Math.max(height, 12);
				 
				 selectedComponent.setSize(tempP, width, height);
			 }
			 repaint();
		 }
	}
	
}
