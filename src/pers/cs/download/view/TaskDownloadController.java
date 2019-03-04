package pers.cs.download.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pers.cs.download.FileDownloaderStart;
import pers.cs.download.entity.Task;

public class TaskDownloadController implements Initializable {
	
	@FXML
	private Text URL;
	@FXML
	private Text fileAbsolutePath;
	@FXML
	private TextArea output;
	
	private Task task;
	private static Stage stage;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void setStage(Stage stage) {
		TaskDownloadController.stage = stage;
	}

	public void setTask(Task task) {
		this.task = task;
		URL.setText(this.task.getURL());
		String fullFilePath = this.task.getFilePath() + File.separatorChar + this.task.getFileName();
		fileAbsolutePath.setText(fullFilePath);
		new Thread(() -> {
			new FileDownloaderStart().start(task, output);
		}).start();
	}
	
	public static void close() {
		stage.close();
	}
}
