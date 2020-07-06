package replaceTextLoop;

public class RTLtest {

	public static void main(String[] args) {
		LinesIndex sourceIndex = new LinesIndex();
		sourceIndex.add("lang(\"ゼミフラグ＝\" + var_65(250 + 330) + \" \", \"Seminar variable:\" + var_65(250 + 330) + \" \")");
		sourceIndex.add("lang(\"第2回（\" + var_2276 + \"gp）必要レベル2\", \"Part 2 / \" + var_2276 + \"gp - min. Lv2\")");
		sourceIndex.add("lang(var_63(1, var_537) + \" \" + var_63(0, var_537) + \" は貴重品を持っていないよ。\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(var_63(1, var_537) + \" \" + var_63(0, var_537) + \" は\" + var_386 + \"を持っているよ。\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(\"いひひ…次は\" + var_65(250 + 305) * 50 + \"種類まで集めたらまた見せてくだされ。\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(\"少し怖い話をしてあげ\" + _ru(3) + \"。\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(\"戦闘時にはあまり相手に近づかないらしい\" + _ga(3) + \"。まぁそりゃそう\" + _da(3) + \"、本来は通信機なんだから。もし戦うなら、コイツからテレポートで距離を取って、他の敵を先に片づけたほうがマシ\" + _daro(3) + \"。\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(\"\" + var_80 + \"の鈍足\", \"Decreases speed by \" + var_80)");
		sourceIndex.add("lang(var_63(1, 0) + var_63(0, 0) + \"は狂喜して叫んだ。「\" + var_436 + \"！！」\" + var_236, var_63(1, 0) + \" \" + var_63(0, 0) + \" goes wild with joy, \\\"\" + var_436 + \"!!\\\" \" + cnven(var_236))");
		sourceIndex.indexQuotes();
		sourceIndex.indexVars();
		sourceIndex.print();
	}

}
