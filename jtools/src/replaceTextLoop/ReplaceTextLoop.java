package replaceTextLoop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.*;


//output SUBSTITUTE
//for each dictionary entry, print
//output dictionary entries with prompt

public class ReplaceTextLoop {

	public static void main(String[] args) {
		//open 3 files: source, searchlines, replacelines
		File sourceFile = new File("src.txt");
		File searchLinesFile = new File("searchL.txt");
		File replaceLinesFile = new File("replaceL.txt");
		
		if (!sourceFile.exists() || sourceFile == null){
			System.out.println("Source file " + sourceFile.getName() + " does not exist.");
			System.exit(0);
		}
		else if (!searchLinesFile.exists()) {
			System.out.println("Searched lines file " + searchLinesFile.getName() + " does not exist.");
			System.exit(0);
		}
		else if (!replaceLinesFile.exists()) {
			System.out.println("Replacement lines file " + replaceLinesFile.getName() + " does not exist.");
			System.exit(0);
		}
		
		File outFile = new File(sourceFile.getName() + " out");
		try {
			Files.copy(sourceFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		int searchLinesRows = 0;
		int replaceLinesRows = 0;
		
		//opening files, checking;
		try {
			Scanner replaceLinesScanner = new Scanner(replaceLinesFile);
			Scanner searchLinesScanner = new Scanner(searchLinesFile);
			
			//if number of lines in files don't match, stop	
			System.out.println("Checking files...");
			while (replaceLinesScanner.hasNext()) {
				replaceLinesRows++;
				replaceLinesScanner.nextLine();
			}
			while (searchLinesScanner.hasNext()) {
				searchLinesRows++;
				searchLinesScanner.nextLine();
			}
			if (searchLinesRows == 0) {
				System.out.println("Searched lines file is empty.");
				System.exit(0);
			}
			else if (searchLinesRows != replaceLinesRows) {
				System.out.println("Searched lines file length (" + searchLinesRows + 
						") and replaced lines file length (" + replaceLinesRows + ") aren't the same.");
				System.exit(0);
			}
			searchLinesScanner.close();
			replaceLinesScanner.close();
			System.out.println("Files checked.");
			
			
			//for every line N in LEFT:
			//if LEFT found in ORIGINAL, substitute with RIGHT, subCount++
			//if LEFT not found, search for RIGHT, alreadySubbedCount++
			//if RIGHT also not found, add N, LEFT to dictionary
			//print subCount, alreadySubbedCount
			System.out.println("Beginning replacement...");
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
				Pattern pattern = Pattern.compile(searchString);
								
				boolean found = false;

				//look through source linked list copy, replace all matches
				for(String line : sourceLL) {
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {
						found = true;
//						System.out.println("Found match for: " + matcher.group());
//						System.out.println("in the line: " + currentLine);
						line = matcher.replaceAll(replaceString);
						subCount++;
					}
				};
				
				//if not found, check if already replaced
				if (!found) {					
					pattern = Pattern.compile(replaceString);
					
					for(String line : sourceLL) {
						Matcher matcher = pattern.matcher(line);
						if (matcher.find()) {
							found = true;
							alreadySubbedCount++;
							//System.out.println("Already replaced with: " + matcher.group());
							//System.out.println("in the line: " + currentLine);
						}	
					}
				}
		
				//if still no match
				if (!found) {
					System.out.println("Match not found for: " + searchString);
				}
			}
			
			
			//output to file
			PrintWriter printWriter = new PrintWriter(outFile);
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
			System.out.println("Replaced: " + subCount + "\nAlready replaced: " + alreadySubbedCount);

			} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

	
	}

}

