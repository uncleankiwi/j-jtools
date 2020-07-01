package replaceTextLoop;

import java.io.File;
import java.net.URLDecoder;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//uiWrapper - HBox
//	txtLog - TextField
//	filesBox - VBox
//		sourceBox
//		searchBox
//		replaceBox
//		txtSaveInstr
//		txtSaveFile
//		startBox
//			btnStart

//FileBox - HBox
//	leftBox - VBox
//		txtInstruction - Label
//		txtFile - Label
//	btnOpen

//listener stuff:
//1. interface in child with method to be run in parent
//2. instantiate interface in child
//3. for each child in parent, assign them an interface instance and implement listener callback (parent's reaction)
//4. trigger child's listener somewhere. In child?

public class ReplaceUI extends Application {
	private File sourceFile = null;
	private File searchFile = null;
	private File replaceFile = null;
	private File outputFile = null;
	private String log = "";
	
	HBox uiWrapper = new HBox();
	Label txtLog = new Label();
	VBox filesBox = new VBox();

	Label txtSaveInstr = new Label("File will be saved as:");
	
	@Override
	public void start(Stage pStage) throws Exception {
		txtLog.setPadding(new Insets(5));
		txtLog.setAlignment(Pos.TOP_LEFT);
		
		FileBox sourceBox = new FileBox("Source file:", pStage);
		FileBox searchBox = new FileBox("Search source with these lines:", pStage);
		FileBox replaceBox = new FileBox("Replace with these lines:", pStage);


		Label txtSaveFile = new Label("");
		txtLog.setMinSize(300, 300);
		txtLog.setStyle("-fx-border-color: black;");
		uiWrapper.setPadding(new Insets(5));
		uiWrapper.getChildren().addAll(txtLog, filesBox);
		VBox startBox = new VBox();
		Button btnStart = new Button("Begin");
		startBox.getChildren().add(btnStart);
		startBox.setAlignment(Pos.CENTER);
		
		filesBox.setPadding(new Insets(10));
		filesBox.getChildren().addAll(sourceBox, searchBox, replaceBox, txtSaveInstr, txtSaveFile, startBox);
		filesBox.setAlignment(Pos.CENTER_LEFT);
		
		ReplaceTextLoop.setLogOutputListener(new ReplaceTextLoop.LogInterface() {
			@Override
			public void onLogOutput(String msg) {
				logOutput(msg);
			}
		});
		
		//3. parent, listening for child
		sourceBox.setFileOpenedListener(new FileBox.FileOpenedInterface() {
			@Override
			public void onFileOpened(File file) {
				sourceFile = file;
				
				logOutput(sourceFile.toPath().toString());
				//TODO set outputFile
			}

			@Override
			public void onLogOutput(String msg) {
				logOutput(msg);				
			}
		});
		
		searchBox.setFileOpenedListener(new FileBox.FileOpenedInterface() {
			@Override
			public void onFileOpened(File file) {
				searchFile = file;
			}

			@Override
			public void onLogOutput(String msg) {
				logOutput(msg);	
			}
		});
		
		replaceBox.setFileOpenedListener(new FileBox.FileOpenedInterface() {
			@Override
			public void onFileOpened(File file) {
				replaceFile = file;
			}

			@Override
			public void onLogOutput(String msg) {
				logOutput(msg);				
			}
		});
		
		btnStart.setOnAction(e -> {
			//check if all files have been specified
			if (sourceFile == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Please specify a source file to open.");
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (searchFile == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Please specify a search line file to open.");
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (replaceFile == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Please specify a replacement line file to open.");
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (outputFile == null) {
				//this shouldn't happen
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Could not determine where to output file.");
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else {
				ReplaceTextLoop.replaceFiles(sourceFile, searchFile, replaceFile, outputFile);
			}
			
			
		});
				
		Scene scene = new Scene(uiWrapper);
		pStage.setScene(scene);
		pStage.setTitle("Replace text loop");
		pStage.show();
	}
	
	public static void main(String[] args) {
		launch();
	}
	
	public void logOutput(String msg) {
		log += msg + "\n";
		txtLog.setText(log);
	}


}


class FileBox extends HBox{
	private VBox leftBox = new VBox();
	private Label txtInstruction = new Label("");
	private Label txtFile = new Label("");
	private Button btnOpen = new Button("Open...");
	private FileChooser fileChooser = new FileChooser();	
	
	public File file;
	
	public FileBox(String strInstructions, Stage stage){
		txtInstruction.setText(strInstructions);
		txtFile.setMinWidth(200);
		txtFile.setMaxWidth(200);
			
		leftBox.getChildren().addAll(txtInstruction, txtFile);
		this.getChildren().addAll(leftBox, btnOpen);
		this.setPadding(new Insets(10, 0, 10, 0));
		
		btnOpen.setOnAction(e-> {
			//find where this jar is, and set file dialogue's default directory to it
			String decodedPath = "";
			try {
				String jarPath = (ReplaceUI.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
				decodedPath = URLDecoder.decode(jarPath, "UTF-8");
			} catch (Exception ex) {
				listener.onLogOutput(ex.getMessage());
			}
			fileChooser.setInitialDirectory(new File(decodedPath));
			file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				txtFile.setText(file.getName());
				//4. trigger listener
				listener.onFileOpened(file);
			}
		});
	}
	
	//1. interface with methods that will be fired in parent
	public interface FileOpenedInterface{
		public void onFileOpened(File file);
		
		public void onLogOutput(String msg);
	}
	
	//2. instantiate listener interface
	private FileOpenedInterface listener = null;
	
	public void setFileOpenedListener(FileOpenedInterface foi) {
		this.listener = foi;
	}
	
	
}

