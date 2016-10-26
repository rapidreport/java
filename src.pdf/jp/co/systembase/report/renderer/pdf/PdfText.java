package jp.co.systembase.report.renderer.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.renderer.RenderUtil;

public class PdfText {

	public PdfRenderer renderer;
	public Region region;
	public TextDesign textDesign;
	public String text;
	public PdfContentByte contentByte;
	public BaseFont font;
	public List<Float> textMatrix = null;
	
	protected static final float TOLERANCE = 0.1f;
	protected static final float OFFSET_Y = -0.5f;
	protected static final float MARGIN_X = 2.0f;
	protected static final float MARGIN_BOTTOM = 2.0f;

	protected static final String VERTICAL_ROTATE_CHARS = "～…‥｜ーｰ(){}[]<>（）｛｝「」＜＞";
	protected static final String VERTICAL_SHIFT_CHARS = "。、";
	
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
		List<String> texts = _splitByCr(region, textDesign, text, false);
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
			List<Float> m  = _getDistributeMap(region.getWidth() - MARGIN_X * 2, t.length(), fontSize);
            contentByte.setFontAndSize(font, fontSize);
            contentByte.beginText();
            for(int j = 0;j < t.length();j++){
            	String c = t.substring(j, j + 1);
                _drawText(fontSize, c, m.get(j) - _getTextWidth(renderer.setting, textDesign, font, fontSize, c) / 2 + MARGIN_X, y);
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
		List<String> texts = _splitByCr(region, textDesign, text, false);
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
				List<Float> m = _getDistributeMap(region.getHeight() - MARGIN_BOTTOM, t.length(), fontSize);
				contentByte.setFontAndSize(font, fontSize);
				contentByte.beginText();
				for(int j = 0;j < t.length();j++){
					_drawVerticalChar(fontSize, t.substring(j, j + 1),
							x, m.get(j) - fontSize / 2 + OFFSET_Y);
	            }
	            contentByte.endText();
			}
			x -= fontSize;
		}
	}

	protected void _draw_vertical(){
		List<String> texts = _splitByCr(region, textDesign, text, false);
		_draw_vertical_aux(textDesign.font.size, texts);
	}

	protected void _draw_verticalShrink(){
		float fontSize = textDesign.font.size;
		List<String> texts = _splitByCr(region, textDesign, text, false);
		{
			int m = 0;
			for(String t: texts){
				if (m < t.length()){
					m = t.length();
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
		List<String> texts = _splitByCr(region, textDesign, text, true);
		_draw_vertical_aux(textDesign.font.size, texts);
	}

	protected void _draw_fixdec(){
		_FixDec fd = new _FixDec(this);
		List<String> texts = new ArrayList<String>();
		texts.add(fd.getFullText(textDesign.decimalPlace));
		fd.drawText(textDesign.font.size);
	}

	protected void _draw_fixdecShrink(){
		_FixDec fd = new _FixDec(this);
		List<String> texts = new ArrayList<String>();
		texts.add(fd.getFullText(textDesign.decimalPlace));
		float fontSize = _getFitFontSize(region, renderer.setting, font, textDesign, texts);
		fd.drawText(fontSize);
	}

	protected void _draw_shrink(){
		List<String> texts = _splitText(region, renderer.setting, textDesign, font, text, false);
		float fontSize = _getFitFontSize(region, renderer.setting, font, textDesign, texts);
		_draw_aux(fontSize, texts);
	}

	protected void _draw_wrap(){
		List<String> texts = _splitText(region, renderer.setting, textDesign, font, text, true);
		_draw_aux(textDesign.font.size, texts);
	}

	protected void _draw(){
		List<String> texts = _splitText(region, renderer.setting, textDesign, font, text, false);
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
			float w = _getTextWidth(renderer.setting, textDesign, font, fontSize, t);
			{
				float cw = region.getWidth() - MARGIN_X * 2;
				if (w > cw){
					String _t = "";
					float _w = 0;
					for(int j = 1;j <= t.length();j++){
						String __t = t.substring(0, j);
						float __w = _getTextWidth(renderer.setting, textDesign, font, fontSize, __t);
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
				y = (region.getHeight() - fontSize * t.length()) / 2;
				y = Math.max(y, 0);
				break;
			case BOTTOM:
				y = region.getHeight() - fontSize * t.length() - MARGIN_BOTTOM;
				y = Math.max(y, 0);
				break;
			}
			y += OFFSET_Y;
			int _yc = Math.min(t.length(), rows);
			if (textDesign.font.underline){
				float lw = (fontSize / 13.4f) * renderer.setting.underlineWidthCoefficient;
				_drawVerticalUnderLine(fontSize, x + fontSize / 2, y, _yc * fontSize, lw);
			}
			contentByte.setFontAndSize(font, fontSize);
			contentByte.beginText();
			for(int j = 0;j < _yc;j++){
				_drawVerticalChar(fontSize, t.substring(j, j + 1), x, y);
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
				contentByte.setTextMatrix(1f, 0f, 0.3f, 1f, trans.x(_x), trans.y(_y));
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
		float _y = (region.top + y + fontSize) - (fontSize / 13.4f);
		if (textDesign.font.italic){
			contentByte.setTextMatrix(-0.3f, -1, 1, 0, trans.x(_x), trans.y(_y));
		}else{
			contentByte.setTextMatrix(0, -1, 1, 0, trans.x(_x), trans.y(_y));
		}
	}
	
	protected void _drawText(
			float fontSize,
			String text,
			float x,
			float y){
		List<String> _texts = null;
		if (renderer.setting.gaijiFont != null){
			_texts = _detectGaiji(text);
		}
		if (_texts == null){
			_setTextMatrix(fontSize, x, y);
			contentByte.showText(text);
		}else{
			boolean gaiji = false;
			float _x = x;
			for(String t: _texts){
				if (t.length() > 0){
					if (!gaiji){
						_setTextMatrix(fontSize, _x, y);
						contentByte.showText(t);
						_x += font.getWidthPoint(t, fontSize);
					}else{
						contentByte.setFontAndSize(renderer.setting.gaijiFont, fontSize);
						for(int i = 0;i < t.length();i++){
							_setTextMatrix(fontSize, _x, y);
							contentByte.showText(t.substring(i, i + 1));
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
					fontSize, x - fontSize / 3, 
					y - _getTextWidth(renderer.setting, textDesign, font, fontSize, c));
		}else if (VERTICAL_SHIFT_CHARS.indexOf(c) >= 0){
			_setTextMatrix(fontSize, x, y - fontSize / 2);
		}else{
			_setTextMatrix( 
			  fontSize, x - _getTextWidth(renderer.setting, textDesign, font, fontSize, c) / 2, y); 
					
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
		if (text == null || text.length() == 0){
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

	protected static float _getFitFontSize(
			Region region,
			PdfRendererSetting setting,
			BaseFont font,
			TextDesign textDesign,
			List<String> texts){
		float fontSize = textDesign.font.size;
		String t = null;
		float w = 0f;
		float rw = region.getWidth() - MARGIN_X * 2;
		for(String _t: texts){
			float _w = _getTextWidth(setting, textDesign, font, fontSize, _t);
			if (w < _w){
				w = _w;
				t = _t;
			}
		}
		if (w <= rw){
			return fontSize;
		}
		int _i = 0;
		while(setting.shrinkFontSizeMin + _i * 0.5 < fontSize){
			_i += 1;
		}
		for(int i = _i - 1;i >= 1;i--){
		  float s = setting.shrinkFontSizeMin + i * 0.5f;
		  if (_getTextWidth(setting, textDesign, font, s, t) <= rw){
			  return s;
		  }
		}
        return setting.shrinkFontSizeMin;
	}

	protected static List<String> _splitText(
			Region region,
			PdfRendererSetting setting,
			TextDesign textDesign,
			BaseFont font,
			String text,
			boolean wrap){
		float fontSize = textDesign.font.size;
		float cw = region.getWidth() - MARGIN_X * 2;
		List<String> ret = new ArrayList<String>();
		for(String t: text.split("\n")){
			t = t.replace("\r", "");
			if (wrap && _getTextWidth(setting, textDesign, font, fontSize, t) > cw + TOLERANCE){
				String _t  = t;
				while(_t.length() > 0){
					int i;
					for(i = 2;i <= _t.length();i++){
						if (_getTextWidth(setting, textDesign, font, fontSize, _t.substring(0, i)) > cw + TOLERANCE){
							break;
						}
					}
					ret.add(_t.substring(0, i - 1));
					_t = _t.substring(i - 1);
				}
			}else{
				ret.add(t);
			}
		}
		return ret;
	}

	protected static List<String> _splitByCr(
			Region region,
			TextDesign textDesign,
			String text,
			boolean wrap){
		float fontSize = textDesign.font.size;
		float ch = region.getHeight();
		int yc = (int)((ch + TOLERANCE) / fontSize);
		List<String> ret = new ArrayList<String>();
		for(String t: text.split("\n")){
			t = t.replace("\r", "");
			if (wrap){
				while(true){
					if (t.length() > yc){
						ret.add(t.substring(0, yc));
						t = t.substring(yc);
					}else{
						ret.add(t);
						break;
					}
				}
			}else{
				ret.add(t);
			}
		}
		return ret;
	}

	protected static float _getTextWidth(
			PdfRendererSetting setting,
			TextDesign textDesign,
			BaseFont font,
			float fontSize,
			String text){
		List<String> _texts = null;
		if (setting.gaijiFont != null){
			_texts = _detectGaiji(text);
		}
		float ret = 0;
		if (_texts == null){
			ret = font.getWidthPoint(text, fontSize);
		}else{
			boolean g = false;
			for(String t: _texts){
				if (t.length() > 0){
					if (!g){
						ret += font.getWidthPoint(t, fontSize);
					}else{
						ret += t.length() * fontSize;
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
		for(int i = 0;i < text.length();i++){
			if (_isGaiji(text.charAt(i))){
				if (!g){
					if (ret == null){
						ret = new ArrayList<String>();
					}
					ret.add(text.substring(last, i));
					last = i;
					g = true;
				}
			}else{
				if (g){
					ret.add(text.substring(last, i));
					last = i;
					g = false;
				}
			}
		}
		if (ret != null){
			ret.add(text.substring(last));
		}
		return ret;
	}

	protected static boolean _isGaiji(char c){
		return c >= '\ue000' && c <= '\uf8ff';
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
					t = t.substring(0, t.length() - this.text3.length());
				}
			}
			{
				Pattern p = Pattern.compile("(\\.[0-9]*)?$");
				Matcher m = p.matcher(t);
				if (m.find()){
					this.text2 = m.group(0);
					this.text1 = t.substring(0, t.length() - this.text2.length());
				}
			}
		}

		public String getFullText2(int width){
			String ret = this.text2;
			if (ret.length() == 0){
				ret = ".";
			}
			while(ret.length() <= width){
				ret += "0";
			}
			return ret;
		}

		public String getFullText(int width){
			return this.text1 + this.getFullText2(width) + this.text3;
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
				String t = this.text1 + this.getFullText2(pdfText.textDesign.decimalPlace);
				String ft = t + this.text3;
				float w = _getTextWidth(pdfText.renderer.setting, pdfText.textDesign, pdfText.font, fontSize, t);
				float fw = _getTextWidth(pdfText.renderer.setting, pdfText.textDesign, pdfText.font, fontSize, ft);
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
			if (_getTextWidth(pdfText.renderer.setting, pdfText.textDesign, pdfText.font, fontSize, t) > w + TOLERANCE){
				String _t = "";
				String __t = "";
				for(int i = 0;i <= t.length();i++){
					__t = t.substring(0, i);
					if (_getTextWidth(pdfText.renderer.setting, pdfText.textDesign, pdfText.font, fontSize, __t) <= w + TOLERANCE){
						_t = __t;
					}else{
						break;
					}
				}
				t = _t;
			}
			if (t.length() > 0){
				pdfText._drawText(fontSize, t, _x, y);
			}
		}

	}
}
