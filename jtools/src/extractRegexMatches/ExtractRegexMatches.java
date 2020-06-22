//given a source file and regex, outputs a file with every match on separate lines
package extractRegexMatches;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.*;

public class ExtractRegexMatches {

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
		File sourceFile = new File("zemi1.99parsed english.txt");
		File outFile = new File("zemi1.99parsed english-OJ.txt");
		Pattern pattern = Pattern.compile("lang\\(\"[^\"]+");
		
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
			try(Scanner input = new Scanner(sourceFile);
				PrintWriter output = new PrintWriter(outFile);){
				while(input.hasNext()) {
					lineCount++;
					String inputLine = input.nextLine();
					Matcher matcher = pattern.matcher(inputLine);
					while (matcher.find()) {
						//adds every hit line by line to output file
						output.write(matcher.group());
						output.write("\n");
						matchCount++;
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
