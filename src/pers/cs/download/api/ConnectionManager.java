package pers.cs.download.api;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ConnectionManager {
	/**
	 * 开启一个连接以便进行下载
	 * @param url 要下载的URL
	 * @return 一个进行下载的连接
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	Connection open(String url) throws MalformedURLException, IOException;
}
