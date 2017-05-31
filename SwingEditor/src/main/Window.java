package main;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.parser.ParseException;

import component.Component;
import component.ComponentType;
import util._Observable;
import util._Observer;

public class Window extends JFrame implements _Observer{

	private JPanel contentPane;
	private EditorPanel editorPane;
	private AttributePanel attributePane;

	private List<_Observable> observables;
	
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 1050, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		editorPane = new EditorPanel();
		editorPane.setBounds(320, 15, 680, 500);
		editorPane.setLayout(null);	
		editorPane.setObserver(this);
		contentPane.add(editorPane);
		
		attributePane = new AttributePanel();
		attributePane.setBounds(0, 10, 350, 230);
		attributePane.setLayout(null);
		attributePane.setObserver(this);
		contentPane.add(attributePane);
		
		observables  = new LinkedList<_Observable>();
		
		observables.add(editorPane);
		observables.add(attributePane);
	}

	@Override
	public void notifyObservables(Component component) {
		int size = observables.size();
		
		for(int i=0;i<size;i++)
			observables.get(i).updateObservable(component);
		
	}
	public void addComponent(Component component){
		editorPane.addComponent(component);
	}
}
