package replaceTextLoop;

import java.util.LinkedList;
import java.util.ListIterator;


//a collection of Line objects. equivalent to the entire source code text file, or a search/replace text file.
//non-lang() searches and replacements are NOT these
public class LinesIndex {
	private LinkedList<Line> LI = new LinkedList<Line>();
	private static LogInterface logListener;
	
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
	
	public ListIterator<Line> listIterator() {
		return LI.listIterator();
	}
	
	public static LinesIndex replaceLoop(LinesIndex searchIndex, LinesIndex sourceIndex, LinesIndex replaceIndex) {
		int totalReplacedCount = 0;	//number of sourceIndex lines replaced
		int replacedCount = 0; //searchIndex lines that have had at least 1 replacement
		int alreadyReplacedCount = 0;
		int noMatchCount = 0;
		
		searchIndex.indexQuotes();
		searchIndex.indexVars();
		
		replaceIndex.indexVars();
		replaceIndex.indexQuotes();
		
		sourceIndex.indexQuotes();
		
		
		System.out.println("source print...");
		sourceIndex.print();//TODO
		System.out.println("search print...");
		searchIndex.print();
		System.out.println("replace print...");
		replaceIndex.print();
		
		//for each SEARCHINDEX line
		ListIterator<Line> searchIter = searchIndex.listIterator();
		ListIterator<Line> replaceIter = replaceIndex.listIterator();
		while (searchIter.hasNext()) {
			int lineNumber = searchIter.nextIndex();
			Line searchLine = searchIter.next();
			Line replaceLine = replaceIter.next();
			
			boolean found = false;	//is this line present at least once in entire source?
			boolean alreadyReplaced = false;	//was this line not found, but its replacement found at least once?
			
			//for each SOURCEINDEX line
			ListIterator<Line> sourceIter = sourceIndex.listIterator();
			while (sourceIter.hasNext()) {
				Line sourceLine = sourceIter.next();
				
				//if match SOURCEINDEX line
				if (Line.quotesMatch(searchLine, sourceLine)) {
					boolean pass = true;
					
					//find SOURCEINDEX vars
					pass = sourceLine.indexUnknownVars(searchLine);
					
					//replace REPLACEINDEX's line's vars
					if (pass) {
						pass = Line.tryReplace(searchLine, replaceLine, sourceLine);
					}
					else {
						logOutput(ReplaceUI.getMessage("LinesIndex.fail_line_replace", new Object[] {
								lineNumber + 1, searchLine.varCount(), replaceLine.varCount(), searchLine.getRaw()}));
						//Search line {0} variable count ({1}) does not match source variable count ({2}). Line: {3}
					}
					
		 			//replace SOURCEINDEX line's lang with REPLACEINDEX line
					if (pass) {
						sourceLine.setRaw(replaceLine.getRaw());
						sourceLine.setQuotes(replaceLine);
						found = true;
						totalReplacedCount++;
					}
				}	
			}
			
			
			//if no matches, search for replaced line
			if (!found) {
				//for each SOURCEINDEX line
				sourceIter = sourceIndex.listIterator();
				while (sourceIter.hasNext()) {
					Line sourceLine = sourceIter.next();
					if (Line.quotesMatch(replaceLine, sourceLine)) {
						alreadyReplaced = true;
					}
				}
			}
			
			if (found) {
				//this search line was found at least once in entire source file
				replacedCount++;
			}
			else if (alreadyReplaced) {
				alreadyReplacedCount++;
			}
			else {
				//if neither search line nor replacement were found, display in log
				noMatchCount++;
				logOutput(ReplaceUI.getMessage("LinesIndex.no_match", new Object[] {lineNumber + 1, searchLine.getRaw()}));

			}
			
		}
		
		logOutput((ReplaceUI.getMessage("LinesIndex.replaced", new Object[] {replacedCount, searchIndex.LI.size(), totalReplacedCount})));
		logOutput(ReplaceUI.getMessage("LinesIndex.already_replaced", new Object[] {alreadyReplacedCount, searchIndex.LI.size()}));
		logOutput(ReplaceUI.getMessage("LinesIndex.not_found", new Object[] {noMatchCount, searchIndex.LI.size()}));
		return sourceIndex;
	}
	
	public static void setLogOutputListener(LogInterface newListener) {
		logListener = newListener;
	}
	
	private static void logOutput(String msg) {
		if (logListener != null) {
			logListener.onLogOutput(msg);
		}
	}
	
	public int getSize() {
		return this.LI.size();
	}
	
	public void print() {	//temp
		System.out.println("Index size: " + this.getSize());
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
