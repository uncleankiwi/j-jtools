package replaceTextLoop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
//		txtSaveInstr
//		txtSaveFile
//		startBox
//			btnStart

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
		
		FileBox sourceBox = new FileBox("Source file:", pStage);
		FileBox searchBox = new FileBox("Search source with these lines:", pStage);
		FileBox replaceBox = new FileBox("Replace with these lines:", pStage);
		Label txtSaveInstr = new Label("File will be saved as:");
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
	
	public File file;
		
	public FileBox(String strInstructions, Stage stage){
		txtInstruction.setText(strInstructions);
		txtFile.setMinWidth(200);
		txtFile.setMaxWidth(200);
			
		leftBox.getChildren().addAll(txtInstruction, txtFile);
		this.getChildren().addAll(leftBox, btnOpen);
		this.setPadding(new Insets(10, 0, 10, 0));
		
		btnOpen.setOnAction(e-> {
			file = fileChooser.showOpenDialog(stage);
			txtFile.setText(file.getName());
		});
	}
	

	
	
}

