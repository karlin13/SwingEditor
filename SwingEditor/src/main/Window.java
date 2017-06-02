package main;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import component.MockComponent;
import util._Observable;
import util._Observer;

import javax.swing.BoxLayout;


public class Window extends JFrame implements _Observer{

	private JPanel contentPane;
	private EditorPanel editorPane;
	private AttributePanel attributePane;
	private MenuBar menuBar;
	private ToolBar toolBar;
	
	private List<_Observable> observables;
	
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(300, 300, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		editorPane = new EditorPanel();
		editorPane.setLayout(null);
		editorPane.setObserver(this);
		contentPane.add(editorPane, BorderLayout.CENTER);
		
		attributePane = new AttributePanel();
		attributePane.setObserver(this);
		contentPane.add(attributePane, BorderLayout.WEST);
		
		menuBar = new MenuBar(this, editorPane);
		setJMenuBar((JMenuBar)menuBar);
		
		toolBar = new ToolBar(this, editorPane);
		contentPane.add(toolBar,BorderLayout.NORTH);
		
		observables = new LinkedList<>();
		
		observables.add(editorPane);
		observables.add(attributePane);
	}

	@Override
	public void notifyObservables(MockComponent component) {
		for(_Observable observable:observables){
			observable.updateObservable(component);
		}
	}
}