package pers.cs.download.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import pers.cs.download.FileDownloader;
import pers.cs.download.api.ConnectionManager;
import pers.cs.download.api.DownloadListener;
import pers.cs.download.api.FileManager;
import pers.cs.download.api.impl.ConnectionManagerImpl;
import pers.cs.download.api.impl.FileManagerImpl;
import pers.cs.download.entity.Task;

public class TestDownload {
	
	boolean downloadFinished = false;

	@Test
	public void testDownload() throws IOException {
		FileManager fileManager = null;

		Task task = new Task();
		task.setURL("https://download.operachina.com/pub/opera/desktop/57.0.3098.106/win/Opera_57.0.3098.106_Setup_x64.exe");
		task.setFilePath("D:\\TestDownload");
		task.setFileName("Opera_57.0.3098.106_Setup_x64.exe");

		// 根据url创建FileDownloader
		String url = task.getURL();
		FileDownloader fd = new FileDownloader(url);

		// 创建FileManager并设置到fd中
		String fullFilePath = task.getFilePath() + File.separatorChar + task.getFileName();
		File file = new File(fullFilePath);
		if(file.exists())
			file.delete();
		try {
			fileManager = new FileManagerImpl(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		fd.setFileManager(fileManager);

		// 创建ConnectionManager并设置到fd中
		ConnectionManager connectionManager = new ConnectionManagerImpl();
		fd.setConnectionManager(connectionManager);

		// 设置DownloadListener
		fd.setListener(new DownloadListener() {

			@Override
			public void notifyFinished() {
				downloadFinished = true;
			}
		});

		// 开启一个线程开始下载
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					fd.execute();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		// 没有下载结束一直在循环中
		while (!downloadFinished) {
			try {
				System.out.println("还没有下载完成，休眠5秒！\n");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("下载完成！");
		
		File expectedFile = new File("D:\\Users\\lenovo\\下载\\Programs\\Opera_57.0.3098.106_Setup_x64.exe");
		File actualFile = new File(fullFilePath);
		FileInputStream expectedInputStream = new FileInputStream(expectedFile);
		FileInputStream actualInputStream = new FileInputStream(actualFile);
		
		while(true) {
			int t = 0;
			int expected = expectedInputStream.read();
			int actual = actualInputStream.read();
			t++;
			if(expected == -1) {
				expectedInputStream.close();
				actualInputStream.close();
				break;
			}
			else {
				if(expected != actual)
					System.out.println("expected: " + expected + " actual: " + actual + " " + t);
				assertEquals(expected, actual);
			}
		}
	}
}
