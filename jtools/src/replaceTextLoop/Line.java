package replaceTextLoop;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//holds one line of source/line to search/line to replace, with a list of stuff enclosed by quotation marks, and a list of
//variables in it
public class Line {
	private String rawtext = "";
	private LinkedList<String> quoteList = new LinkedList<String>();
	private LinkedList<String> varList = new LinkedList<String>();
	private int quoteCount;
	private int varCount;
	
	public Line(String raw) {
		this.rawtext = raw;
	}
	
	public void indexQuotes() {
		quoteCount = 0;
		quoteList = new LinkedList<String>();
		Pattern pattern = Pattern.compile("\"(.*?)(?<!\\\\)\"");	//search string
		//all in one quote				"  .+  "
		//includes escaped quotes		"  (.*?)  " 
		//ignores escaped quotes		"  (.*?)  (?<!\)  " - lookbehind
		Matcher matcher = pattern.matcher(rawtext);
		while (matcher.find()) {
			quoteCount++;
			quoteList.addLast(matcher.group());
		}
		
	}
	
	public void indexVars() {
		varCount = 0;
		varList = new LinkedList<String>();
		Pattern pattern = Pattern.compile("var_\\d+");	//search string
		Matcher matcher = pattern.matcher(rawtext);
		while (matcher.find()) {
			varCount++;
			varList.addLast(matcher.group());
		}
	}
	
	public int quoteCount() {
		return this.quoteCount;
	}
	
	public int varCount() {
		return this.varCount;
	}
	
	//check if order and contents of this line's quoteList is same as input string's
	public boolean tryReplace(LinkedList<String> inputList) {
		
		return this.quoteList.equals(inputList);
	}

	public LinkedList<String> getQuotes(){
		return quoteList;	//temp
	}
	
	public LinkedList<String> getVars(){
		return varList;	//temp
	}
	
}
