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
	public Dimension getPreferredSize(){
		return new Dimension(200,200);
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
				unchangedComp.setSizeNLocation(selectedComp.getLocation(), selectedComp.getWidth(), selectedComp.getHeight());
			}
		}
		public void mouseReleased(MouseEvent e){
			if(compSelected == false){
				//set lastP
				lastP.x = e.getX();
				lastP.y = e.getY();
				
				//TODO: create class that manages variable names
				newComp.setSizeNLocation(firstP, lastP);
				
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
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		repaint();
		
		if(compSelected == true)
			selectedComp.drawResizeHelper(g);
	}
	
	class EditorMouseMotionAdapter extends MouseMotionAdapter{
		 public void mouseDragged(MouseEvent e){
			if(compSelected == false){
				tempP.x = e.getX();
				tempP.y = e.getY();
				
				newComp.setSizeNLocation(firstP, tempP);
			}
			else{
				int width, height;
				
				width = selectedComp.getWidth();
				height = selectedComp.getHeight();
				
				switch(dir){
					case NONE:
						width = selectedComp.getWidth();
						height = selectedComp.getHeight();
						tempP.x = e.getX() + selectedComp.getDx();
						tempP.y = e.getY() + selectedComp.getDy();
						break;
					case UL:
						 width = unchangedComp.getWidth()+unchangedComp.getX()-e.getX();
						 height = unchangedComp.getHeight()+unchangedComp.getY()-e.getY();
						 tempP.x = (width<MockComponent.MIN_WIDTH)?selectedComp.getX():e.getX();
						 tempP.y = (height<MockComponent.MIN_HEIGHT)?selectedComp.getY():e.getY();
						 break;
					 case U:
						 width = selectedComp.getWidth();
						 height = unchangedComp.getHeight()+unchangedComp.getY()-e.getY();
						 tempP.x = selectedComp.getX();
						 tempP.y = (height<MockComponent.MIN_HEIGHT)?selectedComp.getY():e.getY();
						 break;
					 case UR:
						 width = e.getX()-unchangedComp.getX();
						 height = unchangedComp.getHeight()+unchangedComp.getY()-e.getY();
						 tempP.x = selectedComp.getX();
						 tempP.y = (height<MockComponent.MIN_HEIGHT)?selectedComp.getY():e.getY();
						 break;
					 case R:
						 width = e.getX()-unchangedComp.getX();
						 height = selectedComp.getHeight();
						 tempP.x = selectedComp.getX();
						 tempP.y = selectedComp.getY();
						 break;
					 case DR:
						 width = e.getX()-unchangedComp.getX();
						 height = e.getY()-unchangedComp.getY();
						 tempP.x = selectedComp.getX();
						 tempP.y = selectedComp.getY();
						 break;
					 case D:
						 width = selectedComp.getWidth();
						 height = e.getY()-selectedComp.getY();
						 tempP.x = selectedComp.getX();
						 tempP.y = selectedComp.getY();
						 break;
					 case DL:
						 width = unchangedComp.getWidth()+unchangedComp.getX()-e.getX();
						 height = e.getY()-selectedComp.getY();
						 tempP.x = (width<MockComponent.MIN_WIDTH)?selectedComp.getX():e.getX();
						 tempP.y = selectedComp.getY();
						 break;
					 case L:
						 width = unchangedComp.getWidth()+unchangedComp.getX()-e.getX();
						 height = selectedComp.getHeight();
						 tempP.x = (width<MockComponent.MIN_WIDTH)?selectedComp.getX():e.getX();
						 tempP.y = selectedComp.getY();
						 break;
					default:
						break;
				}
				
				selectedComp.setSizeNLocation(tempP, width, height);
			}
		}
	}

	public void setObserver(_Observer observer){
		this.observer = observer;
	}
	@Override
	public void notifyObserver() {
		if(selectedComp != null){
			dummyComp.setSizeNLocation(selectedComp.getLocation(), selectedComp.getWidth(), selectedComp.getHeight());
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
			selectedComp.setSizeNLocation(component.getLocation(), component.getWidth(), component.getHeight());
			selectedComp.setText(component.getText());
			selectedComp.setType(component.getType());
			selectedComp.setVariableName(component.getVariableName());
		}
	}
}