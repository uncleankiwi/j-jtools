package replaceTextLoop;

import java.util.LinkedList;

public class Line {
	public String rawtext = "";
	public LinkedList<String> quoteList = new LinkedList<String>();
	public LinkedList<String> varList = new LinkedList<String>();
	
	public Line(String raw) {
		this.rawtext = raw;
	}
}
