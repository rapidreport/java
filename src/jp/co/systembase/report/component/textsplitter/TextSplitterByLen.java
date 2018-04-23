package jp.co.systembase.report.component.textsplitter;

import jp.co.systembase.report.ReportUtil;

public class TextSplitterByLen extends TextSplitter {
	private int _length;
	public TextSplitterByLen(int length) {
		super(false);
		this._length = length;
	}
	@Override
	protected int _getNextWidth(String text) {
		if (this._length == 0){
			return super._getNextWidth(text);
		}
		return Math.min(this._length, ReportUtil.stringLen(text));
	}
}
