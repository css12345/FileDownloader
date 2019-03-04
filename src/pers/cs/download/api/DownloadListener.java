package pers.cs.download.api;

public interface DownloadListener {
	/**
	 * 当下载结束后发起一个通知
	 */
	void notifyFinished();
}
