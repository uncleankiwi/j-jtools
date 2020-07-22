package replaceTextLoop;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.LinkedList;
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
//			txtSaveInstr
//			txtSaveFile
//			startBox
//				btnStart

//listener stuff:
//1. interface in child with method to be run in parent
//2. instantiate interface in child
//3. for each child in parent, assign them an interface instance and implement listener callback (parent's reaction)
//4. trigger child's listener somewhere. In child?


//TODO update zemi translations
//TODO known issues list: source/TL files appear empty if encoding is wrong
//TODO KIL: unable to parse multiple lang( ) in one line
//TODO fix files empty
//TODO fix lines not found
//TODO fix item desc TL
//TODO uncomment file renaming
//TODO publish release
//TODO pick encoding?

public class ReplaceUI extends Application {
	private File sourceFile = null;
	private File translationFile = null;
	private File outputFile = null;
	private String log = "";

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
	private ItemDescIndex itemDI = new ItemDescIndex();	//first item is used to search, other items for replacing
	
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
				ResourceBundle.getBundle("replaceTextLoop.ApplicationResources", enLocale).getString("ReplaceUI.UI.lang_name"));
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
				ResourceBundle.getBundle("replaceTextLoop.ApplicationResources", jaLocale).getString("ReplaceUI.UI.lang_name"));
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
		FileBox translationBox = new FileBox(getMessage("ReplaceUI.UI.translation_file"), pStage);

		uiWrapper.setPadding(new Insets(5));
		uiWrapper.getChildren().addAll(txtLog, filesBox);
		startBox.getChildren().add(btnStart);
		startBox.setAlignment(Pos.CENTER);
		
		uiWrapWrapper.getChildren().addAll(menuBar, uiWrapper);
		
		filesBox.setPadding(new Insets(10));
		filesBox.getChildren().addAll(sourceBox, translationBox, txtSaveInstr, txtSaveFile, startBox);
		filesBox.setAlignment(Pos.CENTER_LEFT);
		
		//for refreshing UI after changing language setting
		if (this.sourceFile != null) sourceBox.setFile(sourceFile);
		if (this.translationFile != null) translationBox.setFile(translationFile);
		if (this.outputFile != null) this.txtSaveFile.setText(outputFile.getName());
		if (this.log != "") this.txtLog.setText(this.log);
		
		LinesIndex.setLogOutputListener(new LogInterface() {
			@Override
			public void onLogOutput(String msg) {
				logOutput(msg);
			}
		});
		ItemDescIndex.setLogOutputListener(new LogInterface() {
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
				outputFile = new File(name + " " + date + extension);//TODO uncomment
//				if (outputFile.exists()){
//					//but if it exists, append date + N
//					int i = 1;
//					while (outputFile.exists()) {
//						outputFile = new File(name + " " + date + " " + i + extension);
//						i++;
//					}
//				}

				txtSaveFile.setText(outputFile.getName());
			}

			@Override
			public void onLogOutput(String msg) {
				logOutput(msg);				
			}
		});
		
		translationBox.setFileOpenedListener(new FileBox.FileOpenedInterface() {
			@Override
			public void onFileOpened(File file) {
				translationFile = file;
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
			else if (translationFile == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(getMessage("ReplaceUI.btnStart.specify_search"));
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
				boolean pass = false;
				pass = fileCheck();
				
				//extension case selection
				if (pass) {
					logOutput(ReplaceUI.getMessage("ReplaceUI.btnStart.begin_replacement"));
					
					if (this.mode == Mode.LANG) {
						this.sourceLI = LinesIndex.replaceLoop(this.searchLI, this.sourceLI, this.replaceLI);
					}
					else if (this.mode == Mode.DESC) {
						this.sourceLI = ItemDescIndex.replaceLoop(sourceLI, itemDI);
					}
					else {
						pass = false;
					}
					
				}
				
				//output to file
				if (pass) {
					pass = outputSource();
				}
				
			}
			
		});
				
		Scene scene = new Scene(uiWrapWrapper);
		pStage.setScene(scene);
		pStage.setTitle(getMessage("ReplaceUI.UI.window_title"));
		pStage.setResizable(false);
		pStage.show();
	}
	
	public static void main(String[] args) {
		launch();
	}
	
	public void logOutput(String msg) {
		log += msg + "\n";
		txtLog.setText(log);
		txtLog.end();
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
		this.itemDI = new ItemDescIndex();

		//open the 2 files
		if (!this.sourceFile.exists() || this.sourceFile == null){
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.source_does_not_exist", 
					new Object[] {this.sourceFile.getName()}));
			return false;
		}
		else if (!this.translationFile.exists()) {
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.search_does_not_exist", 
					new Object[] {this.translationFile.getName()}));
			return false;
		}
				
		try {
			Files.copy(this.sourceFile.toPath(), this.outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
			logOutput(e.getMessage());
			return false;
		}
				
		//parse files
		try {
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.checking_files"));
			
			Scanner searchLinesScanner = new Scanner(this.translationFile);

			LinkedList<LinkedList<String>> tempLL = new LinkedList<LinkedList<String>>();
			int minCols = 0;
			int maxCols = 0;
			
			//separating tsv values
			while (searchLinesScanner.hasNext()) {
				String[] stringsArrToAdd = searchLinesScanner.nextLine().split("\t");
				LinkedList<String> tempRow = new LinkedList<String>();
				int rowCount = 0;
				for (String str : stringsArrToAdd) {
					tempRow.add(str);
					rowCount++;
				}
				tempLL.add(tempRow);
				if (minCols == 0 || rowCount < minCols) minCols = rowCount;
				if (rowCount > maxCols) maxCols = rowCount;
			}
			searchLinesScanner.close();
			
			//if translation file is empty
			if (tempLL.size() == 0) {
				logOutput((ReplaceUI.getMessage("ReplaceUI.fileCheck.translation_file_empty")));
				return false;
			}
			
			//setting mode based on col count
			if (minCols == 2 && maxCols == 2) {
				this.mode = Mode.LANG;
			}
			else if ((minCols == 7 && maxCols == 7) || (minCols == 8 && maxCols == 8)) {//8 in case new eng variables are added
				this.mode = Mode.DESC;
			}
			else {
				logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.column_length_wrong", new Object[] {minCols, maxCols}));
				//ReplaceUI.filecheck.column_length_wrong: The translation file needs to have 2 columns 
				//in all rows for lang() translation or 7 columns in all rows for item description translation. 
				//The translation file column numbers ranged from {0} to {1}.
				return false;
			}
			
			
			//based on mode, place values into searchLI/replaceLI, or itemDI
			ListIterator<LinkedList<String>> tempIter = tempLL.listIterator();
			if (this.mode == Mode.LANG) {
				while (tempIter.hasNext()){
					LinkedList<String> tempRow = tempIter.next();
					this.searchLI.add(tempRow.get(0));
					this.replaceLI.add(tempRow.get(1));
							
				}
			}
			else if (this.mode == Mode.DESC) {
				while (tempIter.hasNext()){
					this.itemDI.add(tempIter.next());
				}
			}

			//parse source file
			Scanner sourceScanner = new Scanner(sourceFile);
			while (sourceScanner.hasNext()) {
				sourceLI.add(sourceScanner.nextLine());
			}
			sourceScanner.close();
			if (sourceLI.getSize() == 0) {
				logOutput((ReplaceUI.getMessage("ReplaceUI.fileCheck.source_file_empty")));
				return false;
			}
			
			logOutput(ReplaceUI.getMessage("ReplaceUI.fileCheck.files_checked", new Object[] {this.mode.toString()}));
			return true;
		} catch (Exception e) {
			logOutput(e.toString());
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




