package jp.co.systembase.report.renderer.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.renderer.RenderUtil;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

public class PdfRenderUtil {

	private static final float TOLERANCE = 0.1f;
	private static final float OFFSET_Y = -0.5f;
	private static final float MARGIN_X = 2.0f;
	private static final float MARGIN_BOTTOM = 2.0f;

	private static final String VERTICAL_ROTATE_CHARS = "～…‥｜ーｰ(){}[]<>（）｛｝「」＜＞";
	private static final String VERTICAL_SHIFT_CHARS = "。、";

	public static void drawText(
			PdfRenderer renderer,
			Region region,
			TextDesign textDesign,
			String text){
		if (text == null || text.length() == 0){
			return;
		}
		if (region.getWidth() <= 0 || region.getHeight() <= 0){
			return;
		}
		PdfContentByte cb = renderer.writer.getDirectContent();
		cb.saveState();
		try{
			if (textDesign.color != null){
				short[] c = RenderUtil.getColorRGB(textDesign.color);
				if (c != null){
					cb.setRGBColorFill(c[0], c[1], c[2]);
					cb.setRGBColorStroke(c[0], c[1], c[2]);
				}
			}
			BaseFont font = renderer.setting.getFont(textDesign.font.name);
			if (textDesign.font.bold){
				cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
				if (!Report.Compatibility._4_6_PdfFontBold){
					cb.setLineWidth(textDesign.font.size * 0.01f);
                }
			}else{
				cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
			}
			if (textDesign.distribute){
				if (textDesign.vertical){
					_drawText_distribute_vertical(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
				}else{
					_drawText_distribute(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
				}
			}else if (textDesign.vertical){
				if (textDesign.shrinkToFit){
					_drawText_vertical_shrink(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
				}else if (textDesign.wrap){
					_drawText_vertical_wrap(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
				}else{
					_drawText_vertical(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
				}
			}else if (textDesign.decimalPlace > 0){
				if (textDesign.shrinkToFit){
					_drawText_fixdec_shrink(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
				}else{
					_drawText_fixdec(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
				}
			}else if (textDesign.shrinkToFit){
				_drawText_shrink(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
			}else if (textDesign.wrap){
				_drawText_wrap(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
			}else{
				_drawText(cb, region, renderer.setting, renderer.trans, textDesign, font, text);
			}
		}finally{
			cb.restoreState();
		}
	}

	public static void _drawText_distribute(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,			
			String text){
		float fontSize = textDesign.font.size;
		float y = 0;
		switch(textDesign.valign){
		case TOP:
			y = 0;
			break;
		case CENTER:
			y = (region.getHeight() - fontSize) / 2;
			break;
		case BOTTOM:
			y = region.getHeight() - fontSize - MARGIN_BOTTOM;
			break;
		}
		y += OFFSET_Y;
		{
			List<Float> m  = getDistributeMap(region.getWidth() - MARGIN_X * 2, text.length(), fontSize);
            cb.setFontAndSize(font, fontSize);
            cb.beginText();
            for(int i = 0;i < text.length();i++){
            	String c = text.substring(i, i + 1);
                showText(cb, region, setting, trans, textDesign, font, fontSize, c,
                              m.get(i) - getTextWidth(setting, textDesign, font, fontSize, c) / 2 + MARGIN_X, y);
            }
            cb.endText();
        }
		if (textDesign.font.underline){
			drawUnderline(cb, region, trans, fontSize, MARGIN_X, y, region.getWidth() - MARGIN_X * 2);
		}
	}

	public static void _drawText_distribute_vertical(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		float fontSize = textDesign.font.size;
		float x = 0;
		switch(textDesign.halign){
		case LEFT:
			x = fontSize / 2 + MARGIN_X;
			break;
		case CENTER:
			x = region.getWidth() / 2;
			break;
		case RIGHT:
			x = region.getWidth() - fontSize / 2 - MARGIN_X;
			break;
		}
        if (textDesign.font.underline){
        	drawVerticalUnderLine(cb, region, trans, fontSize, x + fontSize / 2, 0, region.getHeight());
        }
		{
			List<Float> m = getDistributeMap(region.getHeight() - MARGIN_BOTTOM, text.length(), fontSize);
			cb.setFontAndSize(font, fontSize);
			cb.beginText();
			for(int i = 0;i < text.length();i++){
				showVerticalChar(cb, region, setting, trans, textDesign, font, fontSize, text.substring(i, i + 1),
						x, m.get(i) - fontSize / 2 + OFFSET_Y);
            }
            cb.endText();
		}
	}

	public static void _drawText_vertical(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		List<String> texts = splitVerticalText(region, textDesign, text, false);
		_drawText_vertical_aux(cb, region, setting, trans, textDesign, font, textDesign.font.size, texts);
	}

	public static void _drawText_vertical_shrink(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		float fontSize = textDesign.font.size;
		List<String> texts = splitVerticalText(region, textDesign, text, false);
		{
			int m = 0;
			for(String t: texts){
				if (m < t.length()){
					m = t.length();
				}
			}
			if (m > 0){
				float _fontSize = Math.max((region.getHeight() - MARGIN_BOTTOM) / m, setting.shrinkFontSizeMin);
				fontSize = Math.min(fontSize, _fontSize);
			}
        }
        _drawText_vertical_aux(cb, region, setting, trans, textDesign, font, fontSize, texts);
	}

	public static void _drawText_vertical_wrap(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		List<String> texts = splitVerticalText(region, textDesign, text, true);
		_drawText_vertical_aux(cb, region, setting, trans, textDesign, font, textDesign.font.size, texts);
	}

	public static void _drawText_fixdec(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		_FixDec fd = new _FixDec(text);
		List<String> texts = new ArrayList<String>();
		texts.add(fd.getFullText(textDesign.decimalPlace));
		fd.drawText(cb, region, setting, trans, textDesign, font, textDesign.font.size);
	}

	public static void _drawText_fixdec_shrink(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		_FixDec fd = new _FixDec(text);
		List<String> texts = new ArrayList<String>();
		texts.add(fd.getFullText(textDesign.decimalPlace));
		float fontSize = getFitFontSize(region, setting, font, textDesign, texts);
		fd.drawText(cb, region, setting, trans, textDesign, font, fontSize);
	}

	public static void _drawText_shrink(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		List<String> texts = splitText(region, setting, textDesign, font, text, false);
		float fontSize = getFitFontSize(region, setting, font, textDesign, texts);
		_drawText_aux(cb, region, setting, trans, textDesign, font, fontSize, texts);
	}

	public static void _drawText_wrap(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		List<String> texts = splitText(region, setting, textDesign, font, text, true);
		_drawText_aux(cb, region, setting, trans, textDesign, font, textDesign.font.size, texts);
	}

	public static void _drawText(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			String text){
		List<String> texts = splitText(region, setting, textDesign, font, text, false);
		_drawText_aux(cb, region, setting, trans, textDesign, font, textDesign.font.size, texts);
	}

    public static void _drawText_aux(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
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
			float w = getTextWidth(setting, textDesign, font, fontSize, t);
			{
				float cw = region.getWidth() - MARGIN_X * 2;
				if (w > cw){
					String _t = "";
					float _w = 0;
					for(int j = 1;j <= t.length();j++){
						String __t = t.substring(0, j);
						float __w = getTextWidth(setting, textDesign, font, fontSize, __t);
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
			cb.setFontAndSize(font, fontSize);
			cb.beginText();
			showText(cb, region, setting, trans, textDesign, font, fontSize, t, x, y);
			cb.endText();
			if (textDesign.font.underline){
				drawUnderline(cb, region, trans, fontSize, x, y, w);
			}
			y += fontSize;
		}
	}

	public static void _drawText_vertical_aux(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
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
				drawVerticalUnderLine(cb, region, trans, fontSize, x + fontSize / 2, y, _yc * fontSize);
			}
			cb.setFontAndSize(font, fontSize);
			cb.beginText();
			for(int j = 0;j < _yc;j++){
				showVerticalChar(cb, region, setting, trans, textDesign, font, fontSize, t.substring(j, j + 1), x, y);
				y += fontSize;
			}
			cb.endText();
			x -= fontSize;
		}
	}

	private static class _FixDec{

		public String text1 = "";
		public String text2 = "";
		public String text3 = "";

		public _FixDec(String text){
			String t = text;
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

		public void drawText(
				PdfContentByte cb,
				Region region,
				PdfRendererSetting setting,
				PdfRenderer.Trans trans,
				TextDesign textDesign,
				BaseFont font,
				float fontSize){
			float y = 0;
			switch(textDesign.valign){
			case TOP:
				y = 0;
				break;
			case CENTER:
				y = (region.getHeight() - fontSize) / 2;
				break;
			case BOTTOM:
				y = region.getHeight() - fontSize - MARGIN_BOTTOM;
				break;
			}
			y += OFFSET_Y;
			{
				String t = this.text1 + this.getFullText2(textDesign.decimalPlace);
				String ft = t + this.text3;
				float w = getTextWidth(setting, textDesign, font, fontSize, t);
				float fw = getTextWidth(setting, textDesign, font, fontSize, ft);
				float x = 0;
				switch(textDesign.halign){
				case LEFT:
					x = MARGIN_X;
					break;
				case CENTER:
					x = (region.getWidth() - fw) / 2;
					x = Math.max(x, MARGIN_X);
					break;
				case RIGHT:
					x = region.getWidth() - fw - MARGIN_X;
					x = Math.max(x, MARGIN_X);
					break;
				}
				cb.setFontAndSize(font, fontSize);
				cb.beginText();
				drawText_aux(cb, region, setting, trans, textDesign, font, fontSize, x, y, this.text1 + this.text2);
				if (this.text3.length() > 0){
					drawText_aux(cb, region, setting, trans, textDesign, font, fontSize, x + w, y, this.text3);
				}
				cb.endText();
				if (textDesign.font.underline){
					drawUnderline(cb, region, trans, fontSize, x, y, fw);
				}
			}
		}

		private void drawText_aux(
				PdfContentByte cb,
				Region region,
				PdfRendererSetting setting,
				PdfRenderer.Trans trans,
				TextDesign textDesign,
				BaseFont font,
				float fontSize,
				float x,
				float y,
				String text){
			float _x = Math.max(x, MARGIN_X);
			float w = region.getWidth() - _x - MARGIN_X;
			String t = text;
			if (w < 0){
				return;
			}
			if (getTextWidth(setting, textDesign, font, fontSize, t) > w + TOLERANCE){
				String _t = "";
				String __t = "";
				for(int i = 0;i <= t.length();i++){
					__t = t.substring(0, i);
					if (getTextWidth(setting, textDesign, font, fontSize, __t) <= w + TOLERANCE){
						_t = __t;
					}else{
						break;
					}
				}
				t = _t;
			}
			if (t.length() > 0){
				showText(cb, region, setting, trans, textDesign, font, fontSize, t, _x, y);
			}
		}

	}

	private static List<Float> getDistributeMap(
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

	private static float getFitFontSize(
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
			float _w = getTextWidth(setting, textDesign, font, fontSize, _t);
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
		  if (getTextWidth(setting, textDesign, font, s, t) <= rw){
			  return s;
		  }
		}
        return setting.shrinkFontSizeMin;
	}

	private static List<String> splitText(
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
			if (wrap && getTextWidth(setting, textDesign, font, fontSize, t) > cw + TOLERANCE){
				String _t  = t;
				while(_t.length() > 0){
					int i;
					for(i = 2;i <= _t.length();i++){
						if (getTextWidth(setting, textDesign, font, fontSize, _t.substring(0, i)) > cw + TOLERANCE){
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

	private static List<String> splitVerticalText(
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

	private static float getTextWidth(
			PdfRendererSetting setting,
			TextDesign textDesign,
			BaseFont font,
			float fontSize,
			String text){
		List<String> _texts = null;
		if (setting.gaijiFont != null){
			_texts = detectGaiji(text);
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

	private static void setTextMatrix(
			PdfContentByte cb,
			Region region,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			float fontSize,
			float x,
			float y){
		float _x = region.left + x;
		float _y = (region.top + y + fontSize) - (fontSize / 13.4f);
		if (textDesign.font.italic){
			cb.setTextMatrix(1f, 0f, 0.3f, 1f, trans.x(_x), trans.y(_y));
		}else{
			cb.setTextMatrix(trans.x(_x), trans.y(_y));
		}
	}

	private static void setRotateTextMatrix(
			PdfContentByte cb,
			Region region,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			float fontSize,
			float x,
			float y){
		float _x = region.left + x;
		float _y = (region.top + y + fontSize) - (fontSize / 13.4f);
		if (textDesign.font.italic){
			cb.setTextMatrix(-0.3f, -1, 1, 0, trans.x(_x), trans.y(_y));
		}else{
			cb.setTextMatrix(0, -1, 1, 0, trans.x(_x), trans.y(_y));
		}
	}

	private static void showText(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			float fontSize,
			String text,
			float x,
			float y){
		List<String> _texts = null;
		if (setting.gaijiFont != null){
			_texts = detectGaiji(text);
		}
		if (_texts == null){
			setTextMatrix(cb, region, trans, textDesign, fontSize, x, y);
			cb.showText(text);
		}else{
			boolean gaiji = false;
			float _x = x;
			for(String t: _texts){
				if (t.length() > 0){
					if (!gaiji){
						setTextMatrix(cb, region, trans, textDesign, fontSize, _x, y);
						cb.showText(t);
						_x += font.getWidthPoint(t, fontSize);
					}else{
						cb.setFontAndSize(setting.gaijiFont, fontSize);
						for(int i = 0;i < t.length();i++){
							setTextMatrix(cb, region, trans, textDesign, fontSize, _x, y);
							cb.showText(t.substring(i, i + 1));
							_x += fontSize;
						}
						cb.setFontAndSize(font, fontSize);
					}
				}
				gaiji = !gaiji;
			}
		}
	}

	private static void showVerticalChar(
			PdfContentByte cb,
			Region region,
			PdfRendererSetting setting,
			PdfRenderer.Trans trans,
			TextDesign textDesign,
			BaseFont font,
			float fontSize,
			String c,
			float x,
			float y){
		boolean gaiji = false;
		if (setting.gaijiFont != null && isGaiji(c.charAt(0))){
			cb.setFontAndSize(setting.gaijiFont, fontSize);
			gaiji = true;
		}
		if (VERTICAL_ROTATE_CHARS.indexOf(c) >= 0){
			setRotateTextMatrix(cb, region, trans, textDesign, 
					fontSize, x - fontSize / 3, 
					y - getTextWidth(setting, textDesign, font, fontSize, c));
		}else if (VERTICAL_SHIFT_CHARS.indexOf(c) >= 0){
			setTextMatrix(cb, region, trans, textDesign, fontSize, x, y - fontSize / 2);
		}else{
			setTextMatrix(cb, region, trans, textDesign, 
					fontSize, x - getTextWidth(setting, textDesign, font, fontSize, c) / 2, 
					y);
		}
		cb.showText(c);
		if (gaiji){
			cb.setFontAndSize(font, fontSize);
		}
	}

	private static void drawUnderline(
			PdfContentByte cb,
			Region region,
			PdfRenderer.Trans trans,
			float fontSize,
			float x,
			float y,
			float width){
		float lw = fontSize / 13.4f;
		float _x1 = region.left + x;
		float _x2= _x1 + width;
		float _y = (region.top + y + fontSize) - OFFSET_Y;
		_x1 = Math.max(_x1, region.left + MARGIN_X);
		_x2 = Math.min(_x2, region.right - MARGIN_X);
		if (_x1 < _x2){
			cb.setLineWidth(lw);
			cb.moveTo(trans.x(_x1), trans.y(_y));
			cb.lineTo(trans.x(_x2), trans.y(_y));
			cb.stroke();
		}
    }

	private static void drawVerticalUnderLine(
			PdfContentByte cb,
			Region region,
			PdfRenderer.Trans trans,
			float fontSize,
			float x,
			float y,
			float h){
		float lw = fontSize / 13.4f;
		float _x = region.left + x;
		float _y = (region.top + y) - OFFSET_Y;
		cb.setLineWidth(lw);
		cb.moveTo(trans.x(_x), trans.y(_y));
		cb.lineTo(trans.x(_x), trans.y(_y + h));
		cb.stroke();
	}

	private static List<String> detectGaiji(String text){
		List<String> ret = null;
		boolean g = false;
		int last = 0;
		for(int i = 0;i < text.length();i++){
			if (isGaiji(text.charAt(i))){
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

	private static boolean isGaiji(char c){
		return c >= '\ue000' && c <= '\uf8ff';
	}


}