//given a source file and regex, outputs a file with every match on separate lines
package extractRegexMatches;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.*;

//extracts lang(stuff) from source code for easy translation and later incorporation
public class LangToTsv {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//open source file
//		if (args.length != 3) {
//			System.out.println("Usage: java ExtractRegexMatches sourceFile outFile regex");
//			System.exit(0);
//		}
		

//		File sourceFile = new File(args[0]);
//		File outFile = new File(args[1]);	//this will override an existing file!
//		Pattern pattern = Pattern.compile(args[2]);
		
		//removing reliance on args
		File sourceFile = new File("source full 1995r zemi replaced.txt");
		File outFile = new File("sorry lang original.txt");
		Pattern pattern = Pattern.compile("lang\\(\".+\"\\)");
		final boolean PRINT_LINES_WITHOUT_LANG = false;
		OutputStream outStream;
		
		//lang(" ... ")		---> lang\\(\".+\"\\)
		//lang\\(\"
		//.+
		//\"\\)
		
		
		//lang(" ...		---> lang\\(\"[^\"]+
		
		
		if (!sourceFile.exists()){
			System.out.println("Source file " + sourceFile.getName() + " does not exist.");
			System.exit(0);
			
		}
		//find each instance of this regex
		int lineCount = 0;
		int matchCount = 0;
		try {
			try(Scanner input = new Scanner(sourceFile, "shift_jis");
				PrintWriter output = new PrintWriter(outFile);){
				while(input.hasNext()) {
					lineCount++;
					String inputLine = input.nextLine();
					Matcher matcher = pattern.matcher(inputLine);
					boolean found = false;
					while (matcher.find()) {
						//adds every hit line by line to output file
						String matchStr = matcher.group();
						if (matchStr.contains("Sorry, this is untranslated sentence")) {
							output.write(matchStr);
							output.write("\n");
							matchCount++;
							found = true;
						}

					}	

					if (PRINT_LINES_WITHOUT_LANG && !found){
						System.out.println("Line without lang: " + inputLine);
					}
					
					
				}
				System.out.println(sourceFile.getName() + " length: " + sourceFile.length());
				System.out.println("Lines parsed: " + lineCount);
				System.out.println("Matches: " + matchCount);
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
