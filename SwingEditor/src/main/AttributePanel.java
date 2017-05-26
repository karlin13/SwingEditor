package main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import component.Component;
import javax.swing.JComboBox;


public class AttributePanel extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	
	private String[] ComponentType = {"NONE", "RECTANGLE"};
	private JComboBox<String> comboBox;

	public AttributePanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("x position");
		lblNewLabel.setBounds(15, 15, 150, 20);
		add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(200, 15, 150, 25);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("y position");
		lblNewLabel_1.setBounds(15, 45, 150, 20);
		add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(200, 45, 150, 25);
		add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("height");
		lblNewLabel_2.setBounds(15, 75, 150, 20);
		add(lblNewLabel_2);
		
		textField_2 = new JTextField();
		textField_2.setBounds(200, 75, 150, 25);
		add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("width");
		lblNewLabel_3.setBounds(15, 105, 150, 20);
		add(lblNewLabel_3);
		
		textField_3 = new JTextField();
		textField_3.setBounds(200, 105, 150, 25);
		add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("component type");
		lblNewLabel_4.setBounds(15, 135, 150, 20);
		add(lblNewLabel_4);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(200, 135, 150, 25);
		for(int i=0; i<ComponentType.length; i++)
			comboBox.addItem(ComponentType[i]);
		add(comboBox);
		
		JLabel lblNewLabel_5 = new JLabel("variable name");
		lblNewLabel_5.setBounds(15, 165, 150, 20);
		add(lblNewLabel_5);
		
		textField_4 = new JTextField();
		textField_4.setBounds(200, 165, 150, 25);
		add(textField_4);
		textField_4.setColumns(10);
		
		
	}		
	public void setTextField(Component component){
		textField.setText(component.getStartP().x+"");
		textField_1.setText(component.getStartP().y+"");
		textField_2.setText(component.getHeight()+"");
		textField_3.setText(component.getWidth()+"");
		comboBox.setSelectedItem(component.getType());
		textField_4.setText(component.getName());
	
	}
}
