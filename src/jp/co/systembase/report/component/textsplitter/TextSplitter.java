package jp.co.systembase.report.component.textsplitter;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.Report;
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

	private boolean _breakRule;

	public TextSplitter() {
		this(false);
	}

	public TextSplitter(boolean breakRule){
		this._breakRule = breakRule;
	}

	public List<String> getLines(String text){
		return this._getLines_aux(text, -1);
	}

	public String getLine(String text, int i){
		List<String> l = this._getLines_aux(text, i);
		if (l.size() > i){
			return l.get(i);
		}else{
			return null;
		}
	}
	
	private List<String> _getLines_aux(String text, int limit){
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

	

	private void _split(List<String> l, String text, int limit){
		if (text.isEmpty()){
			l.add("");
		}else{
			String t = text;
			while(true){
				int w = _getNextWidth(t);
				if (w == 0){
					break;
				}
				if (_breakRule){
					w = _getNextOnRule(t, w);
				}
				l.add(_clipText(t, w));
				if (limit >= 0 && l.size() > limit){
					break;
				}
				if (ReportUtil.stringLen(t) > w){
					t = ReportUtil.subString(t, w);
					if (!Report.Compatibility._4_37_WrappedTextNoTrim){
						t = ReportUtil.trimLeft(t);
					}
				}else{
					break;
				}
			}
		}
	}

	protected int _getNextWidth(String text){
		return ReportUtil.stringLen(text);
	}

	protected String _clipText(String text, int w){
		return ReportUtil.subString(text, 0, w);
	}

	private int _getNextOnRule(String text, int w){
		if (w == ReportUtil.stringLen(text)){
			return w;
		}
		int _w = w;
		while(_w > 0){
			String cp = ReportUtil.subString(text, _w - 1, 1);
			String cn = ReportUtil.subString(text, _w, 1);
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
