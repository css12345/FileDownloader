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
	long previousDownloadBytes;

	public DownloadThread(FileManager fileManager, Connection connection, int startPos, int endPos,long previousDownloadBytes) {
		this.fileManager = fileManager;
		this.connection = connection;
		this.startPos = startPos;
		this.endPos = endPos;
		this.previousDownloadBytes = previousDownloadBytes;
	}
	
	@Override
	public void run() {
		//首先从缓冲区读，然后把读的内容从文件管理器写入文件。由于读文件不会有共享变量的修改问题，所有不需要加锁；
			//文件写入时需要调整文件指针的位置，再进行写入，这两步必须一起做，不然可能会漏写，所以加同步所。
		byte[] downloadContent = connection.read(startPos, endPos);
		synchronized (fileManager) {
			fileManager.seek(startPos,previousDownloadBytes);
			fileManager.write(downloadContent, downloadContent.length);
		}
	}

}
