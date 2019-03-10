package pers.cs.download.api;

/**
 * 下载时需要与URL建立一个连接
 * @author cs
 *
 */
public interface Connection {
	
	byte[] read(int startPos,int endPos);
	
	int getContentLength();
}
