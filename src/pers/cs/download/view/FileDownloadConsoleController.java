package pers.cs.download.view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;
import javafx.stage.Stage;
import pers.cs.download.StartUp;
import pers.cs.download.entity.Settings;
import pers.cs.download.entity.Task;
import pers.cs.download.entity.TaskListWrapper;
import pers.cs.download.util.DateTimeUtil;

@SuppressWarnings({ "unused", "deprecation" })
public class FileDownloadConsoleController implements Initializable {

	@FXML
	private TextField newURL;
	@FXML
	private TextField newFileName;
	@FXML
	private Label newFilePath;
	@FXML
	private Label newThreadNum;
	@FXML
	private Label newBufferSize;

	@FXML
	private TableView<Task> taskTable;
	@FXML
	private TableColumn<Task, String> taskFileName;
	@FXML
	private TableColumn<Task, String> taskFilePath;
	@FXML
	private TableColumn<Task, String> taskURL;
	@FXML
	private TableColumn<Task, String> taskState;
	@FXML
	private TableColumn<Task, String> taskDownloadTime;

	public static ObservableList<Task> tasks = FXCollections.observableArrayList();

	@FXML
	private TextField defaultDownloadPath;
	@FXML
	private TextField defaultThreadNum;
	@FXML
	private TextField defaultBufferSize;
	
	
	private StartUp startUp;
	 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bind();
		
		setSettingsDefaultValues();

		associateTableWithData();
	}

	private void bind() {
		taskTable.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				taskFileName.setPrefWidth(newValue.doubleValue() / 5.0);
				taskFilePath.setPrefWidth(newValue.doubleValue() / 5.0);
				taskDownloadTime.setPrefWidth(newValue.doubleValue() / 5.0);
				taskState.setPrefWidth(newValue.doubleValue() / 5.0);
				taskURL.setPrefWidth(newValue.doubleValue() / 5.0);
			}
		});
	}

	private void associateTableWithData() {
		//从xml加载数据
		File file = new File("src/tasks.xml");
		loadTaskDataFromFile(file);
		
		
		//使表格和tasks相互关联
		taskTable.setItems(tasks);
		taskFileName.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
		taskFilePath.setCellValueFactory(cellData -> cellData.getValue().filePathProperty());
		taskState.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
		taskURL.setCellValueFactory(cellData -> cellData.getValue().URLProperty());
		taskDownloadTime.setCellValueFactory(
				cellData -> new SimpleStringProperty(DateTimeUtil.format(cellData.getValue().getDownloadTime())));
	}

	private void loadTaskDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			TaskListWrapper wrapper = (TaskListWrapper) um.unmarshal(file);
			if(wrapper.getTasks() != null)
				tasks.setAll(wrapper.getTasks());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveTaskDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(TaskListWrapper.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			TaskListWrapper wrapper = new TaskListWrapper();
			wrapper.setTasks(tasks);
			
			marshaller.marshal(wrapper, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	private void setSettingsDefaultValues() {
		Settings settings = Settings.getInstance();
		
		loadSettingsFromFile(new File("src/settings.property"));
		
		defaultDownloadPath.setText(settings.DEFAULT_DOWNLOAD_PATH);
		newFilePath.setText(defaultDownloadPath.getText());
		
		
		defaultThreadNum.setText(String.valueOf(settings.THREADNUM));
		newThreadNum.setText(defaultThreadNum.getText());
		
		
		defaultBufferSize.setText(String.valueOf(settings.SIZE));
		newBufferSize.setText(defaultBufferSize.getText());
	}

	private void loadSettingsFromFile(File file) {
		FileInputStream inputStream = null;
		try {
			Settings settings = Settings.getInstance();
			inputStream = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(inputStream);
			String defaultFilePath = properties.getProperty("defaultFilePath");
			String threadNum = properties.getProperty("threadNum");
			String size = properties.getProperty("size");
			String maxThreadNum = properties.getProperty("maxThreadNum");
			String minThreadNum = properties.getProperty("minThreadNum");
			String maxBufferSize = properties.getProperty("maxBufferSize");
			String minBufferSize = properties.getProperty("minBufferSize");
			if(defaultFilePath != null)
				settings.DEFAULT_DOWNLOAD_PATH = defaultFilePath;
			if(threadNum != null)
				settings.THREADNUM = Integer.parseInt(threadNum);
			if(size != null)
				settings.SIZE = Integer.parseInt(size);
			if(maxThreadNum != null)
				settings.MAX_THREAD_NUM = Integer.parseInt(maxThreadNum);
			if(minThreadNum != null)
				settings.MIN_THREAD_NUM = Integer.parseInt(minThreadNum);
			if(maxBufferSize != null)
				settings.MAX_BUFFER_SIZE = Integer.parseInt(maxBufferSize);
			if(minBufferSize != null)
				settings.MIN_BUFFER_SIZE = Integer.parseInt(minBufferSize);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
				try {
					if(inputStream != null)
						inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public void startDownload() throws IOException {
		Task task = new Task();
		String errorMessage = "";
		if(!newFileName.getText().trim().equals(""))
			task.setFileName(newFileName.getText().trim());
		else 
			errorMessage = "请输入文件名！";
		
		if(!newURL.getText().trim().equals(""))
			task.setURL(newURL.getText().trim());
		else
			errorMessage = "请输入URL！";
		
		if(errorMessage.equals("")) {
			//判断输入路径下是否存在文件并选择是否覆盖
			String filePath = newFilePath.getText() + File.separatorChar + newFileName.getText();
			File file = new File(filePath);
			if(file.exists()) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setContentText("该路径下文件已存在，是否删除");
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK)
					file.delete();
				else
					return;
			}
			
			LocalDateTime downloadTime = LocalDateTime.now();
			task.setDownloadTime(downloadTime);
			task.setFilePath(newFilePath.getText());
			task.setState("下载中");
			tasks.add(task);
			startUp.openDownloadTask(task);
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(errorMessage);
			alert.show();
		}
	}
	
	public void delete() {
		int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
		if(selectedIndex == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("请选中一行！");
			alert.show();
		}
		else {
			Task task = tasks.get(selectedIndex);
			File selectedFile = new File(task.getFilePath() + File.separatorChar + task.getFileName());
			tasks.remove(task);
			saveTaskDataToFile(new File("src/tasks.xml"));
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setContentText("是否同时删除文件?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				boolean successful = selectedFile.delete();
				if(!successful) {
					Alert deleteAlert = new Alert(AlertType.ERROR);
					deleteAlert.setContentText("删除失败！");
					deleteAlert.show();
				}
			}
		}
	}

	public void copy() {
		int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
		if(selectedIndex == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("请选中一行！");
			alert.show();
		}
		else {
			Task task = tasks.get(selectedIndex);
			String text = task.toString();
			
			// 获取系统剪贴板
	        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	        // 封装文本内容
	        Transferable trans = new StringSelection(text);
	        // 把文本内容设置到系统剪贴板
	        clipboard.setContents(trans, null);
	        
	        Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("复制成功！");
			alert.show();
		}
	}

	public void openFilePath() {
		int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
		if(selectedIndex == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("请选中一行！");
			alert.show();
		}
		else {
			Task task = tasks.get(selectedIndex);
			File selectedFile = new File(task.getFilePath() + File.separatorChar + task.getFileName());
			try {
				Runtime.getRuntime().exec("explorer /e,/select," + selectedFile.getAbsolutePath());
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("打开失败");
				alert.setContentText(e.getMessage());
				alert.show();
			}
		}
	}

	public void reDownload() throws IOException {
		int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
		if(selectedIndex == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("请选中一行！");
			alert.show();
		}
		else {
			Task task = tasks.get(selectedIndex);
			Task newTask = new Task();
			newTask.setFileName(task.getFileName());
			newTask.setFilePath(task.getFilePath());
			newTask.setURL(task.getURL());
			newTask.setState("下载中");
			newTask.setDownloadTime(LocalDateTime.now());
			tasks.add(newTask);
			startUp.openDownloadTask(newTask);
		}
	}

	public void updateDefaultDownloadPath() {
		Settings settings = Settings.getInstance();
		DirectoryChooserBuilder builder = DirectoryChooserBuilder.create();
		String currentDownloadPath = settings.DEFAULT_DOWNLOAD_PATH;
		if(currentDownloadPath == null)
			currentDownloadPath = System.getProperty("user.home");
        File file = new File(currentDownloadPath);
        builder.initialDirectory(file);
		DirectoryChooser directoryChooser = builder.build();
		File selectedfile = directoryChooser.showDialog(new Stage());
		if(selectedfile != null) {
			settings.DEFAULT_DOWNLOAD_PATH = selectedfile.getAbsolutePath();
			defaultDownloadPath.setText(settings.DEFAULT_DOWNLOAD_PATH);
			newFilePath.setText(defaultDownloadPath.getText());
		}
		
		
		saveSettingsToFile(new File("src/settings.property"));
	}

	private void saveSettingsToFile(File file) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			Settings settings = Settings.getInstance();
			Properties properties = new Properties();
			String defaultFilePath = settings.DEFAULT_DOWNLOAD_PATH;
			String threadNum = String.valueOf(settings.THREADNUM);
			String size = String.valueOf(settings.SIZE);
			String maxThreadNum = String.valueOf(settings.MAX_THREAD_NUM);
			String minThreadNum = String.valueOf(settings.MIN_THREAD_NUM);
			String maxBufferSize = String.valueOf(settings.MAX_BUFFER_SIZE);
			String minBufferSize = String.valueOf(settings.MIN_BUFFER_SIZE);
			
			put(properties,"defaultFilePath",defaultFilePath,System.getProperty("user.home"));
			put(properties, "threadNum", threadNum, "32");
			put(properties, "size", size, "10240");
			put(properties,"maxThreadNum",maxThreadNum,"64");
			put(properties, "minThreadNum", minThreadNum, "1");
			put(properties, "maxBufferSize", maxBufferSize, "20480");
			put(properties,"minBufferSize",minBufferSize,"256");
			
			properties.store(outputStream, "settings");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void put(Properties properties, String key, String value1,String value2) {
		if(value1 != null && !value1.equals(""))
			properties.put(key, value1);
		else
			properties.put(key, value2);
	}

	public void updateDefaultThreadNum() {
		try {
			Settings settings = Settings.getInstance();
			int threadNum = Integer.parseInt(defaultThreadNum.getText());
			if(threadNum >= settings.MIN_THREAD_NUM && threadNum <= settings.MAX_THREAD_NUM) {
				settings.THREADNUM = threadNum;
				defaultThreadNum.setText(String.valueOf(threadNum));
				newThreadNum.setText(defaultThreadNum.getText());
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("修改成功！");
				alert.show();
				
				saveSettingsToFile(new File("src/settings.property"));
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("线程数应该在" + settings.MIN_THREAD_NUM + "到" + settings.MAX_THREAD_NUM + "之间！");
				alert.show();
			}
		}catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("输入了非整数！");
			alert.show();
		}
	}

	public void updateDefaultBufferSize() {
		try {
			Settings settings = Settings.getInstance();
			int size = Integer.parseInt(defaultBufferSize.getText());
			if(size >= settings.MIN_BUFFER_SIZE && size <= settings.MAX_BUFFER_SIZE) {
				settings.SIZE = size;
				defaultBufferSize.setText(String.valueOf(size));
				newBufferSize.setText(defaultBufferSize.getText());
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("修改成功！");
				alert.show();
				
				saveSettingsToFile(new File("src/settings.property"));
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("缓冲数组大小应该在" + settings.MIN_BUFFER_SIZE + "到" + settings.MAX_BUFFER_SIZE + "字节之间！");
				alert.show();
			}
		}catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("输入了非整数！");
			alert.show();
		}
	}

	public void setMain(StartUp startUp) {
		this.startUp = startUp;
	}
}
