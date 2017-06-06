package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar{
	private JMenu file;

	private JMenuItem mnItemNew;
	private JMenuItem mnItemOpen;
	private JMenuItem mnItemSave;
	private JMenuItem mnItemSaveAs;
	private JMenuItem mnItemGenCode;
	private JMenuItem mnItemClose;
	
	
	public MenuBar(Window window, EditorPanel panel){
		file = new JMenu("File");
		
		mnItemNew = new JMenuItem("new");
		mnItemOpen = new JMenuItem("open");
		mnItemSave = new JMenuItem("save");
		mnItemSaveAs = new JMenuItem("save as");
		mnItemGenCode = new JMenuItem("to java");
		mnItemClose = new JMenuItem("close");
		
		mnItemNew.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility._new(panel);
			}
		});
		mnItemOpen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.open(window, panel);
			}
		});
		mnItemSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.save(window, panel);
			}
		});
		mnItemSaveAs.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.saveAs(window, panel);
			}
		});
		mnItemGenCode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.genCode(window, panel);
			}
		});
		mnItemClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Utility.close();
			}
		});
		
		file.add(mnItemNew);
		file.add(mnItemOpen);
		file.add(mnItemSave);
		file.add(mnItemSaveAs);
		file.add(mnItemGenCode);
		file.add(mnItemClose);
		
		this.add(file);
	}
}