package application.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileHelper {
	public static ArrayList<File> listFilesInDirectory(String path) {
		ArrayList<File> files = new ArrayList<File>();
		File dir = new File(path);
	    for (File file : dir.listFiles()) {
	        if (!file.isDirectory()) {
	        	files.add(file);
	        }
	    }
	    return files;
	}
	
	public static String[] readLines(File file) {
		byte[] bytes = readFile(file);
		if(bytes != null) {
			return readLines(bytes);
		}
		return null;
	}
	
	public static String[] readLines(byte[] bytes) {
		String content = "";
		for(byte b: bytes) {
			content += (char)b;
		}
		return content.split("\n");
	}
	
	public static byte[] readFile(File file) {
		try {
			return Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			return null;
		}
	}
	
	public static boolean writeFile(String dir, String filename, byte[] data) {
		Path outputFile = Paths.get(dir, filename);
		try (FileOutputStream fos = new FileOutputStream(outputFile.toString())) {
			   fos.write(data);
			   return true;
		} catch (IOException e) {
			return false;
		}
	}
	public static void writeFile(File file, String content) throws IOException {
		file.delete();
	    BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath()));
	    writer.write(content);
	    writer.close();
	}
	
	public static byte[] readRemoteFile(String url) {
		InputStream input = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
		URL _url = new URL(url);
		byte[] chunk = new byte[4096];
		input = _url.openStream();
		int i;
	    while ((i = input.read(chunk)) > 0) {
	    	output.write(chunk, 0, i);
	    }
		}catch(IOException ex) {
			
		}finally {
			try {
				output.close();
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output.toByteArray();
	}
}
