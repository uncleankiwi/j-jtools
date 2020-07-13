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

		//for each SEARCHINDEX line
		ListIterator<Line> searchIter = searchIndex.listIterator();
		ListIterator<Line> replaceIter = replaceIndex.listIterator();
		while (searchIter.hasNext()) {
			int lineNumber = searchIter.nextIndex();
			Line searchLine = searchIter.next();
			Line replaceLine = replaceIter.next();
			
			boolean found = false;	//is this line present at least once in entire source?
			
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
						logOutput("Failed to replace variables in replace line. Search line variable count (" +
								searchLine.varCount() + ") does not match source variable count (" + replaceLine.varCount() +
								")");//TODO
					}
					
		 			//replace SOURCEINDEX line's lang with REPLACEINDEX line
					if (pass) {
						sourceLine.setRaw(replaceLine.getRaw());
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
						alreadyReplacedCount++;
						found = true;;
					}
				}
			}
			
			if (found) {
				//this search line was found at least once in entire source file
				replacedCount++;
			}
			else {
				//if neither search line nor replacement were found, display in log
				noMatchCount++;
				logOutput(ReplaceUI.getMessage("LinesIndex.no_match", new Object[] {lineNumber, searchLine.getRaw()}));

			}
			
		}
		
	/*
	 
	 	for each SEARCHINDEX line,
	 		for each SOURCEINDEX line
		 		if match SOURCEINDEX line
		 			find SOURCEINDEX vars
		 			replace REPLACEINDEX's line's vars
		 			replace SOURCEINDEX line with REPLACEINDEX line	 		
	 		if no matches,
	 			for each SOURCEINDEX line
	 				if match equivalent REPLACEINDEX line, alreadyReplacedCount++
	 		
	 
	 
		given SOURCEINDEX, REPLACEINDEX
			for each SEARCHINDEX line, 
				1. check each sourceIndex quote list to see if equal
				2. quotes equal: 
					0. found = false
					1. find sourceIndex line varList
					2. check if varList = sourceIndex line varList
					3. replace replaceIndex line rawText's vars. search this.varList, replace with sourceIndex VarList
					4. replace sourceIndex line's rawText with replaceIndex line's rawText
					5. totalReplacedCount++, found = true
				?. if found = true, replacedCount++
				3. no match:
					check each sourceIndex quote list with replaceIndex quoteList to see if equal
					quotes equal: 
						alreadyReplacedCount++
					
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
		
		logOutput((ReplaceUI.getMessage("LinesIndex.replaced", new Object[] {replacedCount, searchIndex.LI.size(), totalReplacedCount})));
		logOutput(ReplaceUI.getMessage("LinesIndex.already_replaced", new Object[] {alreadyReplacedCount}));
		logOutput(ReplaceUI.getMessage("LinesIndex.not_found", new Object[] {noMatchCount}));
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
