package replaceTextLoop;

public class RTLtest {

	public static void main(String[] args) {
//		LinesIndex searchIndex = new LinesIndex();
//		searchIndex.add("lang(\"�[�~�t���O��\" + var_65(250 + 330) + \" \", \"Seminar variable:\" + var_65(250 + 330) + \" \")");
//		searchIndex.add("lang(\"��2��i\" + var_2276 + \"gp�j�K�v���x��2\", \"Part 2 / \" + var_2276 + \"gp - min. Lv2\")");
//		searchIndex.add("lang(var_63(1, var_537) + \" \" + var_63(0, var_537) + \" �͋M�d�i�������Ă��Ȃ���B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(var_63(1, var_537) + \" \" + var_63(0, var_537) + \" ��\" + var_386 + \"�������Ă����B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"���ЂЁc����\" + var_65(250 + 305) * 50 + \"��ނ܂ŏW�߂���܂������Ă�������B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"�����|���b�����Ă���\" + _ru(3) + \"�B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"�퓬���ɂ͂��܂葊��ɋ߂Â��Ȃ��炵��\" + _ga(3) + \"�B�܂�����Ⴛ��\" + _da(3) + \"�A�{���͒ʐM�@�Ȃ񂾂���B�����키�Ȃ�A�R�C�c����e���|�[�g�ŋ���������āA���̓G���ɕЂÂ����ق����}�V\" + _daro(3) + \"�B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"\" + var_80 + \"�̓ݑ�\", \"Decreases speed by \" + var_80)");
//		searchIndex.add("lang(var_63(1, 0) + var_63(0, 0) + \"�͋��삵�ċ��񂾁B�u\" + var_436 + \"�I�I�v\" + var_236, var_63(1, 0) + \" \" + var_63(0, 0) + \" goes wild with joy, \\\"\" + var_436 + \"!!\\\" \" + cnven(var_236))");
//		searchIndex.indexQuotes();
//		searchIndex.indexVars();
//		searchIndex.print();
//		
//		LinesIndex sourceIndex = new LinesIndex();
//		searchIndex.add("lang(\"�[�~�t���O��\" + hoihoi(250 + 330) + \" \", \"Seminar variable:\" + mmmm333(250 + 330) + \" \")");
//		searchIndex.add("lang(\"��2��i\" + mewmewmew + \"gp�j�K�v���x��2\", \"Part 2 / \" + ������ + \"gp - min. Lv2\")");
//		searchIndex.add("lang(���ӊ���(1, var_537) + \" \" + �{�{�{�[�{(0, var_537) + \" �͋M�d�i�������Ă��Ȃ���B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(�ϐ�A(1, var_537) + \" \" + �ϐ�b(0, var_537) + \" ��\" + var_386 + \"�������Ă����B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"���ЂЁc����\" + var_65(250 + 305) * 50 + \"��ނ܂ŏW�߂���܂������Ă�������B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"�����|���b�����Ă���\" + _ru(3) + \"�B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"�퓬���ɂ͂��܂葊��ɋ߂Â��Ȃ��炵��\" + _ga(3) + \"�B�܂�����Ⴛ��\" + _da(3) + \"�A�{���͒ʐM�@�Ȃ񂾂���B�����키�Ȃ�A�R�C�c����e���|�[�g�ŋ���������āA���̓G���ɕЂÂ����ق����}�V\" + _daro(3) + \"�B\", \"Sorry, this is untranslated sentence.\")");
//		searchIndex.add("lang(\"\" + var_80 + \"�̓ݑ�\", \"Decreases speed by \" + var_80)");
//		searchIndex.add("lang(var_63(1, 0) + var_63(0, 0) + \"�͋��삵�ċ��񂾁B�u\" + var_436 + \"�I�I�v\" + var_236, var_63(1, 0) + \" \" + var_63(0, 0) + \" goes wild with joy, \\\"\" + var_436 + \"!!\\\" \" + cnven(var_236))");
//		searchIndex.indexQuotes();
//		
		
		Line search1 = new Line("lang(\"�[�~�t���O��\" + var_65(250 + 330) + \" \", \"Seminar variable:\" + var_65(250 + 330) + \" \")");
		search1.indexQuotes();
		search1.indexVars();
		Line src1 = new Line("aasdasd(0,49) = lang( \"�[�~�t���O��\" + hoihoi(250 + 330) + \" \", \"Seminar variable:\" + mmmm333(250+330) + \" \")");
		src1.indexQuotes();
		
		src1.indexUnknownVars(search1);
		
		
	}

}
