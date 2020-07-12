package replaceTextLoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ListIterator;
import java.util.Locale;
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
	private Label txtSaveInstr = new Label(getMessage("file_saved_as"));
	
	private VBox startBox = new VBox();
	private Button btnStart = new Button(getMessage("begin"));
	
	private LinesIndex searchLI = new LinesIndex();
	private LinesIndex replaceLI = new LinesIndex();
	private LinesIndex sourceLI = new LinesIndex();
	
	@Override
	public void start(Stage pStage) throws Exception {
		//internationalisation stuff
		MenuBar menuBar = new MenuBar();
		Menu mnuLang = new Menu(getMessage("language"));
		
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
		
		FileBox sourceBox = new FileBox(getMessage("source_file"), pStage);
		FileBox searchBox = new FileBox(getMessage("search_source_with"), pStage);
		FileBox replaceBox = new FileBox(getMessage("replace_source_with"), pStage);


		uiWrapper.setPadding(new Insets(5));
		uiWrapper.getChildren().addAll(txtLog, filesBox);
		startBox.getChildren().add(btnStart);
		startBox.setAlignment(Pos.CENTER);
		
		uiWrapWrapper.getChildren().addAll(menuBar, uiWrapper);
		
		filesBox.setPadding(new Insets(10));
		filesBox.getChildren().addAll(sourceBox, searchBox, replaceBox, txtSaveInstr, txtSaveFile, startBox);
		filesBox.setAlignment(Pos.CENTER_LEFT);
		
		LinesIndex.setLogOutputListener(new LogInterface() {	//TODO remove
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
				alert.setHeaderText(getMessage("specify_source"));
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (searchFile == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(getMessage("specify_search"));
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (replaceFile == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(getMessage("specify_replacement"));
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else if (outputFile == null) {
				//this shouldn't happen
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(getMessage("cannot_output"));
				alert.setTitle("");
				alert.showAndWait().ifPresent(response -> {});
			}
			else {
				replaceLangInFiles(sourceFile, searchFile, replaceFile, outputFile);
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
	
	public static String getMessage(String key) {
		return ResourceBundle.getBundle("replaceTextLoop.ApplicationResources", currentLocale).getString(key);
	}
	
	//overloaded log printing method for putting in placeholders {0}, {1}, etc
	public static String getMessage(String key, Object[] args) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(currentLocale);
		return MessageFormat.format(getMessage(key), args);
	}
	
	public void replaceLangInFiles(File sourceFile, File searchLinesFile, File replaceLinesFile, File outputFile) {
		//open the 3 files
		if (!sourceFile.exists() || sourceFile == null){
			logOutput(ReplaceUI.getMessage("source_does_not_exist", 
					new Object[] {sourceFile.getName()}));
			return;
		}
		else if (!searchLinesFile.exists()) {
			logOutput(ReplaceUI.getMessage("search_does_not_exist", 
					new Object[] {searchLinesFile.getName()}));
			return;
		}
		else if (!replaceLinesFile.exists()) {
			logOutput(ReplaceUI.getMessage("replacement_does_not_exist", 
					new Object[] {replaceLinesFile.getName()}));
			return;
		}
		
		try {
			Files.copy(sourceFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
			logOutput(e.getMessage());;
		}
		
		int searchLinesRows = 0;
		int replaceLinesRows = 0;
		
		//opening files, checking;
		try {
			Scanner replaceLinesScanner = new Scanner(replaceLinesFile);
			Scanner searchLinesScanner = new Scanner(searchLinesFile);
			
			//if number of lines in files don't match, stop	
			logOutput(ReplaceUI.getMessage("checking_files"));
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
				logOutput((ReplaceUI.getMessage("search_file_empty")));
				return;
			}
			else if (searchLinesRows != replaceLinesRows) {
				logOutput(ReplaceUI.getMessage("file_not_equal_length", 
						new Object[] {searchLinesRows, replaceLinesRows} ));
				return;
			}

			logOutput(ReplaceUI.getMessage("files_checked"));
			
			
			//for every line N in LEFT:
			//if LEFT found in ORIGINAL, substitute with RIGHT, subCount++
			//if LEFT not found, search for RIGHT, alreadySubbedCount++
			//if RIGHT also not found, add N, LEFT to dictionary
			//print subCount, alreadySubbedCount
			logOutput(ReplaceUI.getMessage("begin_replacement"));
			int subCount = 0;	//lines replaced
			int alreadySubbedCount = 0; //lines already replaced
			int notFoundCount = 0;	//neither searched lines nor replacement found
			Scanner sourceScanner = new Scanner(sourceFile);
			while (sourceScanner.hasNext()) {
				sourceLI.add(sourceScanner.nextLine());
			}
			sourceScanner.close();
			
			sourceLI = LinesIndex.replaceLoop(searchLI, sourceLI, replaceLI);
			
			//output to file
			PrintWriter printWriter = new PrintWriter(outputFile);
			boolean firstLine = true;
			ListIterator<Line> sourceIter = sourceLI.listIterator();
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

			replaceLinesScanner.close();
			searchLinesScanner.close();
			logOutput((ReplaceUI.getMessage("replaced", new Object[] {subCount})));
			logOutput(ReplaceUI.getMessage("already_replaced", new Object[] {alreadySubbedCount}));
			logOutput(ReplaceUI.getMessage("not_found", new Object[] {notFoundCount}));
			
			} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
	}
}




