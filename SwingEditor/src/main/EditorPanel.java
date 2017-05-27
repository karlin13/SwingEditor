package main;

import java.awt.Color;
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
import component.ComponentFactory;
import component.ComponentType;
import component.Direction;
import component.RectangleComponent;
import util._Observable;
import util._Observer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 에디터 패널
 * @author karlin
 *
 */
public class EditorPanel extends JPanel implements _Observable{
	//attributes
	/**
	 * 컴포넌트를 선택한 뒤 우클릭하면 나오는 contextMenu
	 */
	JPopupMenu contextMenu;
	
	/**
	 * 지금까지 생성된 컴포넌트들을 저장하고 있는 컨테이너입니다.
	 */
	private List<Component> components;
	/**
	 * 컴포넌트 name 만들때 사용되는 ID
	 */
	private int componentID;
	
	/**
	 * 새로운 컴포넌트를 생성할 때
	 * 마우스 드래그 할 때 보여지는 컴포넌트입니다.
	 */
	private Component tempComponent;
	/**
	 * 현재 선택된 컴포넌트입니다.
	 */
	private Component selectedComponent;
	/**
	 * 컴포넌트가 리사이징 되기 전의 정보를 저장하고 있습니다. 
	 * 
	 * 컴포넌트 리사이징 시 높이와 너비를 구하기 위해
	 * 컴포넌트가 리사이징 되기 전의 정보가 필요합니다.
	 */
	private Component unchangedComponent;
	
	private Point firstP;
	private Point lastP;
	private Point tempP;
	
	/**
	 * 컴포넌트를 선택할 때 
	 * 컴포넌트의 시작 좌표와 컴포넌트를 클릭한 좌표의 차를 저장하여 
	 * 컴포넌트 이동 시 이 정보를 이용합니다.
	 */
	private int dx, dy;//컴포넌트 이동할 때 쓰는 변수
	
	/**
	 * tempComponent를 그릴지 말지를 결정합니다.
	 */
	private boolean drawTempComponent;
	
	/**
	 * 컴포넌트의 종류
	 */
	private ComponentType type;
	
	private _Observer observer;
	
	//operations
	public EditorPanel() {
		initContextMenu();
		
		type = ComponentType.RECTANGLE;
		
		components = new LinkedList<Component>();
		componentID = 0;
		
		tempComponent = ComponentFactory.createComponent(type, "tempComponent");
		selectedComponent = null;
		unchangedComponent = ComponentFactory.createComponent(type, "unchanged");
		
		firstP = new Point();
		lastP = new Point();
		tempP = new Point();
		
		addMouseListener(new EditorMouseAdapter());
		addMouseMotionListener(new EditorMouseMotionAdapter());
		
		//배경색 흰색으로 설정
		setBackground(Color.WHITE);
	}
	/**
	 * contextMenu를 초기화합니다
	 */
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
	
	public List<Component> getAllComponent(){
		return components;
	}
	
	private void deleteComponentItemAction(){
		if(selectedComponent != null){
			deleteComponent();
		}
		repaint();
	}
	/**
	 * 새로운 컴포넌트를 추가합니다
	 * @param component
	 */
	public void addComponent(Component component){
		componentID = (componentID+1)%Integer.MAX_VALUE;
		System.out.println(component.getName());
		components.add(component);
	}
	/**
	 * 현재 선택된 컴포넌트를 삭제합니다
	 */
	private void deleteComponent(){
		components.remove(selectedComponent);
		selectedComponent = null;
	}
	/**
	 * 현재까지 그려진 모든 컴포넌트를 삭제합니다.
	 */
	public void deleteAllComponent(){
		selectedComponent = null;
		
		int size = components.size();
		for(int i=0;i<size;i++)
			components.remove(0);
	}
	/**
	 * 컴포넌트의 name 필드는 type + ID입니다 (ex. RECTANGLE1, RECTANGLE7...)
	 * 새로운 컴포넌트를 생성할 때 그 컴포넌트의 ID를 반환하는 메소드입니다. 
	 * @return componentID
	 */
	public int getNextComponentID(){
		return componentID;
	}
	/**
	 * EditorPanel의 repaint() 메소드를 외부에서도 호출해야 할 때가 있어서 만든 메소드입니다
	 * repaint()메소드를 호출합니다.
	 */
	public void _repaint(){
		repaint();
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
		if(drawTempComponent){
			tempComponent.draw(g);
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
				
				notifyObserver();
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
				drawTempComponent = false;
				
				//너비와 높이가 최소 너비, 높이보다 클 때만 새로운 컴포넌트를 생성한다
				if(Math.abs(firstP.x-lastP.x) > Component.MIN_WIDTH && Math.abs(firstP.y-lastP.y) > Component.MIN_HEIGHT){
					Component newComponent;
					
					String name = type.toString() + getNextComponentID();
					
					//새 컴포넌트를 리스트에 추가한다
					newComponent = new RectangleComponent(name);
					newComponent.setSize(firstP , lastP);
					
					addComponent(newComponent);
				}
			}
			
			notifyObserver();
			//에디터 패널 갱신
			repaint();
		}
	}
	class EditorMouseMotionAdapter extends MouseMotionAdapter{
		 public void mouseDragged(MouseEvent e){ 
			if(selectedComponent == null){
				 drawTempComponent = true;
				 
				 tempP.x = e.getX();
				 tempP.y = e.getY();
				 
				 tempComponent.setSize(firstP, tempP);
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
				 
				 width = Math.max(width, Component.MIN_WIDTH);
				 height = Math.max(height, Component.MIN_HEIGHT);
				 
				 selectedComponent.setSize(tempP, width, height);
			 }
			 
			 notifyObserver();
			 repaint();
		 }
	}
	
	public void setObserver(_Observer observer){
		this.observer = observer;
	}
	@Override
	public void notifyObserver() {
		observer.notifyObservables(selectedComponent);
	}
	@Override
	public void updateObservable(Component component) {
		if(selectedComponent != null){
			selectedComponent.setSize(component.getStartP(), component.getWidth(), component.getHeight());
			selectedComponent.setName(component.getName());
		}
		
		_repaint();
	}
	
}
