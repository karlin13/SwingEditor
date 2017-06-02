package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

public class ToolBar extends JToolBar{
	private JButton btnNew;
	private JButton btnOpen;
	private JButton btnSave;
	private JButton btnSaveAs;
	private JButton btnGenCode;
	private JButton btnClose;
	
	private String btnNewPath = "NEW";
	private String btnOpenPath = "OPEN";
	private String btnSavePath = "SAVE";
	private String btnSaveAsPath = "SAVEAS";
	private String btnToJavaPath = "TOJAVA";
	private String btnClosePath = "CLOSE";
	
	public ToolBar(Window window, EditorPanel panel){
		btnNew = new JButton(btnNewPath);
		btnOpen = new JButton(btnOpenPath);
		btnSave = new JButton(btnSavePath);
		btnSaveAs = new JButton(btnSaveAsPath);
		btnGenCode = new JButton(btnToJavaPath);
		btnClose = new JButton(btnClosePath);
		
		btnNew.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility._new(panel);
			}
		});
		btnOpen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.open(window, panel);
			}
		});
		btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.save(window, panel);
			}
		});
		btnSaveAs.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.saveAs(window, panel);
			}
		});
		btnGenCode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.genCode(window, panel);
			}
		});
		btnClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.close();
			}
		});
		
		add(btnNew);
		add(btnOpen);
		add(btnSave);
		add(btnSaveAs);
		add(btnGenCode);
		add(btnClose);
	}
}