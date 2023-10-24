package application.core;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import application.MainController;
import application.utils.FileHelper;
import application.utils.HashHelper;

public class UpdaterWorker implements Runnable {
	
	private MainController controller;
	private String mods_path, modpack;

	public UpdaterWorker(MainController controller, String minecraft, String modpack) {
		this.controller = controller;
		this.mods_path = Paths.get(minecraft,"mods").toString();
		this.modpack = modpack;
	}
	
	@Override
	public void run() {
		Gson gson = new Gson();
		try {
			String json = String.join("", FileHelper.readLines(FileHelper.readRemoteFile(modpack)));
			JsonArray modlist = gson.fromJson(json, JsonArray.class);
			HashMap<String, Mod> oldMods = getOldMods();
			Set<Mod> newMods = new HashSet<>();
			for(JsonElement element : modlist) {
				Mod mod = new Mod(element.getAsJsonObject());
				if(oldMods.containsKey(mod.checksum)) {
					oldMods.remove(mod.checksum);
				}
				else {
					newMods.add(mod);
				}
				
			}
			deleteOldMods(new HashSet<>(oldMods.values()));
			downloadNewMods(newMods);
			printInLog("INFO: Done!");
			
		}catch(JsonSyntaxException e) {
			printInLog("ERROR: invalid modpack file");
		}catch(Exception e) {
			printInLog("ERROR: unknown error");
		}finally{
			controller.toggleButton(true);
		}
	}
	
	private void deleteOldMods(Set<Mod> oldMods) {
		for(Mod mod : oldMods) {
			File file = new File(mod.url);
			if(file.delete()) {
				printInLog("INFO: Deleting old version of " + mod.name + "...");
			}else {
				printInLog("ERROR: Could not delete old version of " + mod.name);
			}
		}
	}

	private static boolean isJarName(String name){
		return name.endsWith(".jar");
	}
	
	private void downloadNewMods(Set<Mod> newMods) {
		for(Mod mod : newMods) {
			try {
				printInLog("INFO: downloading " + mod.name + "...");
				byte[] newMod = FileHelper.readRemoteFile(mod.url);
				String[] temp = mod.url.split("/");
				String fileName = temp[temp.length - 1];
				if(!isJarName(fileName)){
					fileName = mod.name;
					if(!isJarName(fileName)){
						fileName += ".jar";
					}
				}
				if(!FileHelper.writeFile(this.mods_path, fileName , newMod)) {
					printInLog("ERROR: failed to create file for " + mod.name);
				}
			}catch(Exception e) {
				e.printStackTrace();
				printInLog("ERROR: failed to download " + mod.name);
			}
		}
	}
	
	private HashMap<String, Mod> getOldMods() {
		HashMap<String, Mod> output = new HashMap<>();
		Mod aux;
		ArrayList<File> current_mods = FileHelper.listFilesInDirectory(this.mods_path);
		for(File mod: current_mods) {
			aux = new Mod(mod.getName(), mod.getPath(), HashHelper.checkSum(mod));
			output.put(aux.checksum, aux);
		}
		return output;
	}
	
	private void printInLog(String message) {
		controller.printInLog(message);
	}

}
