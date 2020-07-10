package replaceTextLoop;

public class RTLtest {

	public static void main(String[] args) {
//		LinesIndex searchIndex = new LinesIndex();
//		searchIndex.add("lang(\"ゼミフラグ＝\" + var_65(250 + 330) + \" \", \"Seminar variable:\" + var_65(250 + 330) + \" \")");
//		searchIndex.add("lang(\"第2回（\" + var_2276 + \"gp）必要レベル2\", \"Part 2 / \" + var_2276 + \"gp - min. Lv2\")");
//		searchIndex.add("lang(var_63(1, var_537) + \" \" + var_63(0, var_537) + \" は貴重品を持っていないよ。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(var_63(1, var_537) + \" \" + var_63(0, var_537) + \" は\" + var_386 + \"を持っているよ。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"いひひ…次は\" + var_65(250 + 305) * 50 + \"種類まで集めたらまた見せてくだされ。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"少し怖い話をしてあげ\" + _ru(3) + \"。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"戦闘時にはあまり相手に近づかないらしい\" + _ga(3) + \"。まぁそりゃそう\" + _da(3) + \"、本来は通信機なんだから。もし戦うなら、コイツからテレポートで距離を取って、他の敵を先に片づけたほうがマシ\" + _daro(3) + \"。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"\" + var_80 + \"の鈍足\", \"Decreases speed by \" + var_80)");
//		searchIndex.add("lang(var_63(1, 0) + var_63(0, 0) + \"は狂喜して叫んだ。「\" + var_436 + \"！！」\" + var_236, var_63(1, 0) + \" \" + var_63(0, 0) + \" goes wild with joy, \\\"\" + var_436 + \"!!\\\" \" + cnven(var_236))");
//		searchIndex.indexQuotes();
//		searchIndex.indexVars();
//		searchIndex.print();
//		
//		LinesIndex sourceIndex = new LinesIndex();
//		searchIndex.add("lang(\"ゼミフラグ＝\" + hoihoi(250 + 330) + \" \", \"Seminar variable:\" + mmmm333(250 + 330) + \" \")");
//		searchIndex.add("lang(\"第2回（\" + mewmewmew + \"gp）必要レベル2\", \"Part 2 / \" + あぁん + \"gp - min. Lv2\")");
//		searchIndex.add("lang(感謝感激(1, var_537) + \" \" + ボボボーボ(0, var_537) + \" は貴重品を持っていないよ。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(変数A(1, var_537) + \" \" + 変数b(0, var_537) + \" は\" + var_386 + \"を持っているよ。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"いひひ…次は\" + var_65(250 + 305) * 50 + \"種類まで集めたらまた見せてくだされ。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"少し怖い話をしてあげ\" + _ru(3) + \"。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"戦闘時にはあまり相手に近づかないらしい\" + _ga(3) + \"。まぁそりゃそう\" + _da(3) + \"、本来は通信機なんだから。もし戦うなら、コイツからテレポートで距離を取って、他の敵を先に片づけたほうがマシ\" + _daro(3) + \"。\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"\" + var_80 + \"の鈍足\", \"Decreases speed by \" + var_80)");
//		searchIndex.add("lang(var_63(1, 0) + var_63(0, 0) + \"は狂喜して叫んだ。「\" + var_436 + \"！！」\" + var_236, var_63(1, 0) + \" \" + var_63(0, 0) + \" goes wild with joy, \\\"\" + var_436 + \"!!\\\" \" + cnven(var_236))");
//		searchIndex.indexQuotes();
//		
		
		Line search1 = new Line("lang(\"ゼミフラグ＝\" + var_65(250 + 330) + \" \", \"Seminar variable:\" + var_65(250 + 330) + \" \")");
		search1.indexQuotes();
		search1.indexVars();
		Line src1 = new Line("aasdasd(0,49) = lang( \"ゼミフラグ＝\" + hoihoi(250 + 330) + \" \", \"Seminar variable:\" + mmmm333(250+330) + \" \")");
		src1.indexQuotes();
		
		src1.indexUnknownVars(search1);
		
		
	}

}
