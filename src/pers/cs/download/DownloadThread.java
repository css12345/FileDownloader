package pers.cs.download;

import pers.cs.download.api.Connection;
import pers.cs.download.api.FileManager;

/**
 * 下载线程
 * @author cs
 *
 */
public class DownloadThread extends Thread {
	
	FileManager fileManager;
	Connection connection;
	int startPos;
	int endPos;
	int hasDownload;

	public DownloadThread(FileManager fileManager, Connection connection, int startPos, int endPos) {
		this.fileManager = fileManager;
		this.connection = connection;
		this.startPos = startPos;
		this.endPos = endPos;
		this.hasDownload = 0;
	}
	
	@Override
	public void run() {
		byte[] downloadContent = null;
		while(true) {
			System.out.println(Thread.currentThread().getName() + " is try to read " + startPos + " to " + endPos + "!");
			downloadContent = connection.read(startPos, endPos);
			if(downloadContent != null) 
				break;
		} 
		synchronized (fileManager) {
			System.out.println(Thread.currentThread().getName() + " is ready to write!");
			fileManager.seek(startPos);
			fileManager.write(downloadContent, downloadContent.length);
			System.out.println(Thread.currentThread().getName() + " end!");
		}
		
	}

}
