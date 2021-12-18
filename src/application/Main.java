package application;
	
import java.io.File;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import application.utils.FileHelper;
import application.utils.HashHelper;
import application.utils.OSHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		if(updateApp() && OSHelper.launchProgram("./ModpackUpdater.jar")) return;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
			Parent screen = loader.load();
			Scene scene = new Scene(screen);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
			primaryStage.setResizable(false);
			primaryStage.setTitle("Banana Modpack Updater b.0.4");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateApp() {
		Gson gson = new Gson();
		try {
		String json = String.join("", FileHelper.readLines(FileHelper.readRemoteFile("https://raw.githubusercontent.com/Juanelsuper/ModpackUpdater/main/manifest.json")));
		JsonObject manifest = gson.fromJson(json, JsonObject.class);
		File currentJar = new File("./ModpackUpdater.jar");
		String checksum = HashHelper.checkSum(currentJar);
		if(checksum.equals(manifest.get("checksum").getAsString())) {
			return false;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to update the app?");
		alert.setTitle("Update checker");
		alert.setHeaderText("New update available!");
		alert.showAndWait();
		ButtonType result = alert.getResult();
		if(result == ButtonType.OK) {
			byte[] newVersion = FileHelper.readRemoteFile(manifest.get("jar").getAsString());
			return FileHelper.writeFile(currentJar.getParent(), currentJar.getName(), newVersion);
		}
		}catch(JsonSyntaxException | NullPointerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
