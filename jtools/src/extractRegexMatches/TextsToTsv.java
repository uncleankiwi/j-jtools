package extractRegexMatches;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

//combines 2 text files into a .tsv
public class TextsToTsv {
	
	public static void main(String[] args) {
		File file1 = new File("zemi1.99parsed.txt");
		File file2 = new File("zemi1.99parsed english.txt");
		File fileOut = new File("zemi1.99combined.txt");
		boolean firstLine = true;
		
		try {
			Scanner scan1 = new Scanner(file1);
			Scanner scan2 = new Scanner(file2);
			PrintWriter printOut = new PrintWriter(fileOut);
			while(scan1.hasNext()) {
				if (firstLine) {
					firstLine = false;
				}
				else {
					printOut.write("\n");
				}
				printOut.write(scan1.nextLine() + "\t" + scan2.nextLine());	
			}
			System.out.println("Done");
			printOut.close();
			scan1.close();
			scan2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}

}
