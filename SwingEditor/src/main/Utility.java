package main;

import java.awt.Component;
import java.awt.Point;
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

import component.ComponentType;
import component.MockComponent;

public class Utility {
	//an instance for calling Utility constructor, this variable is never used
	private static Utility instance = new Utility();
	
	private static String jsonFilePath;
	private static String javaFilePath;
	private static JFileChooser fileChooser;

	private Utility() {
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	}

	public static void _new(EditorPanel panel) {
		panel._removeAll();
		panel.repaint();
	}

	public static void open(Window parent, EditorPanel panel){
		// clear editor panel
		_new(panel);

		// open file dialog
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			jsonFilePath = fileChooser.getSelectedFile().getAbsolutePath();
		}

		try {
			BufferedReader br;
			br = new BufferedReader(new FileReader(jsonFilePath));
			String jsonData = br.readLine();
			br.close();
			

			// parse json
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonData);
			JSONArray memberArray = (JSONArray) jsonObj.get("components");

			int x, y, width, height;
			String typeString, text, variableName;

			for (int i = 0; i < memberArray.size(); i++) {
				// parse one by one
				JSONObject tempObj = (JSONObject) memberArray.get(i);

				x = Integer.parseInt(tempObj.get("x").toString());
				y = Integer.parseInt(tempObj.get("y").toString());
				width = Integer.parseInt(tempObj.get("width").toString());
				height = Integer.parseInt(tempObj.get("height").toString());
				text = tempObj.get("text").toString();
				variableName = tempObj.get("variableName").toString();
				typeString = tempObj.get("type").toString();
				
				ComponentType type = ComponentType.fromString(typeString);

				// create component
				MockComponent component = new MockComponent(text);
				component.setSizeNLocation(new Point(x, y), width, height);
				component.setText(text);
				component.setType(type);
				component.setVariableName(variableName);
				// add component
				panel._add(component);
			}

			panel.repaint();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void save(Window parent, EditorPanel panel){
		// if file path not chosen
		if (jsonFilePath == null) {
			int result = fileChooser.showSaveDialog(parent);
			if (result == JFileChooser.APPROVE_OPTION) {
				jsonFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			}
		}

		// make json data
		Component[] components = panel.getComponents();

		StringBuilder jsonData = new StringBuilder("");
		//opening bracket 추가
		jsonData.append("{\"components\":[");
		//components to json
		for (Component component : components) {
			MockComponent mock = (MockComponent)component;
			jsonData.append(mock.toJson()+",");
		}
		// 마지막 , 지운다
		int lastIndex = jsonData.length()-1;
		jsonData.deleteCharAt(lastIndex);
		//closing bracket 추가
		jsonData.append("]}");

		// write json data to file
		try {
			FileWriter fw = new FileWriter(jsonFilePath);
			fw.write(jsonData.toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveAs(Window parent, EditorPanel panel) {
		// open file dialog
		int result = fileChooser.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			jsonFilePath = fileChooser.getSelectedFile().getAbsolutePath();
		}

		// make json data
		Component[] components = panel.getComponents();

		StringBuilder jsonData = new StringBuilder("");

		jsonData.append("{\"components\":[");
		for (Component component : components) {
			MockComponent mock = (MockComponent)component;
			jsonData.append(mock.toJson());
		}
		jsonData.append("]}");

		// write json data to file
		try {
			FileWriter fw = new FileWriter(jsonFilePath);
			fw.write(jsonData.toString());
			fw.close();
		} catch (IOException e) {
			//TODO: show message dialog
		}
	}
	public static void genCode(Window parent, EditorPanel panel){
		// open file dialog
		int result = fileChooser.showSaveDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			javaFilePath = fileChooser.getSelectedFile().getAbsolutePath();
		}
		
		String firstHalf = "import java.awt.BorderLayout;\nimport java.awt.EventQueue;\n\nimport javax.swing.JFrame;\nimport javax.swing.JPanel;\nimport javax.swing.border.EmptyBorder;\n\npublic class test extends JFrame {\n\n\tprivate JPanel contentPane;\n\n\t/**\n\t * Launch the application.\n\t */\n\tpublic static void main(String[] args) {\n\t\tEventQueue.invokeLater(new Runnable() {\n\t\t\tpublic void run() {\n\t\t\t\ttry {\n\t\t\t\t\ttest frame = new test();\n\t\t\t\t\tframe.setVisible(true);\n\t\t\t\t} catch (Exception e) {\n\t\t\t\t\te.printStackTrace();\n\t\t\t\t}\n\t\t\t}\n\t\t});\n\t}\n\n\t/**\n\t * Create the frame.\n\t */\n\tpublic test() {\n\t\tsetDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n\t\tsetBounds(100, 100, 450, 300);\n\t\tcontentPane = new JPanel();\n\t\tcontentPane.setBorder(new EmptyBorder(5, 5, 5, 5));\n\t\tcontentPane.setLayout(new BorderLayout(0, 0));\n\t\tsetContentPane(contentPane);\n";
		String lastHalf = "\t}\n\n}\n";
		
		//generate java code
		StringBuilder code = new StringBuilder("");
		code.append(firstHalf);
		
		Component[] components = panel.getComponents();
		
		//TODO: toJavaCode imcomplete
		for(Component component:components){
			MockComponent mock = (MockComponent)component;
			code.append(mock.toCode());
		}
		code.append(lastHalf);
		
		//write code to file
		FileWriter fw;
		try {
			String rootPath = System.getProperty("user.dir");
			
			fw = new FileWriter(javaFilePath);
			fw.write(code.toString());
			fw.close();
		} catch (IOException e) {
			//TODO: show message dialog
		}
	}
	public static void close() {
		System.exit(0);
	}
}