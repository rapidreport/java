package jp.co.systembase.report.renderer.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.component.textsplitter.TextSplitter;
import jp.co.systembase.report.component.textsplitter.TextSplitterByLen;
import jp.co.systembase.report.renderer.RenderUtil;

public class PdfText {

	public PdfRenderer renderer;
	public Region region;
	public TextDesign textDesign;
	public String text;
	public PdfContentByte contentByte;
	public BaseFont font;
	public BaseFont gaijiFont;
	public List<Float> textMatrix = null;

	protected static final float TOLERANCE = 0.1f;
	protected static final float OFFSET_Y = -0.5f;
	protected static final float MARGIN_X = 2.0f;
	protected static final float MARGIN_BOTTOM = 2.0f;

	protected static final String VERTICAL_ROTATE_CHARS = "…‥｜ーｰ(){}[]<>（）｛｝「」＜＞←→↓⇒⇔↑＝≒";
	protected static final String VERTICAL_ROTATE2_CHARS = "～";
	protected static final String VERTICAL_SHIFT_CHARS = "。、，";

	public void Initialize(
			PdfRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			String text){
		this.renderer = renderer;
		this.region = region.toPointScale(reportDesign);
		this.textDesign = new TextDesign(reportDesign, design);
		this.text = text;
		this.contentByte = this.renderer.writer.getDirectContent();
		this.font = this.renderer.setting.getFont(textDesign.font.name);
		this.gaijiFont = this.renderer.setting.getGaijiFont(textDesign.font.name);
	}

	public void draw(){
		if (_isEmpty()){
			return;
		}
		contentByte.saveState();
		try{
			_draw_preprocess();
			if (textDesign.distribute){
				if (textDesign.vertical){
					_draw_distributeVertical();
				}else{
					_draw_distribute();
				}
			}else if (textDesign.vertical){
				if (textDesign.shrinkToFit){
					_draw_verticalShrink();
				}else if (textDesign.wrap){
					_draw_verticalWrap();
				}else{
					_draw_vertical();
				}
			}else if (textDesign.decimalPlace > 0){
				if (textDesign.shrinkToFit){
					_draw_fixdecShrink();
				}else{
					_draw_fixdec();
				}
			}else if (textDesign.shrinkToFit){
				_draw_shrink();
			}else if (textDesign.wrap){
				_draw_wrap();
			}else{
				_draw();
			}
		}finally{
			contentByte.restoreState();
		}
	}

	protected void _draw_preprocess(){
		if (textDesign.color != null){
			short[] c = RenderUtil.getColorRGB(textDesign.color);
			if (c != null){
				contentByte.setRGBColorFill(c[0], c[1], c[2]);
				contentByte.setRGBColorStroke(c[0], c[1], c[2]);
			}
		}
		if (textDesign.font.bold){
			contentByte.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
			if (!Report.Compatibility._4_6_PdfFontBold){
				contentByte.setLineWidth(textDesign.font.size * 0.01f);
            }
		}else{
			contentByte.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
		}
	}

	protected void _draw_distribute(){
		float fontSize = textDesign.font.size;
		List<String> texts = (new TextSplitter()).getLines(this.text);
		float y = 0;
		switch(textDesign.valign){
		case TOP:
			y = 0;
			break;
		case CENTER:
			y = (region.getHeight() - (fontSize * texts.size())) / 2;
			y = Math.max(y, 0);
			break;
		case BOTTOM:
			y = region.getHeight() - (fontSize * texts.size()) - MARGIN_BOTTOM;
			y = Math.max(y, 0);
			break;
		}
		y += OFFSET_Y;
		int rows = (int)((region.getHeight() + TOLERANCE) / fontSize);
		for(int i = 0;i < Math.max(Math.min(texts.size(), rows), 1);i++){
			String t = texts.get(i);
			List<Float> m  = _getDistributeMap(region.getWidth() - MARGIN_X * 2, ReportUtil.stringLen(t), fontSize);
			_draw_preprocess();
            contentByte.setFontAndSize(font, fontSize);
            contentByte.beginText();
            for(int j = 0;j < ReportUtil.stringLen(t);j++){
            	String c = ReportUtil.subString(t, j, 1);
                _drawText(fontSize, c, m.get(j) - _getTextWidth(fontSize, c) / 2 + MARGIN_X, y);
            }
            contentByte.endText();
            if (textDesign.font.underline){
    			float lw = (fontSize / 13.4f) * renderer.setting.underlineWidthCoefficient;
    			_drawUnderline(fontSize, MARGIN_X, y, region.getWidth() - MARGIN_X * 2, lw);
    		}
            y += fontSize;
        }
	}

	protected void _draw_distributeVertical(){
		float fontSize = textDesign.font.size;
		List<String> texts = (new TextSplitter()).getLines(this.text);
		float x = 0;
		switch(textDesign.halign){
		case LEFT:
			x = fontSize * (texts.size() - 1) + fontSize / 2 + MARGIN_X;
			x = Math.min(x, region.getWidth() - fontSize / 2 - MARGIN_X);
			break;
		case CENTER:
			x = (region.getWidth() + (texts.size() - 1) * fontSize) / 2;
			x = Math.min(x, region.getWidth() - fontSize / 2 - MARGIN_X);
			break;
		case RIGHT:
			x = region.getWidth() - fontSize / 2 - MARGIN_X;
			break;
		}
		int cols = (int)(((region.getWidth() - MARGIN_X * 2) + TOLERANCE) / fontSize);
		for(int i = 0;i < Math.max(Math.min(texts.size(), cols), 1);i++){
			String t = texts.get(i);
			if (textDesign.font.underline){
				float lw = (fontSize / 13.4f) * renderer.setting.underlineWidthCoefficient;
	        	_drawVerticalUnderLine(fontSize, x + fontSize / 2, 0, region.getHeight(), lw);
	        }
			{
				List<Float> m = _getDistributeMap(region.getHeight() - MARGIN_BOTTOM, ReportUtil.stringLen(t), fontSize);
				_draw_preprocess();
				contentByte.setFontAndSize(font, fontSize);
				contentByte.beginText();
				for(int j = 0;j < ReportUtil.stringLen(t);j++){
					_drawVerticalChar(fontSize, ReportUtil.subString(t, j, 1),
							x, m.get(j) - fontSize / 2 + OFFSET_Y);
	            }
	            contentByte.endText();
			}
			x -= fontSize;
		}
	}

	protected void _draw_vertical(){
		_draw_vertical_aux(textDesign.font.size, (new TextSplitter()).getLines(this.text));
	}

	protected void _draw_verticalShrink(){
		float fontSize = textDesign.font.size;
		List<String> texts = (new TextSplitter()).getLines(this.text);
		{
			int m = 0;
			for(String t: texts){
				if (m < ReportUtil.stringLen(t)){
					m = ReportUtil.stringLen(t);
				}
			}
			if (m > 0){
				float _fontSize = Math.max((region.getHeight() - MARGIN_BOTTOM) / m, renderer.setting.shrinkFontSizeMin);
				fontSize = Math.min(fontSize, _fontSize);
			}
        }
        _draw_vertical_aux(fontSize, texts);
	}

	protected void _draw_verticalWrap(){
		int l = (int)((this.region.getHeight() + TOLERANCE) / this.textDesign.font.size);
		_draw_vertical_aux(textDesign.font.size, (new TextSplitterByLen(l)).getLines(this.text));
	}

	protected void _draw_fixdec(){
		_FixDec fd = new _FixDec(this);
		List<String> texts = new ArrayList<String>();
		texts.add(fd.getFullText());
		fd.drawText(textDesign.font.size);
	}

	protected void _draw_fixdecShrink(){
		_FixDec fd = new _FixDec(this);
		List<String> texts = new ArrayList<String>();
		texts.add(fd.getFullText());
		float fontSize = _getFitFontSize(texts);
		fd.drawText(fontSize);
	}

	protected void _draw_shrink(){
		List<String> texts = (new TextSplitter()).getLines(this.text);
		float fontSize = _getFitFontSize(texts);
		_draw_aux(fontSize, texts);
	}

	protected void _draw_wrap(){
		_draw_aux(textDesign.font.size, (new _TextSplitterByPdfWidth(this)).getLines(this.text));
	}

	protected void _draw(){
		List<String> texts = (new TextSplitter()).getLines(this.text);
		_draw_aux(textDesign.font.size, texts);
	}

    protected void _draw_aux(
			float fontSize,
			List<String> texts){
		float y = 0;
		switch(textDesign.valign){
		case TOP:
			y = 0;
			break;
		case CENTER:
			y = (region.getHeight() - fontSize * texts.size()) / 2;
			y = Math.max(y, 0);
			break;
		case BOTTOM:
			y = region.getHeight() - fontSize * texts.size() - MARGIN_BOTTOM;
			y = Math.max(y, 0);
			break;
		}
		y += OFFSET_Y;
		int rows = (int)((region.getHeight() + TOLERANCE) / fontSize);
		for(int i = 0;i < Math.max(Math.min(texts.size(), rows), 1);i++){
			String t = texts.get(i);
			float w = _getTextWidth(fontSize, t);
			{
				float cw = region.getWidth() - MARGIN_X * 2;
				if (w > cw){
					String _t = "";
					float _w = 0;
					for(int j = 1;j <= ReportUtil.stringLen(t);j++){
						String __t = ReportUtil.subString(t, 0, j);
						float __w = _getTextWidth(fontSize, __t);
						if (__w <= cw + TOLERANCE){
							_t = __t;
							_w = __w;
						}else{
							break;
						}
					}
					t = _t;
					w = _w;
				}
			}
			float x = 0;
			switch(textDesign.halign){
			case LEFT:
				x = MARGIN_X;
				break;
			case CENTER:
				x = (region.getWidth() - w) / 2;
				x = Math.max(x, MARGIN_X);
				break;
			case RIGHT:
				x = region.getWidth() - w - MARGIN_X;
				x = Math.max(x, MARGIN_X);
				break;
			}
			_draw_preprocess();
			contentByte.setFontAndSize(font, fontSize);
			contentByte.beginText();
			_drawText(fontSize, t, x, y);
			contentByte.endText();
			if (textDesign.font.underline){
				float lw = (fontSize / 13.4f) * renderer.setting.underlineWidthCoefficient;
				_drawUnderline(fontSize, x, y, w, lw);
			}
			y += fontSize;
		}
	}

	protected void _draw_vertical_aux(
			float fontSize,
			List<String> texts){
		float x = 0;
		switch(textDesign.halign){
		case LEFT:
			x = fontSize * (texts.size() - 1) + fontSize / 2 + MARGIN_X;
			x = Math.min(x, region.getWidth() - fontSize / 2 - MARGIN_X);
			break;
		case CENTER:
			x = (region.getWidth() + (texts.size() - 1) * fontSize) / 2;
			x = Math.min(x, region.getWidth() - fontSize / 2 - MARGIN_X);
			break;
		case RIGHT:
			x = region.getWidth() - fontSize / 2 - MARGIN_X;
			break;
		}
		int cols = (int)(((region.getWidth() - MARGIN_X * 2) + TOLERANCE) / fontSize);
		int rows = (int)((region.getHeight() + TOLERANCE) / fontSize);
		for(int i = 0;i < Math.max(Math.min(texts.size(), cols), 1);i++){
			String t = texts.get(i);
			float y = 0;
			switch(textDesign.valign){
			case TOP:
				y = 0;
				break;
			case CENTER:
				y = (region.getHeight() - fontSize * ReportUtil.stringLen(t)) / 2;
				y = Math.max(y, 0);
				break;
			case BOTTOM:
				y = region.getHeight() - fontSize * ReportUtil.stringLen(t) - MARGIN_BOTTOM;
				y = Math.max(y, 0);
				break;
			}
			y += OFFSET_Y;
			int _yc = Math.min(ReportUtil.stringLen(t), rows);
			if (textDesign.font.underline){
				float lw = (fontSize / 13.4f) * renderer.setting.underlineWidthCoefficient;
				_drawVerticalUnderLine(fontSize, x + fontSize / 2, y, _yc * fontSize, lw);
			}
			_draw_preprocess();
			contentByte.setFontAndSize(font, fontSize);
			contentByte.beginText();
			for(int j = 0;j < _yc;j++){
				_drawVerticalChar(fontSize, ReportUtil.subString(t, j, 1), x, y);
				y += fontSize;
			}
			contentByte.endText();
			x -= fontSize;
		}
	}

	protected void _setTextMatrix(
			float fontSize,
			float x,
			float y){
		PdfRenderer.Trans trans = renderer.trans;
		float _x = region.left + x;
		float _y = (region.top + y + fontSize) - (fontSize / 13.4f);
		if (textMatrix != null){
			contentByte.setTextMatrix(textMatrix.get(0), textMatrix.get(1), textMatrix.get(2), textMatrix.get(3),
					trans.x(_x), trans.y(_y));
		}else{
			if (textDesign.font.italic){
				if (textDesign.vertical){
					contentByte.setTextMatrix(1f, -0.3f, 0f, 1f, trans.x(_x), trans.y(_y));
				}else{
					contentByte.setTextMatrix(1f, 0f, 0.3f, 1f, trans.x(_x), trans.y(_y));
				}
			}else{
				contentByte.setTextMatrix(trans.x(_x), trans.y(_y));
			}
		}
	}

	protected void _setRotateTextMatrix(
			float fontSize,
			float x,
			float y){
		PdfRenderer.Trans trans = renderer.trans;
		float _x = region.left + x;
		float _y = region.top + y + fontSize;
		if (textDesign.font.italic){
			_x -= fontSize / 13.4;
			_y += fontSize / 4;
			contentByte.setTextMatrix(0f, -1f, 1f, -0.3f, trans.x(_x), trans.y(_y));
		}else{
			contentByte.setTextMatrix(0f, -1f, 1f, 0f, trans.x(_x), trans.y(_y));
		}
	}

	protected void _setRotate2TextMatrix(
			float fontSize,
			float x,
			float y){
		PdfRenderer.Trans trans = renderer.trans;
		float _x = region.left + x;
		float _y = region.top + y + fontSize;
		if (textDesign.font.italic){
			_x -= fontSize / 13.4;
			_y += fontSize / 2;
			contentByte.setTextMatrix(0f, -1f, -1f, 0.3f, trans.x(_x), trans.y(_y));
		}else{
			contentByte.setTextMatrix(0f, -1f, -1f, 0f, trans.x(_x), trans.y(_y));
		}
	}

	protected void _drawText(
			float fontSize,
			String text,
			float x,
			float y){
		List<String> _texts = null;
		if (renderer.setting.gaijiFont != null || gaijiFont != null){
			_texts = _detectGaiji(text);
		}
		if (_texts == null){
			_setTextMatrix(fontSize, x, y);
			contentByte.showText(text);
		}else{
			boolean gaiji = false;
			float _x = x;
			for(String t: _texts){
				if (ReportUtil.stringLen(t) > 0){
					if (!gaiji){
						_setTextMatrix(fontSize, _x, y);
						contentByte.showText(t);
						_x += font.getWidthPoint(t, fontSize);
					}else{
						for(int i = 0;i < ReportUtil.stringLen(t);i++){
							BaseFont f = renderer.setting.gaijiFont;
							if (gaijiFont != null && gaijiFont.getWidth(t.charAt(i)) > 0){
								f = gaijiFont;
							}
							if (f == null){
								f = font;
							}
							contentByte.setFontAndSize(f, fontSize);
							_setTextMatrix(fontSize, _x, y);
							contentByte.showText(ReportUtil.subString(t, i, 1));
							_x += fontSize;
						}
						contentByte.setFontAndSize(font, fontSize);
					}
				}
				gaiji = !gaiji;
			}
		}
	}

	protected void _drawVerticalChar(
			float fontSize,
			String c,
			float x,
			float y){
		boolean gaiji = false;
		if (renderer.setting.gaijiFont != null && _isGaiji(c.charAt(0))){
			contentByte.setFontAndSize(renderer.setting.gaijiFont, fontSize);
			gaiji = true;
		}
		if (VERTICAL_ROTATE_CHARS.indexOf(c) >= 0){
			_setRotateTextMatrix(
				fontSize, x - fontSize / 3, y - _getTextWidth(fontSize, c));
		}else if (VERTICAL_ROTATE2_CHARS.indexOf(c) >= 0){
			_setRotate2TextMatrix(
				fontSize, x + fontSize / 3, y - _getTextWidth(fontSize, c));
		}else if (VERTICAL_SHIFT_CHARS.indexOf(c) >= 0){
			float d = -_getTextWidth(fontSize, c) / 2;
			if (textDesign.font.italic){
				d += fontSize / 4;
			}
			_setTextMatrix(fontSize, x, y + d);
		}else{
			_setTextMatrix(
			  fontSize, x - _getTextWidth(fontSize, c) / 2, y);

		}
		contentByte.showText(c);
		if (gaiji){
			contentByte.setFontAndSize(font, fontSize);
		}
	}

	protected void _drawUnderline(
			float fontSize,
			float x,
			float y,
			float width,
			float lineWidth){
		PdfRenderer.Trans trans = renderer.trans;
		float _x1 = region.left + x;
		float _x2= _x1 + width;
		float _y = (region.top + y + fontSize) - OFFSET_Y;
		_x1 = Math.max(_x1, region.left + MARGIN_X);
		_x2 = Math.min(_x2, region.right - MARGIN_X);
		if (_x1 < _x2){
			contentByte.setLineWidth(lineWidth);
			contentByte.moveTo(trans.x(_x1), trans.y(_y));
			contentByte.lineTo(trans.x(_x2), trans.y(_y));
			contentByte.stroke();
		}
    }

	protected void _drawVerticalUnderLine(
			float fontSize,
			float x,
			float y,
			float h,
			float lineWidth){
		PdfRenderer.Trans trans = renderer.trans;
		float _x = region.left + x;
		float _y = (region.top + y) - OFFSET_Y;
		contentByte.setLineWidth(lineWidth);
		contentByte.moveTo(trans.x(_x), trans.y(_y));
		contentByte.lineTo(trans.x(_x), trans.y(_y + h));
		contentByte.stroke();
	}

	protected boolean _isEmpty(){
		if (text == null || ReportUtil.stringLen(text) == 0){
			return true;
		}
		if (region.getWidth() <= 0 || region.getHeight() <= 0){
			return true;
		}
		return false;
	}

	protected static List<Float> _getDistributeMap(
			float w,
			int c,
			float fontSize){
		List<Float> ret = new ArrayList<Float>();
		if (c == 1){
			ret.add(w / 2);
		}else{
			float t = fontSize / 2;
			do{
				ret.add(t);
				t += (w - fontSize) / (c - 1);
			}while(t < w && ret.size() < c);
		}
		return ret;
	}

	protected float _getFitFontSize(List<String> texts){
		String t = null;
		float w = 0f;
		float rw = region.getWidth() - MARGIN_X * 2;
		for(String _t: texts){
			float _w = _getTextWidth(textDesign.font.size, _t);
			if (w < _w){
				w = _w;
				t = _t;
			}
		}
		if (w <= rw){
			return textDesign.font.size;
		}
		int _i = 0;
		while(renderer.setting.shrinkFontSizeMin + _i * renderer.setting.shrinkFontSizeStep < textDesign.font.size){
			_i += 1;
		}
		for(int i = _i - 1;i >= 1;i--){
		  float s = renderer.setting.shrinkFontSizeMin + i * renderer.setting.shrinkFontSizeStep;
		  if (_getTextWidth(s, t) <= rw){
			  return s;
		  }
		}
        return renderer.setting.shrinkFontSizeMin;
	}

	protected float _getTextWidth(
			float fontSize,
			String text){
		List<String> _texts = null;
		if (renderer.setting.gaijiFont != null || gaijiFont != null){
			_texts = _detectGaiji(text);
		}
		float ret = 0;
		if (_texts == null){
			ret = font.getWidthPoint(text, fontSize);
		}else{
			boolean g = false;
			for(String t: _texts){
				if (ReportUtil.stringLen(t) > 0){
					if (!g){
						ret += font.getWidthPoint(t, fontSize);
					}else{
						ret += ReportUtil.stringLen(t) * fontSize;
					}
				}
				g = !g;
			}
		}
		if (textDesign.font.italic){
			ret += fontSize / 6;
		}
		return ret;
	}

	protected static List<String> _detectGaiji(String text){
		List<String> ret = null;
		boolean g = false;
		int last = 0;
		for(int i = 0;i < ReportUtil.stringLen(text);i++){
			if (_isGaiji(text.charAt(i))){
				if (!g){
					if (ret == null){
						ret = new ArrayList<String>();
					}
					ret.add(ReportUtil.subString(text, last, i - last));
					last = i;
					g = true;
				}
			}else{
				if (g){
					ret.add(ReportUtil.subString(text, last, i - last));
					last = i;
					g = false;
				}
			}
		}
		if (ret != null){
			ret.add(ReportUtil.subString(text, last));
		}
		return ret;
	}

	protected static boolean _isGaiji(char c){
		return c >= '\ue000' && c <= '\uf8ff';
	}

	protected static class _TextSplitterByPdfWidth extends TextSplitter{
		private PdfText _pdfText;
		public _TextSplitterByPdfWidth(PdfText pdfText){
			super(!Report.Compatibility._4_34_PdfWrapNoRule);
			this._pdfText = pdfText;
		}
		@Override
		protected int _getNextWidth(String text) {
			float cw = this._pdfText.region.getWidth() - MARGIN_X * 2;
			float fontSize = this._pdfText.textDesign.font.size;
			if (this._pdfText._getTextWidth(fontSize, text) > cw + TOLERANCE){
				for(int i = 2;i <= ReportUtil.stringLen(text);i++){
					if (this._pdfText._getTextWidth(fontSize, ReportUtil.subString(text, 0, i)) > cw + TOLERANCE){
						return i - 1;
					}
				}
			}
			return ReportUtil.stringLen(text);
		}
	}

	protected static class _FixDec{

		public PdfText pdfText;
		public String text1 = "";
		public String text2 = "";
		public String text3 = "";

		public _FixDec(PdfText pdfText){
			this.pdfText = pdfText;
			String t = this.pdfText.text;
			{
				Pattern p = Pattern.compile("([^0-9]*)$");
				Matcher m = p.matcher(t);
				if (m.find()){
					this.text3 = m.group(0);
					t = ReportUtil.subString(t, 0, ReportUtil.stringLen(t) - this.text3.length());
				}
			}
			{
				Pattern p = Pattern.compile("(\\.[0-9]*)?$");
				Matcher m = p.matcher(t);
				if (m.find()){
					this.text2 = m.group(0);
					this.text1 = ReportUtil.subString(t, 0, ReportUtil.stringLen(t) - this.text2.length());
				}
			}
		}

		public String getFullText2(){
			String ret = this.text2;
			if (ret.length() == 0){
				ret = ".";
			}
			while(ret.length() <= this.pdfText.textDesign.decimalPlace){
				ret += "0";
			}
			return ret;
		}

		public String getFullText(){
			return this.text1 + this.getFullText2() + this.text3;
		}

		public void drawText(float fontSize){
			float y = 0;
			switch(pdfText.textDesign.valign){
			case TOP:
				y = 0;
				break;
			case CENTER:
				y = (pdfText.region.getHeight() - fontSize) / 2;
				break;
			case BOTTOM:
				y = pdfText.region.getHeight() - fontSize - MARGIN_BOTTOM;
				break;
			}
			y += OFFSET_Y;
			{
				String t = this.text1 + this.getFullText2();
				String ft = t + this.text3;
				float w = pdfText._getTextWidth(fontSize, t);
				float fw = pdfText._getTextWidth(fontSize, ft);
				float x = 0;
				switch(pdfText.textDesign.halign){
				case LEFT:
					x = MARGIN_X;
					break;
				case CENTER:
					x = (pdfText.region.getWidth() - fw) / 2;
					x = Math.max(x, MARGIN_X);
					break;
				case RIGHT:
					x = pdfText.region.getWidth() - fw - MARGIN_X;
					x = Math.max(x, MARGIN_X);
					break;
				}
				pdfText._draw_preprocess();
				pdfText.contentByte.setFontAndSize(pdfText.font, fontSize);
				pdfText.contentByte.beginText();
				_drawText_aux(fontSize, x, y, this.text1 + this.text2);
				if (this.text3.length() > 0){
					_drawText_aux(fontSize, x + w, y, this.text3);
				}
				pdfText.contentByte.endText();
				if (pdfText.textDesign.font.underline){
					float lw = (fontSize / 13.4f) * pdfText.renderer.setting.underlineWidthCoefficient;
					pdfText._drawUnderline(fontSize, x, y, fw, lw);
				}
			}
		}

		private void _drawText_aux(
				float fontSize,
				float x,
				float y,
				String text){
			float _x = Math.max(x, MARGIN_X);
			float w = pdfText.region.getWidth() - _x - MARGIN_X;
			String t = text;
			if (w < 0){
				return;
			}
			if (pdfText._getTextWidth(fontSize, t) > w + TOLERANCE){
				String _t = "";
				String __t = "";
				for(int i = 0;i <= ReportUtil.stringLen(t);i++){
					__t = ReportUtil.subString(t, 0, i);
					if (pdfText._getTextWidth(fontSize, __t) <= w + TOLERANCE){
						_t = __t;
					}else{
						break;
					}
				}
				t = _t;
			}
			if (ReportUtil.stringLen(t) > 0){
				pdfText._drawText(fontSize, t, _x, y);
			}
		}

	}
}
