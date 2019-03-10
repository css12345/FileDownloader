package pers.cs.download.entity;

public class Settings {

	public String DEFAULT_DOWNLOAD_PATH = System.getProperty("user.home");
	
	public int THREADNUM = 10;
	
	public int MAX_THREAD_NUM = 64;
	
	public int MIN_THREAD_NUM = 1;
	
	public int MAX_TIMEOUT = 8000;
	
	public int MIN_TIMEOUT = 5000;
	
	public int TIMEOUT = 5000;
	
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
