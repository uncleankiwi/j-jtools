package replaceTextLoop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
	//list of chars to escape later
	public static List<String> noNoChars = Arrays.asList("\"", "{", "}", "(", ")", "+", ".");
	
	public static void replaceFiles(File sourceFile, File searchLinesFile, File replaceLinesFile, File outputFile) {
		//open the 3 files
		if (!sourceFile.exists() || sourceFile == null){
			listener.onLogOutput("Source file " + sourceFile.getName() + " does not exist.");
			return;
		}
		else if (!searchLinesFile.exists()) {
			listener.onLogOutput("Searched lines file " + searchLinesFile.getName() + " does not exist.");
			return;
		}
		else if (!replaceLinesFile.exists()) {
			listener.onLogOutput("Replacement lines file " + replaceLinesFile.getName() + " does not exist.");
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
			listener.onLogOutput("Checking files...");
			while (replaceLinesScanner.hasNext()) {
				replaceLinesRows++;
				replaceLinesScanner.nextLine();
			}
			while (searchLinesScanner.hasNext()) {
				searchLinesRows++;
				searchLinesScanner.nextLine();
			}
			if (searchLinesRows == 0) {
				listener.onLogOutput("Searched lines file is empty.");
				replaceLinesScanner.close();
				searchLinesScanner.close();
				return;
			}
			else if (searchLinesRows != replaceLinesRows) {
				listener.onLogOutput("Searched lines file length (" + searchLinesRows + 
						") and replaced lines file length (" + replaceLinesRows + ") aren't the same.");
				replaceLinesScanner.close();
				searchLinesScanner.close();
				return;
			}
			searchLinesScanner.close();
			replaceLinesScanner.close();
			listener.onLogOutput("Files checked.");
			
			
			//for every line N in LEFT:
			//if LEFT found in ORIGINAL, substitute with RIGHT, subCount++
			//if LEFT not found, search for RIGHT, alreadySubbedCount++
			//if RIGHT also not found, add N, LEFT to dictionary
			//print subCount, alreadySubbedCount
			listener.onLogOutput(("Beginning replacement..."));
			int subCount = 0;	//lines replaced
			int alreadySubbedCount = 0; //lines already replaced
			Scanner sourceScanner = new Scanner(sourceFile);
			searchLinesScanner = new Scanner(searchLinesFile);
			replaceLinesScanner = new Scanner(replaceLinesFile);
			LinkedList<String> sourceLL = new LinkedList<String>();
			
			while (sourceScanner.hasNext()) {
				sourceLL.add(sourceScanner.nextLine());
			}
			
			//for each line in searchLines
			while (searchLinesScanner.hasNext()) {
				String searchString = searchLinesScanner.nextLine();
				String replaceString = replaceLinesScanner.nextLine();
				
				//putting in escape characters
				searchString = escapeCharacters(searchString);
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
					pattern = Pattern.compile(replaceString);
					
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
					listener.onLogOutput("Match not found for: " + searchString);
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
			sourceScanner.close();
			replaceLinesScanner.close();
			searchLinesScanner.close();
			listener.onLogOutput("Replaced: " + subCount + "\nAlready replaced: " + alreadySubbedCount);

			} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

	
	}

	public static String escapeCharacters(String input) {
		//escapes characters so that they're not recognised by regex
		for (String oldStr : noNoChars) {
			input = input.replace(oldStr, "\\" + oldStr);
		}
		return input;
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

