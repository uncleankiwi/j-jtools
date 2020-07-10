package replaceTextLoop;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//holds one line of source/line to search/line to replace, with a list of stuff enclosed by quotation marks, and a list of
//variables in it
public class Line {
	private String rawtext = "";
	
	//for lang()
	private LinkedList<String> quoteList = new LinkedList<String>();
	private LinkedList<String> varList = new LinkedList<String>();
	private int quoteCount;
	private int varCount;
	
	//for descriptions:
	//private LinkedList<String> nonLangQuoteList = new LinkedList<String>();
	
	public Line(String raw) {
		this.rawtext = raw;
	}
	
	public void indexQuotes() {
		this.quoteCount = 0;
		this.quoteList = new LinkedList<String>();
		Pattern pattern = Pattern.compile("\"(.*?)(?<!\\\\)\"");	//search string
		//all in one quote				"  .+  "
		//includes escaped quotes		"  (.*?)  " 
		//ignores escaped quotes		"  (.*?)  (?<!\)  " - lookbehind
		Matcher matcher = pattern.matcher(this.rawtext);
		while (matcher.find()) {
			this.quoteCount++;
			this.quoteList.addLast(matcher.group());
		}
		
	}
	
	public void indexVars() {
		this.varCount = 0;
		this.varList = new LinkedList<String>();
		Pattern pattern = Pattern.compile("var_\\d+");	//search string
		Matcher matcher = pattern.matcher(this.rawtext);
		while (matcher.find()) {
			this.varCount++;
			this.varList.addLast(matcher.group());
		}
	}
	
	public boolean indexUnknownVars(Line searchLine) {
		this.varCount = 0;
		//no vars to get in this line
		if (searchLine.varCount == 0) return true;
		
		//get contents of lang()
		Pattern pattern = Pattern.compile("lang\\(.*\\)");
		Matcher matcher = pattern.matcher(this.rawtext);
		String sourceLangPart = "";
		if (matcher.find()) {
			sourceLangPart = matcher.group();
		}
		else {
			return false;
		}
		
		//remove spaces in both 
		String sourceNoSpaces = sourceLangPart.replace(" ", "");
		String searchNoSpaces = searchLine.getRaw().replace(" ", "");
				
		//substring with vars
		LinkedList<Integer> varStarts = new LinkedList<Integer>();
		LinkedList<Integer> varEnds = new LinkedList<Integer>();
		System.out.println(sourceNoSpaces);
		String searchNSTemp = searchNoSpaces;
		
		for (String var : searchLine.getVars()) {
			pattern = Pattern.compile(var);
			matcher = pattern.matcher(searchNSTemp);
			if (matcher.find()) {
				matcher.
			}
			else {
				return false;
			}
			
		}
		
		return true;
	}
	
	public int quoteCount() {
		return this.quoteCount;
	}
	
	public int varCount() {
		return this.varCount;
	}
	
	//check if order and contents of this line's quoteList is same as input string's
	public boolean tryReplace(LinkedList<String> inputList) {
		//TODO ?
		return this.quoteList.equals(inputList);
	}
	
	public static boolean quotesMatch(Line line1, Line line2) {
		return line1.getQuotes().equals(line2.getQuotes());
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
	
}
