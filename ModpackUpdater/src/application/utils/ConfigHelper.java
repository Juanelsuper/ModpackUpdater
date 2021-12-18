package application.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigHelper {

	public static HashMap<String, String> readConfig(String path) {
		HashMap<String, String> output = new HashMap<>();
		File file =  new File(path);
		if(file.exists()) {
			String lines[] = FileHelper.readLines(file);
			for(String line : lines) {
				String[] row = line.split("=",2);
				if(row.length == 2) {
					output.put(row[0], row[1]);
				}
			}
		}
		return output;
	}
	
	public static boolean writeConfig(String path, HashMap<String, String> config) {
		File file =  new File(path);
		String content = "";
		try {
			for(String key: config.keySet()) {
				content += key + "=" + config.get(key) + "\n";
			}
			FileHelper.writeFile(file, content);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
