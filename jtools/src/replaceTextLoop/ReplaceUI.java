package replaceTextLoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//UI for picking source code file and search/replace lines file.

//uiWrapWrapper - VBox
//	menuBar
//	uiWrapper - HBox
//		txtLog - TextField
//		filesBox - VBox
//			sourceBox
//			searchBox
//			replaceBox
//			txtSaveInstr
//			txtSaveFile
//			startBox
//				btnStart

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
	private String  log = "";

	private static Locale enLocale = new Locale("en");
	private static Locale jaLocale = new Locale("ja");
	private static Locale currentLocale = jaLocale;

	private VBox uiWrapWrapper = new VBox();
	private HBox uiWrapper = new HBox();
	private TextArea txtLog = new TextArea();
	private VBox filesBox = new VBox();

	private Label txtSaveFile = new Label("");
	private Label txtSaveInstr = new Label(getMessage("ReplaceUI.UI.file_saved_as"));
	
	private VBox startBox = new VBox();
	private Button btnStart = new Button(getMessage("ReplaceUI.UI.begin"));
	
	private LinesIndex searchLI = new LinesIndex();
	private LinesIndex replaceLI = new LinesIndex();
	private LinesIndex sourceLI = new LinesIndex();
	
	//both modes use fileCheck and put the source into a LinesIndex
	//differences:
	//LANG replaces lang() in source.
	//		Search and replace files are indexed into LinesIndex.
	//		Mode activated by opening two .txt files.
	//DESC replaces item descriptions in source.
	//		Search and replace files are indexed into Desc
	//		Mode activated by opening two .tsv files.
	private enum Mode {LANG, DESC};
	private Mode mode;
	
	@Override
	public void start(Stage pStage) throws Exception {
		//internationalisation stuff
		MenuBar menuBar = new MenuBar();
		Menu mnuLang = new Menu(getMessage("ReplaceUI.UI.language"));
		
		MenuItem mnuEn = new MenuItem(
				ResourceBundle.getBundle("replaceTextLoop.ApplicationResources", enLocale).getString("lang_name"));
		mnuEn.setOnAction(e -> {
			currentLocale = enLocale;
			ReplaceUI rui = new ReplaceUI();
			try {
				rui.start(pStage);		
			} catch (Exception ex) {
				logOutput(ex.getMessage());
			}
		});
		MenuItem mnuJa = new MenuItem(
				ResourceBundle.getBundle("replaceTextLoop.ApplicationResources", jaLocale).getString("lang_name"));
		mnuJa.setOnAction(e -> {
			currentLocale = jaLocale;
			ReplaceUI rui = new ReplaceUI();
			try {
				rui.start(pStage);		
			} catch (Exception ex) {
				logOutput(ex.getMessage());
			}
		});

		mnuLang.getItems().addAll(mnuEn, mnuJa);
		menuBar.getMenus().add(mnuLang);
		
		//UI
		txtLog.setPadding(new Insets(5));
		txtLog.setEditable(false);
		txtLog.setMaxSize(480, 300);
		txtLog.setMinSize(480, 300);
		txtLog.setStyle("-fx-border-color: black;");
		
		FileBox sourceBox = new FileBox(getMessage("ReplaceUI.UI.source_file"), pStage);
		FileBox searchBox = new FileBox(getMessage("ReplaceUI.UI.search_source_with"), pStage);
		FileBox replaceBox = new FileBox(getMessage("ReplaceUI.UI.replace_source_with"), pStage);


		uiWrapper.setPadding(new Insets(5));
		uiWrapper.getChildren().addAll(txtLog, filesBox);
		startBox.getChildren().add(btnStart);
		startBox.setAlignment(Pos.CENTER);
		
		uiWrapWrapper.getChildren().addAll(menuBar, uiWrapper);
		
		filesBox.setPadding(new Insets(10));
		filesBox.getChildren().addAll(sourceBox, searchBox, replaceBox, txtSaveInstr, txtSaveFile, startBox);
		filesBox.setAlignment(Pos.CENTER_LEFT);
		
		LinesIndex.setLogOutputListener(new LogInterface() {
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
				String[] filenameArray = getFileNameAndExtension(sourceFull);
				
				String name = filenameArray[0];
				String extension = filenameArray[1];	//includes "."
				
				//try to append date
				String date = LocalDate.now().toString();
				outputFile = new File(name + " " + date + extension);
				if (outputFile.exists()){
					//but if it exists, append date + N
					int i = 1;
					while (outputFile.exists()) {
						outputFile = new File(name + " " + date + " " + i + extension);
						i++;
					}
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
				alert.setHeaderText(getMessage("ReplaceUI.btnStart.specify_source"));
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (searchFile == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(getMessage("ReplaceUI.btnStart.specify_search"));
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (replaceFile == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(getMessage("ReplaceUI.btnStart.specify_replacement"));
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (outputFile == null) {
				//this shouldn't happen
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(getMessage("ReplaceUI.btnStart.cannot_output"));
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else {
				boolean pass = true;
				pass = fileCheck();
				
				//extension case selection
				if (pass) {
					logOutput(ReplaceUI.getMessage("ReplaceUI.btnStart.begin_replacement"));
					
					
					this.sourceLI = LinesIndex.replaceLoop(this.searchLI, this.sourceLI, this.replaceLI);
				}
				
			}
			
		});
				
		Scene scene = new Scene(uiWrapWrapper);
		pStage.setScene(scene);
		pStage.setTitle(getMessage("window_title"));
		pStage.setResizable(false);
		pStage.show();
	}
	
	public static void main(String[] args) {
		launch();
	}
	
	public void logOutput(String msg) {
		log += msg + "\n";
		txtLog.setText(log);
	}
	
	public static String[] getFileNameAndExtension(String filename){
		String name = "";
		String extension = "";	//includes "."
		
		Optional<String> optional = Optional.ofNullable(filename)
				.filter(f -> f.contains("."));
		
		if (filename.contains(".")) {
			name = optional.map(f -> f.substring(0, filename.lastIndexOf("."))).get();
			extension = optional.map(f -> f.substring(filename.lastIndexOf("."))).get();
		}
		else {
			name = filename;
		};
		return new String[] {name, extension};
	}
	
	public static String getMessage(String key) {
		return ResourceBundle.getBundle("replaceTextLoop.ApplicationResources", currentLocale).getString(key);
	}
	
	//overloaded log printing method for putting in placeholders {0}, {1}, etc
	public static String getMessage(String key, Object[] args) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(currentLocale);
		return MessageFormat.format(getMessage(key), args);
	}
	
	public boolean fileCheck() {
		this.searchLI = new LinesIndex();
		this.replaceLI = new LinesIndex();
		this.sourceLI = new LinesIndex();
		
		//open the 3 files
		if (!this.sourceFile.exists() || this.sourceFile == null){
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.source_does_not_exist", 
					new Object[] {this.sourceFile.getName()}));
			return false;
		}
		else if (!this.searchFile.exists()) {
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.search_does_not_exist", 
					new Object[] {this.searchFile.getName()}));
			return false;
		}
		else if (!this.replaceFile.exists()) {
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.replacement_does_not_exist", 
					new Object[] {this.replaceFile.getName()}));
			return false;
		}
				
		try {
			Files.copy(this.sourceFile.toPath(), this.outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
			logOutput(e.getMessage());
			return false;
		}
		
		//checking extensions and switching modes
		this.mode = null;
		String searchExtension = getFileNameAndExtension(this.sourceFile.toPath().toString())[1];
		String replaceExtension = getFileNameAndExtension(this.replaceFile.toPath().toString())[1];
		if (searchExtension == ".txt" && replaceExtension == ".txt") {
			this.mode = Mode.LANG;
		}
		else if (searchExtension == ".tsv" && replaceExtension == ".tsv"){
			this.mode = Mode.DESC;
		}
		else {
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.incorrect_file_extensions", 
					new Object[] {searchExtension, replaceExtension}));
			return false;
		}
		
		
		//parse files
		int searchLinesRows = 0;
		int replaceLinesRows = 0;
		try {
			Scanner replaceLinesScanner = new Scanner(this.replaceFile);
			Scanner searchLinesScanner = new Scanner(this.searchFile);
			
			//if number of lines in files don't match, stop	
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.checking_files"));
			while (replaceLinesScanner.hasNext()) {
				replaceLinesRows++;
				replaceLI.add(replaceLinesScanner.nextLine());
			}
			while (searchLinesScanner.hasNext()) {
				searchLinesRows++;
				searchLI.add(searchLinesScanner.nextLine());
			}
			searchLinesScanner.close();
			replaceLinesScanner.close();
			
			if (searchLinesRows == 0) {
				logOutput((ReplaceUI.getMessage("ReplaceUI.fileCheck.search_file_empty")));
				return false;
			}
			else if (searchLinesRows != replaceLinesRows) {
				logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.file_not_equal_length", 
						new Object[] {searchLinesRows, replaceLinesRows} ));
				return false;
			}
			
			Scanner sourceScanner = new Scanner(sourceFile);
			while (sourceScanner.hasNext()) {
				sourceLI.add(sourceScanner.nextLine());
			}
			sourceScanner.close();
			
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.files_checked"));
			return true;
		} catch (Exception e) {
			logOutput(e.getMessage());
			return false;
		}
	}
	
	
	//output to file
	public boolean outputSource() {
		try {
			PrintWriter printWriter = new PrintWriter(this.outputFile);
			boolean firstLine = true;
			ListIterator<Line> sourceIter = this.sourceLI.listIterator();
			while (sourceIter.hasNext()) {
				if (firstLine) {
					firstLine = false;
				}
				else {
					printWriter.append("\n");
				}
				printWriter.append(sourceIter.next().getRaw());
			}
			printWriter.close();
			
			logOutput(ReplaceUI.getMessage("ReplaceUI.outputSource.end_replacement"));
			return true;
		} catch (Exception e) {
			logOutput(e.getMessage());
			return false;
		}

	}
}




