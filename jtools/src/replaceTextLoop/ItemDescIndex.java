package replaceTextLoop;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//each ItemDesc in this holds the descriptions for 1 item
public class ItemDescIndex {
	private LinkedList<ItemDesc> idi = new LinkedList<ItemDesc>();
	private static LogInterface logListener; //log stuff
	private final static int NUMBER_OF_DESC = 4;	//number of descriptions in Japanese version

	public void add(String newItem) {
		idi.add(new ItemDesc(newItem));
	}
	
	public void add(LinkedList<String> newItem) {
		idi.add(new ItemDesc(newItem));
	}
	
	public ListIterator<ItemDesc> listIterator(){
		return idi.listIterator();
	}
	
	public static LinesIndex replaceLoop(LinesIndex sourceIndex, ItemDescIndex itemDescIndex) {
		int replacedCount = 0;
		int noMatchCount = 0;
		sourceIndex.indexQuotes();
		
		ListIterator<ItemDesc> itemIter = itemDescIndex.listIterator();
		
		//for each itemDescIndex
		while (itemIter.hasNext()) {
			int lineNumber = itemIter.nextIndex();
			ItemDesc item = itemIter.next();
			ListIterator<String> descIter = item.listIterator();
			String currentDesc = "";
			if (descIter.hasNext()) {
				currentDesc = descIter.next();
			}
			int descsFound = 0;	//number of descriptions for this item found. will start replacing when >=5. (i.e. english only)
			int linesAfterFirstFound = 0;	//number of source lines looked through after foundFirstDesc. will quit search if >15
			
			//for each sourceIndex line
			ListIterator<Line> sourceIter = sourceIndex.listIterator();
			while (sourceIter.hasNext()) {
				Line sourceLine = sourceIter.next();
				
				if (descsFound < NUMBER_OF_DESC) {	//looking for japanese texts
					if (sourceLine.getRaw().contains(currentDesc)) {
						descsFound++;
						if (descIter.hasNext()) {
							currentDesc = descIter.next();
						}
						else {
							replacedCount++;
							break;
						}
					}
				}
				else {	//looking for quotation marks where english texts should be, and replacing
					Pattern pattern = Pattern.compile("\"(.*?)(?<!\\\\)\"");
					Matcher matcher = pattern.matcher(sourceLine.getRaw());
					if (matcher.find()) {
						descsFound++;
						//sourceLine.setRaw(sourceLine.getRaw().replaceAll("\"(.*?)(?<!\\\\)\"", currentDesc));
						sourceLine.setRaw(matcher.replaceAll((currentDesc.replaceAll("\\\\", "\\\\\\\\"))));
						if (descIter.hasNext()) {
							currentDesc = descIter.next();
						}
						else {
							replacedCount++;
							break;
						}
					}
				}
				

				//linesAfterFirstFound++
				//if linesAfterFirstFound > 15, break	--> ItemDescIndex.fail_line_replace: Line {0} matched but could not replace: {1}
				if (descsFound > 0) linesAfterFirstFound++;
				if (linesAfterFirstFound > 15) {
					logOutput(ReplaceUI.getMessage("ItemDescIndex.fail_line_replace", new Object[] {lineNumber + 1, item.getFirstDesc()}));
					break;
				}
			}
			
			//if no matches,
				//noMatchCount++	-->ItemDescIndex.no_match: Line {0} not found: {1}
			if (descsFound == 0) {
				noMatchCount++;
				logOutput(ReplaceUI.getMessage("ItemDescIndex.no_match", new Object[] {lineNumber + 1, item.getFirstDesc()}));
			}
		}
		
		logOutput(ReplaceUI.getMessage("ItemDescIndex.replaced", new Object[] {replacedCount, itemDescIndex.idi.size()}));
		logOutput(ReplaceUI.getMessage("ItemDescIndex.not_found", new Object[] {noMatchCount, itemDescIndex.idi.size()}));
		return sourceIndex;
	}
	
	
	//log stuff
	public static void setLogOutputListener(LogInterface newListener) {
		logListener = newListener;
	}
	private static void logOutput(String msg) {
		if (logListener != null) {
			logListener.onLogOutput(msg);
		}
	}
	
	private class ItemDesc{
		private LinkedList<String> descriptions = new LinkedList<String>();
		
		private ItemDesc(String newItem){
			String[] descToAdd = newItem.split("\t");
			for (String str : descToAdd) {
				this.descriptions.add(str);
			}
		}
		
		private ItemDesc(LinkedList<String> newItem) {
			ListIterator<String> iter = newItem.listIterator();
			while (iter.hasNext()) {
				this.descriptions.add(iter.next());
			}
		}
		
		public String getFirstDesc() {
			return this.descriptions.get(0);
		}
		
		public ListIterator<String> listIterator(){
			return descriptions.listIterator();
		}
	}
}
