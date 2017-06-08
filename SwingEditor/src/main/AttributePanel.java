package main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import component.ComponentType;
import component.MockComponent;
import util._Observable;
import util._Observer;

import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import java.awt.GridLayout;


public class AttributePanel extends JPanel implements _Observable{
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblWidth;
	private JLabel lblHeight;
	private JLabel lblText;
	private JLabel lblType;
	private JLabel lblVariableName;
	
	private JTextField txtFldX;
	private JTextField txtFldY;
	private JTextField txtFldWidth;
	private JTextField txtFldHeight;
	private JTextField txtFldText;
	private JTextField txtFldVariableName;
	
	private JComboBox<ComponentType> cmbBxType;
	
	private _Observer observer;
	
	private MockComponent dummyComp;
	private Point dummyP;
	
	public AttributePanel() {
		dummyComp = new MockComponent("dummy");
		dummyP = new Point();
		
		setLayout(new GridLayout(0, 2, 0, 0));
		
		lblX = new JLabel("x position");
		add(lblX);
		
		txtFldX = new JTextField();
		add(txtFldX);
		
		lblY = new JLabel("y position");
		add(lblY);
		
		txtFldY = new JTextField();
		add(txtFldY);
		
		lblWidth = new JLabel("width");
		add(lblWidth);
		
		txtFldWidth = new JTextField();
		add(txtFldWidth);
		
		lblHeight = new JLabel("height");
		add(lblHeight);
		
		txtFldHeight = new JTextField();
		add(txtFldHeight);
		
		lblText = new JLabel("text");
		add(lblText);
		
		txtFldText = new JTextField();
		add(txtFldText);
		
		lblType = new JLabel("type");
		add(lblType);
		
		cmbBxType = new JComboBox<ComponentType>(ComponentType.values());
		add(cmbBxType);
		
		lblVariableName = new JLabel("variable name");
		add(lblVariableName);
		
		txtFldVariableName = new JTextField();
		add(txtFldVariableName);
		
		//add key listener
		KeyAdapter keyAdapter = new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					notifyObserver();
			}
		};
		
		txtFldX.addKeyListener(keyAdapter);
		txtFldY.addKeyListener(keyAdapter);
		txtFldWidth.addKeyListener(keyAdapter);
		txtFldHeight.addKeyListener(keyAdapter);
		txtFldText.addKeyListener(keyAdapter);
		txtFldVariableName.addKeyListener(keyAdapter);
		
		cmbBxType.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED && e.getItem() != ComponentType.NONE)
					notifyObserver();
			}});
	}
	
	private void setAttribute(MockComponent component){
		txtFldX.setText(component.getX()+"");
		txtFldY.setText(component.getY()+"");
		txtFldWidth.setText(component.getWidth()+"");
		txtFldHeight.setText(component.getHeight()+"");
		txtFldText.setText(component.getText());
		txtFldVariableName.setText(component.getVariableName());
		cmbBxType.setSelectedItem(component.getType());
	}
	private void emptyAttribute(){
		txtFldX.setText("");
		txtFldY.setText("");
		txtFldWidth.setText("");
		txtFldHeight.setText("");
		txtFldText.setText("");
		txtFldVariableName.setText("");
		cmbBxType.setSelectedItem(ComponentType.NONE);
	}
	
	public void setObserver(_Observer observer){
		this.observer = observer;
	}
	@Override
	public void notifyObserver() {
		try{
			dummyP.x = Integer.parseInt(txtFldX.getText());
			dummyP.y = Integer.parseInt(txtFldY.getText());
			
			//set dummyComp
			// 깔끔한 구조가 생각안나서 그냥 하드코딩 했습니다
			//TODO: 코드 개선
			dummyComp.setSizeNLocation(dummyP, Integer.parseInt(txtFldWidth.getText()), Integer.parseInt(txtFldHeight.getText()), 0, 0, 390, 338);
			dummyComp.setText(txtFldText.getText());
			dummyComp.setType(ComponentType.fromString(cmbBxType.getSelectedItem().toString()));
			dummyComp.setVariableName(txtFldVariableName.getText());
			
			this.observer.notifyObservables(dummyComp);
		}
		catch(NumberFormatException e){
			
		}
	}

	@Override
	public void updateObservable(MockComponent component) {
		if(component != null)
			setAttribute(component);
		else
			emptyAttribute();
	}
}