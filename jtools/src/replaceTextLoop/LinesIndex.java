package replaceTextLoop;

import java.util.LinkedList;

public class LinesIndex {
	private LinkedList<Line> LI = new LinkedList<Line>();
	
	public void add(String str) {
		LI.addLast(new Line(str));
	}
}
