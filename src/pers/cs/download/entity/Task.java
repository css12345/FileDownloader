package pers.cs.download.entity;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pers.cs.download.util.DateTimeUtil;
import pers.cs.download.util.LocalDateTimeAdapter;

public class Task {

	private StringProperty fileName = new SimpleStringProperty();
	private StringProperty filePath = new SimpleStringProperty();
	private StringProperty state = new SimpleStringProperty();
	private StringProperty url = new SimpleStringProperty();
	private ObjectProperty<LocalDateTime> downloadTime = new SimpleObjectProperty<>();

	public StringProperty fileNameProperty() {
		return this.fileName;
	}

	public String getFileName() {
		return this.fileNameProperty().get();
	}

	public void setFileName(String fileName) {
		this.fileName.set(fileName);
	}

	public StringProperty stateProperty() {
		return this.state;
	}

	public String getState() {
		return this.stateProperty().get();
	}

	public void setState(String state) {
		this.stateProperty().set(state);
	}

	public StringProperty filePathProperty() {
		return this.filePath;
	}

	public String getFilePath() {
		return this.filePathProperty().get();
	}

	public void setFilePath(String filePath) {
		this.filePathProperty().set(filePath);
	}

	public StringProperty URLProperty() {
		return this.url;
	}

	public String getURL() {
		return this.URLProperty().get();
	}

	public void setURL(String url) {
		this.URLProperty().set(url);
	}

	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	public LocalDateTime getDownloadTime() {
		return downloadTime.get();
	}

	public void setDownloadTime(LocalDateTime downloadTime) {
		this.downloadTimeProperty().set(downloadTime);
	}

	public ObjectProperty<LocalDateTime> downloadTimeProperty() {
		return downloadTime;
	}

	@Override
	public String toString() {
		return "Task [fileName=" + fileName.get() + ", filePath=" + filePath.get() + ", state=" + state.get() + ", url=" + url.get()
				+ ", downloadTime=" + DateTimeUtil.format(downloadTime.get()) + "]";
	}
}
