package pers.cs.download.api;

/**
 * 下载时需要与URL建立一个连接
 * @author cs
 *
 */
public interface Connection {
	/**
	 * 从当前的缓冲数组中拿出startPos-endPos之间的字节作为新数组返回，包括startPos和endPos所在位
	 * @param startPos 起始位置
	 * @param endPos 结束位置
	 * @return 当前缓冲数组中从起始位置到结束位置的字节数组
	 */
	byte[] read(int startPos,int endPos);
	
	/**
	 * 
	 * @return 当前缓冲数组的有效字节数的长度
	 */
	int getContentLength();
}
