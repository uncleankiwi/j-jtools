package replaceTextLoop;

import java.io.File;
import java.nio.file.Paths;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


//a pane that holds a file in the UI
//FileBox - HBox
//leftBox - VBox
//	txtInstruction - Label
//	txtFile - Label
//btnOpen

class FileBox extends HBox{
	private VBox leftBox = new VBox();
	private Label txtInstruction = new Label("");
	private Label txtFile = new Label("");
	private Button btnOpen = new Button(ReplaceUI.getMessage("FileBox.open"));
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
			fileChooser.setInitialDirectory(new File(Paths.get(".").toAbsolutePath().normalize().toString()));
			file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				txtFile.setText(file.getName());
				//4. trigger listener
				listener.onFileOpened(file);
			}
		});
	}
	
	public void setFile(File newFile) {
		this.file = newFile;
		if (file != null) {
			txtFile.setText(file.getName());
		}
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