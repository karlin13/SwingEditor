package main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import component.Component;
import component.ComponentFactory;
import component.ComponentType;
import util._Observable;
import util._Observer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;


public class AttributePanel extends JPanel implements _Observable{
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	
	private String[] componentType = {"NONE", "RECTANGLE"};
	private JComboBox<String> comboBox;
	
	private _Observer observer;
	
	public AttributePanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("x position");
		lblNewLabel.setBounds(15, 15, 150, 20);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(150, 15, 150, 25);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("y position");
		lblNewLabel_1.setBounds(15, 45, 150, 20);
		add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(150, 45, 150, 25);
		add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("height");
		lblNewLabel_2.setBounds(15, 75, 150, 20);
		add(lblNewLabel_2);
		
		textField_2 = new JTextField();
		textField_2.setBounds(150, 75, 150, 25);
		add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("width");
		lblNewLabel_3.setBounds(15, 105, 150, 20);
		add(lblNewLabel_3);
		
		textField_3 = new JTextField();
		textField_3.setBounds(150, 105, 150, 25);
		add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("component type");
		lblNewLabel_4.setBounds(15, 135, 150, 20);
		add(lblNewLabel_4);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(200, 135, 150, 25);
		for(int i=0; i<componentType.length; i++)
			comboBox.addItem(componentType[i]);
		add(comboBox);
		
		JLabel lblNewLabel_5 = new JLabel("variable name");
		lblNewLabel_5.setBounds(15, 165, 150, 20);
		add(lblNewLabel_5);
		
		textField_4 = new JTextField();
		textField_4.setBounds(150, 165, 150, 25);
		add(textField_4);
		textField_4.setColumns(10);	
		
		// 이벤트 핸들러 추가
		// 값을 변경하고 엔터 누르면 컴포넌트에 반영된다
		KeyAdapter keyAdapter = new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				notifyObserver();
			}
		};
		
		textField.addKeyListener(keyAdapter);
		textField_1.addKeyListener(keyAdapter);
		textField_2.addKeyListener(keyAdapter);
		textField_3.addKeyListener(keyAdapter);
		textField_4.addKeyListener(keyAdapter);
	}
	
	public void setTextField(Component component){
		textField.setText(component.getStartP().x+"");
		textField_1.setText(component.getStartP().y+"");
		textField_2.setText(component.getHeight()+"");
		textField_3.setText(component.getWidth()+"");
		comboBox.setSelectedItem(component.getType().toString());
		textField_4.setText(component.getName());
	
	}
	public void emptyTextField(){
		textField.setText("");
		textField_1.setText("");
		textField_2.setText("");
		textField_3.setText("");
		comboBox.setSelectedItem(ComponentType.NONE.toString());
		textField_4.setText("");
	}
	
	public void setObserver(_Observer observer){
		this.observer = observer;
	}
	@Override
	public void notifyObserver() {
		// 어느 값 중 하나가 잘못된 형식이면 에러낸다
		try{
			int x = Integer.parseInt(textField.getText());
			int y = Integer.parseInt(textField_1.getText());
			int width = Integer.parseInt(textField_3.getText());
			int height = Integer.parseInt(textField_2.getText());
			ComponentType type = ComponentType.RECTANGLE;//fromString(comboBox.getSelectedItem().toString());
			String name = textField_4.getText();

			Component dummyComponent = ComponentFactory.createComponent(type, x, y, width, height, name);
			System.out.println(dummyComponent.toJson());
			observer.notifyObservables(dummyComponent);
		}
		catch(NumberFormatException exp){
			//TODO: 텍스트 필드를 빨간색으로 표시
		}
	}
	@Override
	public void updateObservable(Component component) {
		if(component != null){
			setTextField(component);
		}
		else{
			emptyTextField();
		}
	}

	
}
