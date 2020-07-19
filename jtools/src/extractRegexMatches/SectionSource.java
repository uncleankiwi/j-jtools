package extractRegexMatches;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

//just grabs a few lines of code...
public class SectionSource {
	static int startLine = 69605;
	static int endLine = 148417;
	
	public static void main(String[] args) {
		File sourceFile = new File("1.995Rs");
		File outFile = new File("1.99IDsource.txt");
		LinkedList<String> outLL = new LinkedList<String>();
		int currLine = 0;
		String sourceLine = "";
		try {
			Scanner sourceScan = new Scanner(sourceFile);
			while (sourceScan.hasNext()) {
				sourceLine = sourceScan.nextLine();
				currLine++;
				if (currLine >= startLine && currLine <=endLine) {
					outLL.add(sourceLine);
				}
				else if (currLine > endLine) {
					break;
				}
				
			}
			
			
			PrintWriter outPrint = new PrintWriter(outFile);
			ListIterator<String> outIter = outLL.listIterator();
			boolean firstLine = true;
			while (outIter.hasNext()) {
				if (firstLine) firstLine = false;
				else {
					outPrint.write("\n");
				}
				outPrint.write(outIter.next());
			}
			
			sourceScan.close();
			outPrint.close();
			System.out.println("Code sectioning complete. Rows produced: " + outLL.size());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
