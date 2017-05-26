package main;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.parser.ParseException;

public class Window extends JFrame {

	private JPanel contentPane;
	private EditorPanel editorPane;
	private AttributePanel attributePane;

	
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 900, 600);
		setBounds(0, 0, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		editorPane = new EditorPanel();
		editorPane.setBounds(365, 15, 635, 500);
		editorPane.setBounds(0, 0, 433, 283);
		contentPane.add(editorPane);
		editorPane.setLayout(null);	
		
		attributePane = new AttributePanel();
		attributePane.setBounds(100, 100, 243, 234);
		contentPane.add(attributePane);
		attributePane.setLayout(null);
		



	}
}
