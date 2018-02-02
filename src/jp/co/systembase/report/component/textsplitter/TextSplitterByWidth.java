package jp.co.systembase.report.component.textsplitter;

import jp.co.systembase.report.ReportUtil;

public class TextSplitterByWidth extends TextSplitter {
	int _width;
	public TextSplitterByWidth(int width, boolean breakRule) {
		super(breakRule);
		this._width = width;
	}
	@Override
	protected int _getNextWidth(String text) {
		if (this._width == 0){
			return super._getNextWidth(text);
		}
		return ReportUtil.getWIndex(text, 0, this._width);
	}
}
