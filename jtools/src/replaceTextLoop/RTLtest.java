package replaceTextLoop;

public class RTLtest {

	public static void main(String[] args) {
		LinesIndex sourceIndex = new LinesIndex();
		sourceIndex.add("lang(\"�[�~�t���O��\" + var_65(250 + 330) + \" \", \"Seminar variable:\" + var_65(250 + 330) + \" \")");
		sourceIndex.add("lang(\"��2��i\" + var_2276 + \"gp�j�K�v���x��2\", \"Part 2 / \" + var_2276 + \"gp - min. Lv2\")");
		sourceIndex.add("lang(var_63(1, var_537) + \" \" + var_63(0, var_537) + \" �͋M�d�i�������Ă��Ȃ���B\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(var_63(1, var_537) + \" \" + var_63(0, var_537) + \" ��\" + var_386 + \"�������Ă����B\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(\"���ЂЁc����\" + var_65(250 + 305) * 50 + \"��ނ܂ŏW�߂���܂������Ă�������B\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(\"�����|���b�����Ă���\" + _ru(3) + \"�B\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(\"�퓬���ɂ͂��܂葊��ɋ߂Â��Ȃ��炵��\" + _ga(3) + \"�B�܂�����Ⴛ��\" + _da(3) + \"�A�{���͒ʐM�@�Ȃ񂾂���B�����키�Ȃ�A�R�C�c����e���|�[�g�ŋ���������āA���̓G���ɕЂÂ����ق����}�V\" + _daro(3) + \"�B\", \"Sorry, this is untranslated sentence.\")");
		sourceIndex.add("lang(\"\" + var_80 + \"�̓ݑ�\", \"Decreases speed by \" + var_80)");
		sourceIndex.add("lang(var_63(1, 0) + var_63(0, 0) + \"�͋��삵�ċ��񂾁B�u\" + var_436 + \"�I�I�v\" + var_236, var_63(1, 0) + \" \" + var_63(0, 0) + \" goes wild with joy, \\\"\" + var_436 + \"!!\\\" \" + cnven(var_236))");
		sourceIndex.indexQuotes();
		sourceIndex.indexVars();
		sourceIndex.print();
	}

}
