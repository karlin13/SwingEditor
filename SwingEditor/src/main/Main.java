package main;

import java.awt.EventQueue;

public class Main{
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Window frame = new Window();
					
					//frame.getContentPane().add(toolBar, BorderLayout.NORTH);
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}