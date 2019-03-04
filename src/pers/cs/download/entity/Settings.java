package pers.cs.download.entity;

public class Settings {

	public String DEFAULT_DOWNLOAD_PATH = System.getProperty("user.home");
	
	public int THREADNUM = 32;
	
	public int SIZE = 10240;
	
	public int MAX_THREAD_NUM = 64;
	
	public int MIN_THREAD_NUM = 1;
	
	public int MAX_BUFFER_SIZE = 20480;
	
	public int MIN_BUFFER_SIZE = 256;
	
	private static volatile Settings settings;
	
	private Settings() {
		
	}
	
	public static Settings getInstance() {
		if(settings == null) {
			synchronized (Settings.class) {
				if(settings == null) {
					settings = new Settings();
				}
				return settings;
			}
		}
		return settings;
	}
}
