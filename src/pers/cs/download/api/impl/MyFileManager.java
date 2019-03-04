package pers.cs.download.api.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import pers.cs.download.api.FileManager;

public class MyFileManager implements FileManager {

	/**
	 * 从URL下载后保存在该文件中
	 */
	RandomAccessFile file;

	public MyFileManager(String path) throws FileNotFoundException {
		this.file = new RandomAccessFile(path, "rw");
	}

	public MyFileManager(File file) throws FileNotFoundException {
		this.file = new RandomAccessFile(file, "rw");
	}

	@Override
	public void open() {
		try {
			file.seek(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void seek(int pos, long previousDownloadBytes) {
		try {
			// System.out.println("seek:" + previousDownloadBytes + pos);
			file.seek(previousDownloadBytes + pos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(byte[] data, int length) {
		try {
			file.write(data, 0, length);
			// System.out.println("file length:" + file.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
