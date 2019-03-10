package pers.cs.download;

import java.io.IOException;

import pers.cs.download.api.Connection;
import pers.cs.download.api.ConnectionManager;
import pers.cs.download.api.DownloadListener;
import pers.cs.download.api.FileManager;
import pers.cs.download.entity.Settings;

/**
 * 对整个文件下载的一个整合
 * @author cs
 *
 */
public class FileDownloader {
	
	String url;  //下载文件的URL
	FileManager fileManager; //设置文件管理器方便写入
	ConnectionManager connectionManager;  //设置连接管理器方便读
	DownloadListener downloadListener;    //文件下载完成后进行通知
	

	public FileDownloader(String url) {
		this.url = url;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public void setListener(DownloadListener downloadListener) {
		this.downloadListener = downloadListener;
	}

	/**
	 * 执行下载的过程，首先连接管理器根据URL打开一个连接，读取总长度，根据读的长度对每个线程进行任务分配，等待所有线程下载结束发起通知。
	 * 所有内容读完之后发起下载结束通知。
	 * @throws IOException 
	 */
	public void execute() throws IOException {
		Connection connection = connectionManager.open(url);
		int contentLength = -1;
		do {
			contentLength = connection.getContentLength();
		} while(contentLength == -1);
		int threadNum = Settings.getInstance().THREADNUM;
		int averageLength = contentLength / threadNum;
		
		//前threadNum-1个线程下载平均长度，最后一个线程把剩下的全部下载
		Thread[] threads = new Thread[threadNum];
		for(int i = 0;i < threadNum - 1;i++) 
			threads[i] = new DownloadThread(fileManager,connectionManager.open(url),averageLength * i,(averageLength * (i + 1) - 1));
		threads[threadNum - 1] = new DownloadThread(fileManager, connectionManager.open(url), averageLength * (threadNum - 1), contentLength - 1);
		
		for(int i = 0;i < threadNum;i++)
			threads[i].start();
		for(int i = 0;i < threadNum;i++)
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		fileManager.close();
		downloadListener.notifyFinished();
	}

}
