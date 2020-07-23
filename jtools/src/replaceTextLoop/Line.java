package replaceTextLoop;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//holds one line of source code/line to search/line to replace, with a list of stuff enclosed by quotation marks,
//and a list of variables in it. Also some utility static methods for interacting with other Lines
public class Line {
	private String rawtext = "";
	
	//for lang()
	private LinkedList<String> quoteList = new LinkedList<String>();
	private LinkedList<String> varList = new LinkedList<String>();
	private int quoteCount;
	private int varCount;
	public static List<String> noNoChars = Arrays.asList("\"", "{", "}", "(", ")", ".", "+", "*");
	
	public Line(String raw) {
		this.rawtext = raw;
	}
	
	//gets a list of ONLY the quotes that are within lang()
	public void indexQuotes() {
		this.quoteCount = 0;
		this.quoteList = new LinkedList<String>();
		Pattern pattern = Pattern.compile("lang\\(\".+\"\\)");	//lang() search string
		Matcher matcher = pattern.matcher(this.rawtext);
		while (matcher.find()) {
			String langStr = matcher.group();
			pattern = Pattern.compile("\"(.*?)(?<!\\\\)\"");	//quotes search string
			//all in one quote				"  .+  "
			//includes escaped quotes		"  (.*?)  " 
			//ignores escaped quotes		"  (.*?)  (?<!\)  " - lookbehind
			matcher = pattern.matcher(langStr);
			while (matcher.find()) {
				this.quoteCount++;
				this.quoteList.addLast(matcher.group());
			}
		}

		
	}
	
	//again, gets a list of ONLY variables that are within lang()
	public void indexVars() {
		this.varCount = 0;
		this.varList = new LinkedList<String>();
		Pattern pattern = Pattern.compile("lang\\(\".+\"\\)");	//lang() search string
		Matcher matcher = pattern.matcher(this.rawtext);
		while (matcher.find()) {
			String langStr = matcher.group();
			pattern = Pattern.compile("var_\\d+");	//search string
			matcher = pattern.matcher(langStr);
			while (matcher.find()) {
				this.varCount++;
				this.varList.addLast(matcher.group());
			}
		}
	}
	
	//find and populate the list of variables in a line of source code, given an indexed search line
	public boolean indexUnknownVars(Line searchLine) {
		this.varCount = 0;
		//no vars to get in this line
		if (searchLine.varCount == 0) return true;
		
		//get contents of lang()
		Pattern pattern = Pattern.compile("lang\\(.*\\)");
		Matcher matcher = pattern.matcher(this.rawtext);
		String sourceLangPart = "";
		if (matcher.find()) {
			while (matcher.find()) {
				sourceLangPart = matcher.group();
			}
		}
		else {
			return false;
		}
		
		//remove spaces in both 
		String sourceNoSpaces = sourceLangPart.replace(" ", "");
		String searchNoSpaces = searchLine.getRaw().replace(" ", "");
				
		//substring with vars
		LinkedList<Integer> searchVarStarts = new LinkedList<Integer>();
		LinkedList<Integer> searchVarEnds = new LinkedList<Integer>();
		LinkedList<String> nonVarList = new LinkedList<String>();
		LinkedList<Integer> sourceNonVarStarts = new LinkedList<Integer>();
		LinkedList<Integer> sourceNonVarEnds = new LinkedList<Integer>();
		int searchStartPos = 0;
		
		//string0 var0 string1 var1 string2
		//1. find searchVarStart, searchVarEnd in searchNoSpaces
		for (String var : searchLine.getVars()) {
			pattern = Pattern.compile(var);
			matcher = pattern.matcher(searchNoSpaces);
			if (matcher.find(searchStartPos)) {
				searchVarStarts.add(matcher.start());
				searchVarEnds.add(matcher.end());
				searchStartPos = matcher.end() + 1;
			}
			else {
				return false;
			}
		}
		
		//2. get nonVarList by substring searchNoSpaces: searchVarStart 0, searchVarStart - 1
		//		...searchVarEnd + 1, searchVarStart - 1... searchVarEnd + 1, searchNoSpaces.length()
		for (int i = 0; i < searchLine.varCount() + 1; i++) {	//assuming there are varCount + 1 nonVar
			int nonVarStart;
			int nonVarEnd;
			if (i == 0) {
				nonVarStart = 0;
			}
			else {
				nonVarStart = searchVarEnds.get(i - 1);
			}
			
			if (i == searchLine.varCount) {
				nonVarEnd = searchNoSpaces.length() - 1;
			}
			else {
				nonVarEnd = searchVarStarts.get(i);
			}
			
			nonVarList.add(searchNoSpaces.substring(nonVarStart, nonVarEnd));
		}		
		
		//string0 var0 string1 var1 string2
		//3. find sourceNonVarStart, sourceNonVarEnd in sourceNoSpaces
		searchStartPos = 0;
		for (String nonVar : nonVarList) {
			pattern = Pattern.compile(Line.escapeCharacters(nonVar));
			matcher = pattern.matcher(sourceNoSpaces);
			if (matcher.find(searchStartPos)) {
				sourceNonVarStarts.add(matcher.start());
				sourceNonVarEnds.add(matcher.end());
				searchStartPos = matcher.end() + 1;
			}
			else {
				return false;
			}
		}	
		
		//4. populate varlist by substring sourceNonVarEnd + 1, sourceNonVarStart[+1] - 1	
		this.varCount = 0;
		this.varList = new LinkedList<String>();
		for (int i = 0; i < searchLine.varCount(); i++) {
			this.varList.add(sourceNoSpaces.substring(
					sourceNonVarEnds.get(i), sourceNonVarStarts.get(i + 1)));
			this.varCount++;
		}
		
		return true;
	}
	
	public int quoteCount() {
		return this.quoteCount;
	}
	
	public int varCount() {
		return this.varCount;
	}
			
	//search replace line with searchLine's varList, replace with sourceLine's varList
	public static boolean tryReplace(Line searchLine, Line replaceLine, Line sourceLine) {
		if (searchLine.varCount() != sourceLine.varCount()) {
			return false;
		}
		else {
			ListIterator<String> searchVarIter = searchLine.getVars().listIterator();
			ListIterator<String> sourceVarIter = sourceLine.getVars().listIterator();
			while (searchVarIter.hasNext()) {
				String searchVar = searchVarIter.next();
				String sourceVar = sourceVarIter.next();
				replaceLine.setRaw(replaceLine.getRaw().replace(searchVar, sourceVar));
			}
		}
		return true;
	}
	
	public static boolean quotesMatch(Line line1, Line line2) {
		return line1.getQuotes().equals(line2.getQuotes());
	}
	
	public void setRaw(String newRaw) {
		this.rawtext = newRaw;
	}
	public void setQuotes(Line newLine) {
		this.quoteList = newLine.getQuotes();
	}

	public LinkedList<String> getQuotes(){
		return quoteList;
	}
	
	public LinkedList<String> getVars(){
		return varList;
		
	}
	
	public String getRaw() {
		return this.rawtext;
	}
	
	public static String escapeCharacters(String input) {
		//escapes characters so that they're not recognised by regex
		for (String oldStr : noNoChars) {
			input = input.replace(oldStr, "\\" + oldStr);
		}
		return input;
	}
	
}
