package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JFileChooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import component.Component;
import component.ComponentFactory;
import component.ComponentType;

//singletone
public class Utility {
	private static Utility instance = new Utility();
	private static String filePath;
	private static JFileChooser fileChooser;

	private Utility() {
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	}

	public static void _new(EditorPanel panel) {
		panel.deleteAllComponent();
	}

	public static void open(Window parent, EditorPanel panel) throws IOException, ParseException {
		// clear editor panel
		_new(panel);

		// open file dialog
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			filePath = fileChooser.getSelectedFile().getAbsolutePath();
		}

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String jsonData = br.readLine();
		br.close();

		// parse json
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonData);
		JSONArray memberArray = (JSONArray) jsonObj.get("components");

		int x, y, width, height;
		String typeString, name;

		for (int i = 0; i < memberArray.size(); i++) {
			// parse one by one
			JSONObject tempObj = (JSONObject) memberArray.get(i);

			x = Integer.parseInt(tempObj.get("x").toString());
			y = Integer.parseInt(tempObj.get("y").toString());
			width = Integer.parseInt(tempObj.get("width").toString());
			height = Integer.parseInt(tempObj.get("height").toString());
			name = tempObj.get("name").toString();
			typeString = tempObj.get("type").toString();

			ComponentType type = ComponentType.fromString(typeString);

			// create component
			Component component = ComponentFactory.createComponent(type, x, y, width, height, name);
			// add component
			panel.addComponent(component);
		}

		panel._repaint();
	}

	public static void save(Window parent, EditorPanel panel) throws IOException {
		// if file path not chosen
		if (filePath == null) {
			int result = fileChooser.showSaveDialog(parent);
			if (result == JFileChooser.APPROVE_OPTION) {
				filePath = fileChooser.getSelectedFile().getAbsolutePath();
			}
		}

		// make json data
		List<Component> list = panel.getAllComponent();

		StringBuilder jsonData = new StringBuilder("");

		jsonData.append("{\"components\":[");
		for (Component component : list) {
			jsonData.append(component.toJson());
		}
		jsonData.append("]}");

		// write json data to file
		FileWriter fw = new FileWriter(filePath);
		fw.write(jsonData.toString());
		fw.close();
	}

	public static void saveAs(Window parent, EditorPanel panel) throws IOException {
		// open file dialog
		int result = fileChooser.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			filePath = fileChooser.getSelectedFile().getAbsolutePath();
		}

		// make json data
		List<Component> list = panel.getAllComponent();

		StringBuilder jsonData = new StringBuilder("");

		jsonData.append("{\"components\":[");
		for (Component component : list) {
			jsonData.append(component.toJson());
		}
		jsonData.append("]}");

		// write json data to file
		FileWriter fw = new FileWriter(filePath);
		fw.write(jsonData.toString());
		fw.close();
	}

	public static void close() {
		System.exit(0);
	}
}
