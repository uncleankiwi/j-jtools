package replaceTextLoop;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

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

	Label txtSaveFile = new Label("");
	Label txtSaveInstr = new Label("File will be saved as:");
	
	VBox startBox = new VBox();
	Button btnStart = new Button("Begin");
	
	
	@Override
	public void start(Stage pStage) throws Exception {
		txtLog.setPadding(new Insets(5));
		txtLog.setAlignment(Pos.TOP_LEFT);
		
		FileBox sourceBox = new FileBox("Source file:", pStage);
		FileBox searchBox = new FileBox("Search source with these lines:", pStage);
		FileBox replaceBox = new FileBox("Replace with these lines:", pStage);

		txtLog.setMinSize(300, 300);
		txtLog.setStyle("-fx-border-color: black;");
		uiWrapper.setPadding(new Insets(5));
		uiWrapper.getChildren().addAll(txtLog, filesBox);
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
				
				//getting filename of source to generate output file's name
				String sourceFull = sourceFile.toPath().toString();
				String name = "";
				String extension = "";	//includes "."
				
				Optional<String> optional = Optional.ofNullable(sourceFull)
						.filter(f -> f.contains("."));
				
				if (sourceFull.contains(".")) {
					name = optional.map(f -> f.substring(0, sourceFull.lastIndexOf("."))).get();
					extension = optional.map(f -> f.substring(sourceFull.lastIndexOf("."))).get();
				}
				else {
					name = sourceFull;
				}

				//try to append date
				String date = LocalDate.now().toString();
				outputFile = new File(name + " " + date + extension);
				if (outputFile.exists()){
					//but if it exists, append date + N
					int i = 1;
					while (outputFile.exists()) {
						outputFile = new File(name + " " + date + " " + i + extension);
					}
					logOutput("syso set output as " + outputFile.getName());
				}

				txtSaveFile.setText(outputFile.getName());
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




