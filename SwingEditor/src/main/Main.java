package main;

import java.awt.EventQueue;
import java.awt.Point;
import component.Component;
import component.RectangleComponent;

public class Main{
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();

					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
