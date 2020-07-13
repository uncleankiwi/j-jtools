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
	
	public static LinesIndex replaceLoop(LinesIndex sourceIndex, ItemDescIndex searchDescIndex, ItemDescIndex replaceDescIndex) {
		//TODO
		return sourceIndex;
	}
	
	private class ItemDesc{
		private LinkedList<String> descriptions = new LinkedList<String>();
		
		private ItemDesc(String newItem){
			//TODO
			String[] descToAdd = newItem.split("\t");
			for (String str : descToAdd) {
				this.descriptions.add(str);
			}
		}
	}
}
