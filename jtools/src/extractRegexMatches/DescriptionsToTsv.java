package extractRegexMatches;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//grabs item descriptions from startLine to endLine, puts each item into separate tsv columns
public class DescriptionsToTsv {
	final static int TSV_COL_COUNT = 7;
	static int startLine = 1;	//69605;
	static int endLine = 215;	//148417;
	final static String VAR_STRING = "var_446";
	
	
	public static void main(String[] args) {
		File sourceFile = new File("1.99IDsource eng.txt");
		File outFile = new File("IDI test.txt");
		LinkedList<String> outLL = new LinkedList<String>();
		try {
			Scanner sourceScan = new Scanner(sourceFile);
			int currLine = 0;
			int currentCol = 1;	//goes from 1 to TSV_COL_COUNT
			String sourceLine = "";
			String outLine = "";
			Pattern pattern = Pattern.compile("\"(.*?)(?<!\\\\)\"");	//lang() search string
			Matcher matcher;
			while (sourceScan.hasNext()) {
				currLine++;
				sourceLine = sourceScan.nextLine();
				if (currLine >= startLine && currLine <= endLine) {
					//if this line contains VAR_STRING, add quotes to column
					if (sourceLine.contains(VAR_STRING)){
						if (currentCol != 1) outLine += "\t";
						//"\"(.*?)(?<!\\\\)\""
						matcher = pattern.matcher(sourceLine);
						if (matcher.find()) {
							outLine += matcher.group();
						}

						//determining next column
						if (currentCol == TSV_COL_COUNT) {
							outLL.add(outLine);
							outLine = "";
							currentCol = 1;
						}
						else currentCol++;
					}
				}
				else if (currLine > endLine) break;
			}
			
			
			PrintWriter outPrint = new PrintWriter(outFile);
			ListIterator<String> outLLIter = outLL.listIterator();
			boolean firstLine = true;
			while (outLLIter.hasNext()) {
				if (firstLine) firstLine = false;
				else outPrint.write("\n");
				outPrint.write(outLLIter.next());
			}
			
			System.out.println("SourceToTsv done. Final line: " + currLine);
			outPrint.close();
			sourceScan.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		
	}

}
