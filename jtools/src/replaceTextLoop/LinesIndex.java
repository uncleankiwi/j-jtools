package replaceTextLoop;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public class LinesIndex {
	private LinkedList<Line> LI = new LinkedList<Line>();
	
	public void add(String str) {
		LI.addLast(new Line(str));
	}
	
	public void indexQuotes() {
		ListIterator<Line> iter = LI.listIterator();
		while (iter.hasNext()) {
			Line line = iter.next();
			line.indexQuotes();
		}
	}
	
	//run only on SEARCHINDEX and REPLACEINDEX. won't work on SOURCEINDEX
	public void indexVars() {
		ListIterator<Line> iter = LI.listIterator();
		while (iter.hasNext()) {
			Line line = iter.next();
			line.indexVars();
		}
	}
	
	public String replaceLoop(LinesIndex sourceIndex, LinesIndex replaceIndex) {
		int replacedCount = 0;
		int alreadyReplacedCount = 0;
		int noMatchCount = 0;
		
		
	/*

	
		TODO dsf 
		given SOURCEINDEX, REPLACEINDEX
			for each line, 
				1. check each sourceIndex quote list to see if equal
				2. quotes equal: 
				for every quote list match found
					create SOURCEINDEX line varList
					if this line's varList.count != SOURCEINDEX's line's varList.count, go to next quote list match
						if SEARCHINDEX line.varList.count != 0
							for var in each SOURCEINDEX line.varList
								search REPLACEINDEX line's rawText with SEARCHINDEX line var at same index, replace with this var
						replace
		
				if no matches for this line
					search 
		*/	
		
		return "Replaced: " + replacedCount + 
				"\nAlready replaced: " + alreadyReplacedCount +
				"\nNo matches: " + noMatchCount;
	}
	
	public void print() {	//temp
		ListIterator<Line> iter = LI.listIterator();
		while (iter.hasNext()) {
			System.out.println("Line index: " + iter.nextIndex());
			Line line = iter.next();
			System.out.println("Quotes: " + line.quoteCount() + " Vars: " + line.varCount());
			LinkedList<String> quotes = line.getQuotes();
			LinkedList<String> vars = line.getVars();
			
			ListIterator<String> iterQuotes = quotes.listIterator();
			ListIterator<String> iterVars = vars.listIterator();
			while (iterQuotes.hasNext()) {
				System.out.println("[Quote " + iterQuotes.nextIndex() + "] " + iterQuotes.next());
			}
			while (iterVars.hasNext()) {
				System.out.println("[Var " + iterVars.nextIndex() + "]" + iterVars.next());
			}
			
		}	
	}
}
