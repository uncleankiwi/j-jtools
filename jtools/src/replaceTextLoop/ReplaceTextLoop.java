package replaceTextLoop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
		
		int searchLinesRows = 0;
		int replaceLinesRows = 0;
		
		int subCount = 0;	//lines replaced
		int alreadySubbedCount = 0; //lines already replaced
		
		//opening files, checking
		try {
			try(Scanner replaceLinesScanner = new Scanner(replaceLinesFile);
				Scanner searchLinesScanner = new Scanner(searchLinesFile);){
				
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
				System.out.println("Files checked.");
				
//				while(sourceScanner.hasNext()) {
//					searchLineCount++;
//					String inputLine = input.nextLine();
//					Matcher matcher = pattern.matcher(inputLine);
//					
//					while (matcher.find()) {
//						//adds every hit line by line to output file
//						output.write(matcher.group());
//						output.write("\n");
//						matchCount++;
//					}		
//					
//					
//				}
//				System.out.println(sourceFile.getName() + " length: " + sourceFile.length());
//				System.out.println("Lines parsed: " + lineCount);
//				System.out.println("Matches: " + matchCount);
				
				}
			} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		//for every line N in LEFT:
		//if LEFT found in ORIGINAL, substitute with RIGHT, subCount++
		//if LEFT not found, search for RIGHT, alreadySubbedCount++
		//if RIGHT also not found, add N, LEFT to dictionary
		//print subCount, alreadySubbedCount
		String replaceString = "";
		String currentLine = null;
		try {
			try(Scanner replaceLinesScanner = new Scanner(replaceLinesFile);
				Scanner searchLinesScanner = new Scanner(searchLinesFile);
				PrintWriter output = new PrintWriter(outFile);){
								
				System.out.println("Beginning replacement...");
				
				while (searchLinesScanner.hasNext()) {
					Pattern searchString = Pattern.compile(searchLinesScanner.nextLine());
					replaceString = replaceLinesScanner.nextLine();
					
					
					try (Scanner sourceScanner = new Scanner(sourceFile);){
						Matcher matcher = searchString.matcher(currentLine);
						while (sourceScanner.hasNext()) {
							currentLine = sourceScanner.nextLine();
							if (currentLine) {
								
							}
							
						}
					}
					



					System.out.println(searchString);
					System.out.println(replaceString);
					
				}
				
				System.out.println("Replaced.");
				
			}	
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();

		}
		
		
		
		
		
		
		
	
	}

}



//
//System.out.println("Enter source file to be opened:");
//System.out.println("ソースコードを指定してくだされ（出力ファイルは新しくセーブされるけどバックアップを取ってくだされ）");
//
//System.out.println("Enter file with lines to be replaced: ");
//System.out.println("差し替えられるテキストくれー");
//
//System.out.println("Enter file with line for replacing: ");
//System.out.println("差し替えテキスト:");
//
//System.out.println("");