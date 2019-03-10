package pers.cs.download.api;

import java.io.IOException;

public interface FileManager {
	/**
	 * 开启文件
	 * @throws IOException 
	 */
	void open();
	
	/**
	 * 移动文件指针到previousDownloadBytes+pos位置
	 * @param pos 本次的位置
	 */
	void seek(int pos);
	
	/**
	 * 将数据从data数组中读取length长度写入文件中
	 * @param data 当前要写入的数组
	 * @param length 要写入的长度
	 */
	void write(byte[] data,int length);
	
	/**
	 * 关闭文件
	 */
	void close();
}
