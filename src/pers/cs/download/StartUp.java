package pers.cs.download;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pers.cs.download.entity.Task;
import pers.cs.download.view.FileDownloadConsoleController;
import pers.cs.download.view.TaskDownloadController;

public class StartUp extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(StartUp.class.getResource("view/FileDownloadConsole.fxml"));		
		VBox vBox = fxmlLoader.load();
		
		FileDownloadConsoleController consoleController = fxmlLoader.getController();
		consoleController.setMain(this);
		
		
		Scene scene = new Scene(vBox);
		primaryStage.setScene(scene);
		primaryStage.setTitle("多线程下载");
		primaryStage.show();
	}
	
	

	public static void main(String[] args) {
		launch(args);
	}



	public void openDownloadTask(Task task) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(StartUp.class.getResource("view/TaskDownload.fxml"));
		AnchorPane pane = loader.load();
		
		TaskDownloadController controller = loader.getController();
		controller.setTask(task);
		
		Scene scene = new Scene(pane);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("下载");
        stage.show();
        
        controller.setStage(stage);
	}
}
