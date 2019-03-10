package pers.cs.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import pers.cs.download.api.ConnectionManager;
import pers.cs.download.api.DownloadListener;
import pers.cs.download.api.FileManager;
import pers.cs.download.api.impl.ConnectionManagerImpl;
import pers.cs.download.api.impl.FileManagerImpl;
import pers.cs.download.entity.Task;
import pers.cs.download.view.FileDownloadConsoleController;
import pers.cs.download.view.TaskDownloadController;

public class FileDownloaderStart {
	
	protected boolean downloadFinished = false;
	
	
	FileManager fileManager = null;
	File tasksFile = new File("src/tasks.xml");
	
	public void start(Task task,TextArea output) {
		//获取下载开始时间
		long startTime = System.currentTimeMillis();
		
		//根据url创建FileDownloader
		String url = task.getURL();
		FileDownloader fd = new FileDownloader(url);
		
		
		//创建FileManager并设置到fd中
		String fullFilePath = task.getFilePath() + File.separatorChar + task.getFileName();
		File file = new File(fullFilePath);
		try {
			fileManager = new FileManagerImpl(file);
		} catch (FileNotFoundException e1) {
			dealError(task,file,e1.getMessage());
		}
		fd.setFileManager(fileManager);
		
		
		//创建ConnectionManager并设置到fd中
		ConnectionManager connectionManager = new ConnectionManagerImpl();
		fd.setConnectionManager(connectionManager);
		
		
		//设置DownloadListener
		fd.setListener(new DownloadListener() {
			
			@Override
			public void notifyFinished() {
				downloadFinished  = true;
			}
		});
		
		//开启一个线程开始下载
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					fd.execute();
				} catch (IOException e) {
					dealError(task, file, e.getMessage());
				}
			}
		}).start();
		
		//没有下载结束一直在循环中
		while(!downloadFinished) {
			try {
				Platform.runLater(() -> {
					output.appendText("还没有下载完成，休眠5秒！\n");
				});
				
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		//下载完成处理显示一些东西
		dealSuccess(task,output,startTime);
	}
	
	private void dealSuccess(Task task, TextArea output, long startTime) {
		task.setState("下载完成");
		FileDownloadConsoleController.saveTaskDataToFile(tasksFile);
		((FileManagerImpl)fileManager).close();
		
		double costTime = (System.currentTimeMillis() - startTime) / 1000.0;
		Platform.runLater(() -> {
			output.appendText("下载完成！\n");
			output.appendText("花费" + costTime + "秒！");
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("下载完成");
			alert.setContentText("花费" + costTime + "秒！");
			alert.show();
		});
	}

	private void dealError(Task task, File file, String message) {
		task.setState("下载出错");
		FileDownloadConsoleController.saveTaskDataToFile(tasksFile);
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("下载出错");
			alert.setContentText(message);
			alert.show();
			TaskDownloadController.close();
		});
		((FileManagerImpl)fileManager).close();
		file.delete();
		return;
	}
}
