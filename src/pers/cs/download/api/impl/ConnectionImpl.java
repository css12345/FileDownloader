package pers.cs.download.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import pers.cs.download.api.Connection;
import pers.cs.download.entity.Settings;

public class ConnectionImpl implements Connection {
	
	HttpURLConnection connection;
	String url;

	public ConnectionImpl(String url) {
		this.url = url;
	}
	
	public void setTimeout() {
		Settings settings = Settings.getInstance();
		connection.setConnectTimeout(settings.TIMEOUT);
		connection.setReadTimeout(settings.TIMEOUT);
	}

	@Override
	public byte[] read(int startPos, int endPos) {
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			setTimeout();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String value = "bytes=" + startPos + "-" + endPos;
		connection.setRequestProperty("Range",value);
		
		try {
			connection.connect();
			int partSize = connection.getContentLength();
			if(partSize != endPos - startPos + 1) {
				connection.disconnect();
				return null;
			}
			else {
				try {
					InputStream inputStream = connection.getInputStream();
					byte[] buffer = new byte[partSize];
					byte[] temp = new byte[102400];
					int size = 0;
					int writed = 0;
					while((size = inputStream.read(temp)) != -1) {
						System.arraycopy(temp, 0, buffer, writed, size);
						writed += size;
					}
					System.out.println(Thread.currentThread().getName() + " " + writed);
					connection.disconnect();
					if(writed == partSize)
						return buffer;
					else 
						return null;
				}
				catch (SocketTimeoutException e) {
					connection.disconnect();
					System.out.println(Thread.currentThread().getName() + " " + e.getMessage());
					return null;
				}
				catch (IOException e) {
					connection.disconnect();
					System.out.println(Thread.currentThread().getName() + " " + e.getMessage());
					return null;
				} 
			}
		} catch (SocketTimeoutException e) {
			connection.disconnect();
			System.out.println(Thread.currentThread().getName() + " " + e.getMessage());
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			connection.disconnect();
		}
		return null;
	}

	@Override
	public int getContentLength() {
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			setTimeout();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String value = "bytes=0-";
		connection.setRequestProperty("Range",value);
		try {
			connection.connect();
			int partSize = connection.getHeaderFieldInt("Content-Length", -1);
			return partSize;
		}
		catch (SocketTimeoutException e) {
			connection.disconnect();
			System.out.println(Thread.currentThread().getName() + " " + e.getMessage());
			return -1;
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return -1;
	}
}
