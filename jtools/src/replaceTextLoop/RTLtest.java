package replaceTextLoop;

import java.util.ListIterator;

//sandbox
public class RTLtest {

	public static void main(String[] args) {
	LinesIndex src = new LinesIndex();
	src.add("			txt_select lang(\"ゼミフラグ＝\" + somevariable(定数わん + WATERMELON) + \" \", \" \"), \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"");
	src.add("　　　　somevar(CONST_FOO, CONST_BAR99) = lang(\"初回 （\" + 変数だあ + \"gp）\", \"Part.1 / \" + 変数だあ + \"gp\")");
	src.indexQuotes();
//	System.out.println("source printing: ");
//	src.print();
//	System.out.println(".......");
	
	LinesIndex search = new LinesIndex();
	search.add("lang(\"ゼミフラグ＝\" + var_65(250 + 330) + \" \", \" \")");
	search.add("lang(\"初回 （\" + var_2374 + \"gp）\", \"Part.1 / \" + var_2374 + \"gp\")");
	search.indexVars();
	search.indexQuotes();
//	System.out.println("search printing: ");
//	search.print();
//	System.out.println(".......");
	
	LinesIndex tl = new LinesIndex();
	tl.add("lang(\"ゼミフラグ＝\" + var_65(250 + 330) + \" \", \"Seminar variable:\" + var_65(250 + 330) + \" \")");
	tl.add("lang(\"初回 （\" + var_2374 + \"gp）\", \"Part.1 / \" + var_2374 + \"gp\")");
	tl.indexVars();
	tl.indexQuotes();
//	System.out.println("tl printing");
//	tl.print();
//	System.out.println(".......");

	src = LinesIndex.replaceLoop(search, src, tl);
//	System.out.println("source printing after replace");
//	src.print();
	ListIterator<Line> srcIter = src.listIterator();
	while (srcIter.hasNext()) {
		Line line = srcIter.next();
		System.out.println(line.getRaw());
//		System.out.println(line.getVars());
//		System.out.println(line.getQuotes());
	}
	
	
	}

}
