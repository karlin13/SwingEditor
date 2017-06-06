package main;

import java.awt.Color;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import component.Direction;
import component.MockComponent;
import util._Observable;
import util._Observer;

public class EditorPanel extends JPanel  implements _Observable{
	//attributes
	private JPopupMenu contextMenu;
	
	private Point firstP;
	private Point lastP;
	private Point tempP;
	
	private MockComponent newComp;
	private MockComponent selectedComp;
	private MockComponent unchangedComp;
	private MockComponent dummyComp;
	
	private boolean compSelected;
	private Direction dir;
	
	private _Observer observer;
	
	private int MIN_X, MIN_Y, MAX_X, MAX_Y;
	//operations
	public EditorPanel() {
		initContextMenu();
		
		firstP = new Point();
		lastP = new Point();
		tempP = new Point();
		
		unchangedComp = new MockComponent("unchanged");
		dummyComp = new MockComponent("dummy");
		
		addMouseListener(new EditorMouseAdapter());
		addMouseMotionListener((MouseMotionListener) new EditorMouseMotionAdapter());

		//배경색 흰색으로 설정
		setBackground(Color.WHITE);
		
		//set min (x,y), max(x,y)
		MIN_X = 0;
		MIN_Y = 0;
		MAX_X = this.getPreferredSize().width;
		MAX_Y = this.getPreferredSize().height;
	}
	
	public void update(){
		revalidate();
		repaint();
	}
	public void _add(MockComponent component){
		add(component);
		update();
	}
	public void _remove(MockComponent component){
		compSelected = false;
		selectedComp = null;
		remove(component);
		update();
	}
	public void _removeAll(){
		compSelected = false;
		selectedComp = null;
		notifyObserver();
		removeAll();
		update();
	}
	private  Component[]_getComponents(){
		return this.getComponents();
	}
	
	private void initContextMenu(){
		contextMenu = new JPopupMenu();
		JMenuItem deleteComponentMenu = new JMenuItem("delete");
		
		deleteComponentMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_remove(selectedComp);
				notifyObserver();
			}
        });
		
		contextMenu.add(deleteComponentMenu);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		repaint();
		
		if(compSelected == true)
			selectedComp.drawResizeHelper(g);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(390,338);
	}
	class EditorMouseAdapter extends MouseAdapter{
		public void mousePressed(MouseEvent e){
			if(compSelected == false){
				//set firstP
				firstP.x = e.getX();
				firstP.y = e.getY();
				
				newComp = new MockComponent("hi");
				_add(newComp);
			}
			else{
				dir = selectedComp.getResizeHelperDirection(e.getX(), e.getY());
				unchangedComp.setSizeNLocation(selectedComp.getLocation(), selectedComp.getWidth(), selectedComp.getHeight(), MIN_X, MIN_Y, MAX_X, MAX_Y);
			}
		}
		public void mouseReleased(MouseEvent e){
			if(compSelected == false){
				int EX = e.getX();
				int EY = e.getY();

				EX = (EX<MIN_X)?MIN_X:EX;
				EX = (EX>MAX_X)?MAX_X:EX;
				EY = (EY<MIN_Y)?MIN_Y:EY;
				EY = (EY>MAX_Y)?MAX_Y:EY;
				
				//set lastP
				lastP.x = EX;
				lastP.y = EY;
				
				//TODO: create class that manages variable names
				newComp.setSizeNLocation(firstP, lastP, MIN_X, MIN_Y, MAX_X, MAX_Y);
				
				if(Math.abs(firstP.x-lastP.x)<MockComponent.MIN_WIDTH-1 || Math.abs(firstP.y-lastP.y)<MockComponent.MIN_HEIGHT-1)
					_remove(newComp);
			}
			else{
				dir = Direction.NONE;
				notifyObserver();
			}
		}
		public void mouseClicked(MouseEvent e){
			if(e.getButton() == MouseEvent.BUTTON1){
				//unhighlight all components
				Component[] components = _getComponents();
				for(Component component:components){
					MockComponent mock = (MockComponent)component;
					mock.unselect();
				}
				
				//select component
				Object source = e.getSource();
				
				if(source instanceof EditorPanel){
					compSelected = false;
					selectedComp = null;
				}
				else if(e.getSource() instanceof MockComponent){
					MockComponent mock = (MockComponent)e.getSource();
					mock.setDiff(mock.getX(), mock.getY(), e.getX(), e.getY());
					mock.select();
					
					selectedComp = mock;
					compSelected = true;
				}
				
				notifyObserver();
			}
			else if(e.getButton() == MouseEvent.BUTTON3 && selectedComp != null){
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
	class EditorMouseMotionAdapter extends MouseMotionAdapter{
		 public void mouseDragged(MouseEvent e){
			int EX = e.getX();
			int EY = e.getY();

			EX = (EX<MIN_X)?MIN_X:EX;
			EX = (EX>MAX_X)?MAX_X:EX;
			EY = (EY<MIN_Y)?MIN_Y:EY;
			EY = (EY>MAX_Y)?MAX_Y:EY;
				
			if(compSelected == false){
				tempP.x = EX;
				tempP.y = EY;
				
				newComp.setSizeNLocation(firstP, tempP, MIN_X, MIN_Y, MAX_X, MAX_Y);
			}
			else{
				int width, height;
				
				width = selectedComp.getWidth();
				height = selectedComp.getHeight();
				
				switch(dir){
					case NONE:
						width = selectedComp.getWidth();
						height = selectedComp.getHeight();
						tempP.x = EX + selectedComp.getDx();
						tempP.y = EY + selectedComp.getDy();
						break;
					case UL:
						 width = unchangedComp.getWidth()+unchangedComp.getX()-EX;
						 height = unchangedComp.getHeight()+unchangedComp.getY()-EY;
						 tempP.x = (width<MockComponent.MIN_WIDTH)?selectedComp.getX():EX;
						 tempP.y = (height<MockComponent.MIN_HEIGHT)?selectedComp.getY():EY;
						 break;
					 case U:
						 width = selectedComp.getWidth();
						 height = unchangedComp.getHeight()+unchangedComp.getY()-EY;
						 tempP.x = selectedComp.getX();
						 tempP.y = (height<MockComponent.MIN_HEIGHT)?selectedComp.getY():EY;
						 break;
					 case UR:
						 width = EX-unchangedComp.getX();
						 height = unchangedComp.getHeight()+unchangedComp.getY()-EY;
						 tempP.x = selectedComp.getX();
						 tempP.y = (height<MockComponent.MIN_HEIGHT)?selectedComp.getY():EY;
						 break;
					 case R:
						 width = EX-unchangedComp.getX();
						 height = selectedComp.getHeight();
						 tempP.x = selectedComp.getX();
						 tempP.y = selectedComp.getY();
						 break;
					 case DR:
						 width = EX-unchangedComp.getX();
						 height = EY-unchangedComp.getY();
						 tempP.x = selectedComp.getX();
						 tempP.y = selectedComp.getY();
						 break;
					 case D:
						 width = selectedComp.getWidth();
						 height = EY-selectedComp.getY();
						 tempP.x = selectedComp.getX();
						 tempP.y = selectedComp.getY();
						 break;
					 case DL:
						 width = unchangedComp.getWidth()+unchangedComp.getX()-EX;
						 height = EY-selectedComp.getY();
						 tempP.x = (width<MockComponent.MIN_WIDTH)?selectedComp.getX():EX;
						 tempP.y = selectedComp.getY();
						 break;
					 case L:
						 width = unchangedComp.getWidth()+unchangedComp.getX()-EX;
						 height = selectedComp.getHeight();
						 tempP.x = (width<MockComponent.MIN_WIDTH)?selectedComp.getX():EX;
						 tempP.y = selectedComp.getY();
						 break;
					default:
						break;
				}
				
				selectedComp.setSizeNLocation(tempP, width, height, MIN_X, MIN_Y, MAX_X, MAX_Y);
			}
		}
	}

	public void setObserver(_Observer observer){
		this.observer = observer;
	}
	@Override
	public void notifyObserver() {
		if(selectedComp != null){
			dummyComp.setSizeNLocation(selectedComp.getLocation(), selectedComp.getWidth(), selectedComp.getHeight(), MIN_X, MIN_Y, MAX_X, MAX_Y);
			dummyComp.setText(selectedComp.getText());
			dummyComp.setType(selectedComp.getType());
			dummyComp.setVariableName(selectedComp.getVariableName());
			
			this.observer.notifyObservables(dummyComp);
		}
		else{
			this.observer.notifyObservables(null);
		}
	}
	@Override
	public void updateObservable(MockComponent component) {
		if(selectedComp != null){
			selectedComp.setSizeNLocation(component.getLocation(), component.getWidth(), component.getHeight(), MIN_X, MIN_Y, MAX_X, MAX_Y);
			selectedComp.setText(component.getText());
			selectedComp.setType(component.getType());
			selectedComp.setVariableName(component.getVariableName());
		}
	}
}