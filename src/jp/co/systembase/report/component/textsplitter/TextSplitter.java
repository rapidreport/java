package jp.co.systembase.report.component.textsplitter;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.ReportUtil;

public class TextSplitter {

	private final String WORD_CHARS =
		"0123456789" +
		"abcdefghijklmnopqrstuvwxyz" +
		"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
		"ﾞﾟｰ･｡@#$%^&*_+|~=\\`<>\"':;,.,";
	private final String OPEN_CHARS =
		"｢({[\"'（［｛〈《「『【〔";
	private final String CLOSE_CHARS =
		"｣)}]\"':;,.,–/）］｝〉》」』】〕" +
		"”’；：。、．,・ゝゞー" +
		"ァィゥェォッャュョヮヵヶ" +
		"ぁぃぅぇぉっゃゅょゎゕゖ" +
		"ㇰㇱㇲㇳㇴㇵㇶㇷㇸㇹㇷ゚ㇺㇻㇼㇽㇾㇿ々〻〜";

	private boolean _BreakRule;

	public TextSplitter() {
		this(false);
	}

	public TextSplitter(boolean breakRule){
		this._BreakRule = breakRule;
	}

	public List<String> getLines(String text){
		return this._getLines(text, -1);
	}

	private List<String> _getLines(String text, int limit){
		List<String> ret = new ArrayList<String>();
		if (text == null){
			ret.add(null);
		}else{
			for(String t: text.split("\n")){
				this._split(ret, t.replace("\r", ""), limit);
				if (limit >= 0 && ret.size() > limit){
					break;
				}
			}
		}
		return ret;
	}

	public String getLine(String text, int i){
		List<String> l = this._getLines(text, i);
		if (l.size() > i){
			return l.get(i);
		}else{
			return null;
		}
	}

	private void _split(List<String> l, String text, int limit){
		String t = text;
		do{
			int w = this._getNextWidth(t);
			if (this._BreakRule){
				w = this._getNextOnRule(t, w);
			}
			l.add(ReportUtil.subString2(t, 0, w));
			if (limit >= 0 && l.size() > limit){
				break;
			}
			t = ReportUtil.subString2(t, w);
		}while(ReportUtil.stringLen(t) > 0);
	}

	protected int _getNextWidth(String text){
		return ReportUtil.stringLen(text);
	}

	private int _getNextOnRule(String text, int w){
		if (w == ReportUtil.stringLen(text)){
			return w;
		}
		int _w = w;
		while(_w > 0){
			String cp = ReportUtil.subString2(text, _w - 1, _w);
			String cn = ReportUtil.subString2(text, _w, _w + 1);
			if (WORD_CHARS.contains(cp) && WORD_CHARS.contains(cn)){
			}else if (OPEN_CHARS.contains(cp)){
			}else if (CLOSE_CHARS.contains(cn)){
			}else{
				return _w;
			}
			_w--;
		}
		return w;
	}

}
