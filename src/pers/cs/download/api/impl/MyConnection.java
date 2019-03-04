package pers.cs.download.api.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import pers.cs.download.api.Connection;
import pers.cs.download.entity.Settings;

public class MyConnection implements Connection {
	// 输入流
	BufferedInputStream bufferedInputStream;

	// 缓冲数组
	byte[] content = new byte[Settings.getInstance().SIZE];
	// 当前缓冲数组的有效长度
	int contentLength = -1;

	public MyConnection(String url) throws MalformedURLException, IOException {
		bufferedInputStream = new BufferedInputStream(new URL(url).openStream());
	}

	/**
	 * 当缓冲数组内容被全部下载后，再从连接中读取后面的内容到缓冲数组
	 * 
	 * @return 新的缓冲数组的有效长度，即读取了多少字节
	 * @throws IOException
	 */
	public int read() throws IOException {
		contentLength = bufferedInputStream.read(content);
		return contentLength;
	}

	@Override
	public byte[] read(int startPos, int endPos) {
		// System.out.println(startPos + " " + endPos);
		byte[] result = new byte[endPos - startPos + 1];

		System.arraycopy(content, startPos, result, 0, result.length);
		return result;
	}

	@Override
	public int getContentLength() {
		return contentLength;
	}

}
