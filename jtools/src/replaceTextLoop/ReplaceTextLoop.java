package replaceTextLoop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.*;


//output SUBSTITUTE
//for each dictionary entry, print
//output dictionary entries with prompt

//listener stuff:
//1. interface in child with method to be run in parent
//2. instantiate interface in child
//3. for each child in parent, assign them an interface instance and implement listener callback (parent's reaction)
//4. trigger child's listener somewhere. In child?

public class ReplaceTextLoop {	
	//holds input files
	private static LinkedList<String> sourceLL = new LinkedList<String>();
	private static LinkedList<String> searchLL = new LinkedList<String>();
	private static LinkedList<String> replaceLL = new LinkedList<String>();
	
	public static void replaceFiles(File sourceFile, File searchLinesFile, File replaceLinesFile, File outputFile) {
		//open the 3 files
		if (!sourceFile.exists() || sourceFile == null){
			listener.onLogOutput(ReplaceUI.getMessage("source_does_not_exist", 
					new Object[] {sourceFile.getName()}));
			return;
		}
		else if (!searchLinesFile.exists()) {
			listener.onLogOutput(ReplaceUI.getMessage("search_does_not_exist", 
					new Object[] {searchLinesFile.getName()}));
			return;
		}
		else if (!replaceLinesFile.exists()) {
			listener.onLogOutput(ReplaceUI.getMessage("replacement_does_not_exist", 
					new Object[] {replaceLinesFile.getName()}));
			return;
		}
		
		try {
			Files.copy(sourceFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
			listener.onLogOutput(e.getMessage());;
		}
		
		int searchLinesRows = 0;
		int replaceLinesRows = 0;
		
		//opening files, checking;
		try {
			Scanner replaceLinesScanner = new Scanner(replaceLinesFile);
			Scanner searchLinesScanner = new Scanner(searchLinesFile);
			
			//if number of lines in files don't match, stop	
			listener.onLogOutput(ReplaceUI.getMessage("checking_files"));
			while (replaceLinesScanner.hasNext()) {
				replaceLinesRows++;
				replaceLL.add(replaceLinesScanner.nextLine());
			}
			while (searchLinesScanner.hasNext()) {
				searchLinesRows++;
				searchLL.add(searchLinesScanner.nextLine());
			}
			searchLinesScanner.close();
			replaceLinesScanner.close();
			
			if (searchLinesRows == 0) {
				listener.onLogOutput(ReplaceUI.getMessage("search_file_empty"));
				return;
			}
			else if (searchLinesRows != replaceLinesRows) {
				listener.onLogOutput(ReplaceUI.getMessage("file_not_equal_length", 
						new Object[] {searchLinesRows, replaceLinesRows} ));
				return;
			}

			listener.onLogOutput(ReplaceUI.getMessage("files_checked"));
			
			
			//for every line N in LEFT:
			//if LEFT found in ORIGINAL, substitute with RIGHT, subCount++
			//if LEFT not found, search for RIGHT, alreadySubbedCount++
			//if RIGHT also not found, add N, LEFT to dictionary
			//print subCount, alreadySubbedCount
			listener.onLogOutput(ReplaceUI.getMessage("begin_replacement"));
			int subCount = 0;	//lines replaced
			int alreadySubbedCount = 0; //lines already replaced
			int notFoundCount = 0;	//neither searched lines nor replacement found
			Scanner sourceScanner = new Scanner(sourceFile);
			while (sourceScanner.hasNext()) {
				sourceLL.add(sourceScanner.nextLine());
			}
			sourceScanner.close();
			
			//TODO 0: index SOURCE for all "text" in every line
			
			
			//for each line in searchLines
			for (ListIterator<String> sourceIter = sourceLL.listIterator(); sourceIter.hasNext();) {
				
				
			}
			
			
			//old iteration
			while (searchLinesScanner.hasNext()) {
				String searchString = searchLinesScanner.nextLine();
				String replaceString = replaceLinesScanner.nextLine();
				
				
				//TODO from here...
//				source:
//					xxxx"text1"+VARXYZ   + "text2"xxx, "textA"xxx
//
//				search:
//					xxxx "text1" + var_123 + "text2" xxx, "textA" xxx
//
//				replace:
//					xxxx "text1" + VARXYZ + "text2" xxx, "text3" + VARXYZ + "text4" xxx
//
//				indexing:
//				1: index SEARCH: rawText, list of quotes, list of var_N, list of noVarNoSpaceText		--
//					list of quotes																		done
//					list of var_N			 															done
//					list of noVarNoSpaceText - compare with SOURCE's to find SOURCE vars				--
//					
//				2: index SOURCE: rawText, list of quotes 												--
//				3: index REPLACE: rawText, list of quotes, list of var_N								--
				
//				searching for line match:
//				4: SEARCHINDEX quoteList equal to SOURCEINDEX line?				--
//				5: --> all match:							
//					if list of var_N(M) not empty, for element in SEARCHINDEX, 
//						if current element is noVarNoSpaceText, subtract from SOURCE noSpaceText
//						if current element is var_N(M), from get from start till first ')', '+', ',', 
//							replace all instances of var_N(M) in REPLACE.rawText
//					//diff?
//					
//				replace each var_N with varXYZ, save in replaceVarRawText
//						put in SOURCE rawText from 
				
//				3: for each SOURCEINDEX, check if "text" count and contents match
//				4: --> match: for matches, find VARXYZ list
//				5: for each var_N, replace var_N with VARXYZ in REPLACE
//				6: from first char of first "text/var_N" in SOURCE to last char of last, replace with same of REPLACE

				
				
				
				
				
				
				Pattern pattern = Pattern.compile(searchString);
				boolean found = false;

				//look through source linked list copy, replace all matches
				//attempt 2 with iterator
				for (ListIterator<String> sourceIter = sourceLL.listIterator(); sourceIter.hasNext();) {
					Matcher matcher = pattern.matcher(sourceIter.next());
					if (matcher.find()) {
						found = true;
						sourceIter.set(matcher.replaceAll(replaceString));
						subCount++;
					}
				}
				

				//if not found, check if already replaced
				if (!found) {					
				
					for(String line : sourceLL) {
						Matcher matcher = pattern.matcher(line);
						if (matcher.find()) {
							found = true;
							alreadySubbedCount++;
						}	
					}
				}
		
				//if still no match
				if (!found) {
					listener.onLogOutput(ReplaceUI.getMessage("no_match", new Object[] {searchString}));
					notFoundCount++;
				}
			}
			
			
			//output to file
			PrintWriter printWriter = new PrintWriter(outputFile);
			boolean firstLine = true;
			for (String line : sourceLL) {
				if (firstLine) {
					firstLine = false;
				}
				else {
					printWriter.append("\n");
				}
				printWriter.append(line);
			}
			printWriter.close();

			replaceLinesScanner.close();
			searchLinesScanner.close();
			listener.onLogOutput(ReplaceUI.getMessage("replaced", new Object[] {subCount}));
			listener.onLogOutput(ReplaceUI.getMessage("already_replaced", new Object[] {alreadySubbedCount}));
			listener.onLogOutput(ReplaceUI.getMessage("not_found", new Object[] {notFoundCount}));
			
			} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

	
	}

	
	//1
	public interface LogInterface{
		public void onLogOutput(String msg);
	}
	
	//2
	private static LogInterface listener = null;
	
	public static void setLogOutputListener(LogInterface li) {
		listener = li;
	}
	
}

