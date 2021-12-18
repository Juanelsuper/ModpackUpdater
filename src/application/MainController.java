package application;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.core.UpdaterWorker;
import application.utils.ConfigHelper;
import application.utils.OSHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class MainController implements Initializable{
	
	@FXML
	TextField tf_minecraft, tf_modpack;
	
	@FXML
	TextArea tb_log;
	
	@FXML
	Button btn_update;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		HashMap<String, String> config = ConfigHelper.readConfig("./.config-modpack-updater");
		if(config.containsKey("modpack_path")) {
			this.tf_minecraft.setText(config.get("modpack_path"));
		}
		if(config.containsKey("modpack_url")) {
			this.tf_modpack.setText(config.get("modpack_url"));
		}
		
	}
	
	@FXML
	private void updateModpack(ActionEvent event){
		clearLog();
		if(this.tf_minecraft.getText().length() <= 0 || this.tf_modpack.getText().length() <= 0) {
			printInLog("ERROR: please enter the modpack URL and the minecraft version first");
			return;
		}
		HashMap<String, String> config = new HashMap<>();
		config.put("modpack_path", this.tf_minecraft.getText());
		config.put("modpack_url", this.tf_modpack.getText());
		ConfigHelper.writeConfig("./.config-modpack-updater", config);
		printInLog("INFO: getting missing mods...");
		toggleButton(false);
		UpdaterWorker worker = new UpdaterWorker(this, tf_minecraft.getText(), tf_modpack.getText());
		Thread t = new Thread(worker);
		t.start();
	}
	
	@FXML
	private void browseDir(ActionEvent event) {
		DirectoryChooser chooser = new DirectoryChooser();
		String minecraftLocation = OSHelper.isWindows() ? System.getenv("APPDATA") + "\\.minecraft\\versions" : System.getProperty("user.home") +"/Library/Application Support/minecraft/versions";
		File minecraftDir = new File(minecraftLocation);
		if(minecraftDir.isDirectory()) {
			chooser.setInitialDirectory(minecraftDir);
		}
		File file = chooser.showDialog(((Control)event.getSource()).getScene().getWindow());
		if(file != null && file.isDirectory()) {
			this.tf_minecraft.setText(file.getPath());
		}
	}
	
	
	public void clearLog() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tb_log.clear();
			}
		});
	}
	
	public void printInLog(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tb_log.appendText(message+ "\n");
			}
		});
		
	}
	public void toggleButton(Boolean enabled) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btn_update.setDisable(!enabled);
			}
		});
	}

	
}
