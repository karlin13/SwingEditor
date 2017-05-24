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
	
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		editorPane = new EditorPanel();
		editorPane.setBounds(50, 100, 433, 283);
		contentPane.add(editorPane);
		editorPane.setLayout(null);
	}
	
	public void _newAction(){
		Utility._new(editorPane);
	}
	
	public void openAction(){
		try {
			Utility.open(this, editorPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveAction(){
		try {
			Utility.save(this, editorPane);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void saveAsAction(){
		try {
			Utility.saveAs(this, editorPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
