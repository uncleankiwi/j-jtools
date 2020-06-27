package replaceTextLoop;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
//		outputBox
//		btnStart

//FileBox - HBox
//	leftBox - VBox
//		txtInstruction - Label
//		txtFile - Label
//	btnOpen

public class ReplaceUI extends Application {	
	@Override
	public void start(Stage pStage) throws Exception {
		HBox uiWrapper = new HBox();
		Label txtLog = new Label();
		VBox filesBox = new VBox();
		
		FileBox sourceBox = 
				new FileBox("Source file:", 
						pStage, FileBox.Mode.OPEN);
		FileBox searchBox = 
				new FileBox("File with lines to search:", 
						pStage, FileBox.Mode.OPEN);
		FileBox replaceBox = 
				new FileBox("", 
						pStage, FileBox.Mode.OPEN);
		FileBox outputBox = 
				new FileBox("Save output as:", 
						pStage, FileBox.Mode.SAVE);
		Button btnStart = new Button("Begin");
		txtLog.setMinSize(300, 300);
		txtLog.setStyle("-fx-border-color: black;");
		uiWrapper.setPadding(new Insets(5));
		uiWrapper.getChildren().addAll(txtLog, filesBox);
		filesBox.setPadding(new Insets(10));
		filesBox.getChildren().addAll(sourceBox, searchBox, replaceBox, outputBox, btnStart);
		filesBox.setAlignment(Pos.CENTER_RIGHT);
		
		Scene scene = new Scene(uiWrapper);
		pStage.setScene(scene);
		pStage.setTitle("Replace text loop");
		pStage.show();
	}
	
	public static void main(String[] args) {
		launch();
	}

}


class FileBox extends HBox{
	private VBox leftBox = new VBox();
	private Label txtInstruction = new Label("");
	private Label txtFile = new Label("");
	private Button btnOpen = new Button("Open...");
	private FileChooser fileChooser = new FileChooser();
	public static enum Mode {OPEN, SAVE}
//	private FileChooser.ExtensionFilter extFilter = 
//			new FileChooser.ExtensionFilter().getExtensions().add(fileChooser);
	
	
	
	public File file;
	
	public FileBox(String strInstructions, Stage stage, Mode mode){
		txtInstruction.setText(strInstructions);
		txtFile.setMinWidth(200);
		txtFile.setMaxWidth(200);
			
		leftBox.getChildren().addAll(txtInstruction, txtFile);
		this.getChildren().addAll(leftBox, btnOpen);
		this.setPadding(new Insets(10, 0, 10, 0));
		
		btnOpen.setOnAction(e-> {
			if (mode == Mode.OPEN) {
				file = fileChooser.showOpenDialog(stage);
			}
			else if (mode == Mode.SAVE) {
				file = fileChooser.showSaveDialog(stage);
			}

			});
	}
	
	
}
