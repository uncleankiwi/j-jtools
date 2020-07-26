package extractRegexMatches;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//lang() extracter with explicit encoding
public class LangToTsvEncoding {

	public static void main(String[] args) throws IOException {
		Path sourceFile = Paths.get("source full 1995r zemi replaced.txt");
		Path outFile = Paths.get("sorry lang original.txt");
		Pattern pattern = Pattern.compile("lang\\(\".+\"\\)");

		
		
		if (!Files.exists(sourceFile)){
			System.out.println("Source file " + sourceFile.getFileName() + " does not exist.");
			System.exit(0);
		}
		else {
			System.out.println("File opened.");
		}

		int lineCount = 0;
		int matchCount = 0;
		
		List<String> sourceList = Files.readAllLines(sourceFile, Charset.forName("windows-31j"));
		LinkedList<String> outList = new LinkedList<String>();
		
		for (String str : sourceList) {
			lineCount++;
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				//adds every hit line by line to output file
				String matchStr = matcher.group();
				if (matchStr.contains("Sorry, this is untranslated sentence")) {
					outList.add(matchStr);
					matchCount++;
				}

			}	
			
			
		}
		
		Files.write(outFile, outList, Charset.forName("windows-31j"));
		
		
		System.out.println(sourceFile.getFileName() + " length: " + Files.size(sourceFile));
		System.out.println("Lines parsed: " + lineCount);
		System.out.println("Matches: " + matchCount);
		

	}

}
