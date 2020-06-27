package replaceTextLoop;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
		TextField txtLog = new TextField();
		VBox filesBox = new VBox();
		
		FileBox sourceBox = new FileBox();
		FileBox searchBox = new FileBox();
		FileBox replaceBox = new FileBox();
		FileBox outputBox = new FileBox();
		Button btnStart = new Button();
		
		uiWrapper.getChildren().addAll(txtLog, filesBox);
		filesBox.getChildren().addAll(sourceBox, searchBox, replaceBox, outputBox, btnStart);
		
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
	private VBox leftBox;
	private Label txtInstruction = new Label("");
	private Label txtFile = new Label("");
	private Button btnOpen = new Button("Open...");
	
	public File file;
	
	public FileBox(String strInstructions){
		txtInstruction.setText(strInstructions);
		leftBox.getChildren().addAll(txtInstruction, txtFile);
		this.getChildren().addAll(leftBox, btnOpen);
	}
	
	public FileBox(){
		this("Open a file:");
	}
	
}
