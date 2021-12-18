module ModpackUpdaterGUI {
	requires javafx.controls;
	requires java.sql;
	requires gson;
	requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
}
