package jp.co.systembase.report.component.textsplitter;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.TextDesign;

public class TextSplitterByDrawingWidth extends TextSplitter {

	private TextDesign _textDesign;
	private float _width;
	private float _clipWidth;

	public TextSplitterByDrawingWidth(TextDesign textDesign, float width, float clipWidth){
		super(true);
		_textDesign = textDesign;
		_width = width;
		_clipWidth = clipWidth;
	}

	@Override
	protected int _getNextWidth(String text) {
		if (_width == 0){
			return super._getNextWidth(text);
		}
		return _textDesign.getMonospacedDrawableLen(text, _width);
	}

	@Override
	protected String _clipText(String text, int w) {
		if (_clipWidth == 0){
			return super._clipText(text, w);
		}
		return ReportUtil.subString(text, 0, _textDesign.getMonospacedDrawableLen(text, _clipWidth, w));
	}

}
