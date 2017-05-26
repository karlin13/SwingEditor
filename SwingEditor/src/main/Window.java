package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Window extends JFrame {

	private JPanel contentPane;
	private EditorPanel editorPane;
	private AttributePanel attributePane;

	
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		editorPane = new EditorPanel();
		editorPane.setBounds(365, 15, 635, 500);
		contentPane.add(editorPane);
		editorPane.setLayout(null);	
		
		attributePane = new AttributePanel();
		attributePane.setBounds(100, 100, 243, 234);
		contentPane.add(attributePane);
		attributePane.setLayout(null);
		



	}

}
