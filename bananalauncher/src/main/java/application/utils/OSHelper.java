package application.utils;

import java.io.IOException;

public class OSHelper {
	public static boolean isWindows() {
		String os = System.getProperty("os.name");
		return os.toLowerCase().startsWith("win");
	
	}
	public static boolean launchProgram(String path) {
		try {
			if(isWindows()) {
				Runtime.getRuntime().exec("cmd /c start javaw -jar " + path);
			}
			else {
				Runtime.getRuntime().exec("java -jar " + path);
			}
		}catch(IOException e) {
			return false;
		}
		return true;
	}
}
