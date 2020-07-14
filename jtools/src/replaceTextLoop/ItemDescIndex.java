package replaceTextLoop;

import java.util.LinkedList;
import java.util.ListIterator;

//each ItemDesc in this holds the descriptions for 1 item
public class ItemDescIndex {
	private LinkedList<ItemDesc> idi = new LinkedList<ItemDesc>();

	public void add(String newItem) {
		idi.add(new ItemDesc(newItem));
	}
	
	public ListIterator<ItemDesc> listIterator(){
		return idi.listIterator();
	}
	
	public static LinesIndex replaceLoop(LinesIndex sourceIndex, ItemDescIndex itemDescIndex) {
		//TODO
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
			boolean foundFirstDesc = false;	//has the first description of this item been found in the source yet?
			boolean foundLastDesc = false; //has the last desc of this item been found yet?
			int linesAfterFirstFound = 0;	//number of source lines looked through after foundFirstDesc. will quit search if >15
			
			//for each sourceIndex line
			ListIterator<Line> sourceIter = sourceIndex.listIterator();
			while (sourceIter.hasNext()) {
				//if match first item in searchDescIndex
				Line sourceLine = sourceIter.next();
				
				if (sourceLine.getRaw().contains(currentDesc)) {
					
				}
				//this source line contains currentDesc:
					//if foundFirstDesc:
						//replace
				
					//else:
						//
				
					//currentDesc = descIter.next()
				//this source line does not have currentDesc:
					//
				//linesAfterFirstFound++
				//if linesAfterFirstFound > 15, break
				
				//start replacing the next quotes with the next items in
				//searchDescIndex
				//until 15 lines have passed
				//	-> failReplace+1, break
				//or the final quote has been replaced
				//	-> replaced+1, break
			}
			

		//if no matches,
			//noMatchCount++
			//log: ItemDescIndex.no_match		
		}

				
		return sourceIndex;
	}
	
	private class ItemDesc{
		private LinkedList<String> descriptions = new LinkedList<String>();
		
		private ItemDesc(String newItem){
			String[] descToAdd = newItem.split("\t");
			for (String str : descToAdd) {
				this.descriptions.add(str);
			}
		}
		
		public ListIterator<String> listIterator(){
			return descriptions.listIterator();
		}
	}
}
