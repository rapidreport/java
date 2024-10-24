package jp.co.systembase.report.component;

import java.math.BigDecimal;
import java.text.BreakIterator;
import java.util.List;
import java.util.Locale;

import jp.co.systembase.core.Cast;
import jp.co.systembase.core.Round;
import jp.co.systembase.report.Report.EHAlign;
import jp.co.systembase.report.Report.EVAlign;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.ReportUtil;

public class TextDesign {

	public FontDesign font;
	public EHAlign halign = EHAlign.LEFT;
	public EVAlign valign = EVAlign.TOP;
	public String color = null;
	public boolean vertical = false;
	public boolean distribute = false;
	public boolean wrap = false;
	public boolean shrinkToFit = false;
	public int decimalPlace = 0;
	public float charSpacing = 0;
	public String xlsFormat = null;
	public MonospacedFontsDesign.DetailDesign monospacedFont = null;
	public float boldWidth = 0;

	public TextDesign(
			ReportDesign reportDesign,
			ElementDesign desc){
		if (desc.isNull("font")){
			this.font = reportDesign.defaultFontDesign;
		}else{
			this.font = new FontDesign(desc.child("font"));
		}
		if (!desc.isNull("halign")){
			String ha = (String)desc.get("halign");
			if (ha.equals("left")){
				this.halign = EHAlign.LEFT;
			}
			if (ha.equals("center")){
				this.halign = EHAlign.CENTER;
			}
			if (ha.equals("right")){
				this.halign = EHAlign.RIGHT;
			}
		}
		if (!desc.isNull("valign")){
			String va = (String)desc.get("valign");
			if (va.equals("top")){
				this.valign = EVAlign.TOP;
			}
			if (va.equals("center")){
				this.valign = EVAlign.CENTER;
			}
			if (va.equals("bottom")){
				this.valign = EVAlign.BOTTOM;
			}
		}
		this.color = (String)desc.get("color");
		this.wrap = Cast.toBool(desc.get("wrap"));
		this.vertical = Cast.toBool(desc.get("vertical"));
		this.distribute = Cast.toBool(desc.get("distribute"));
		this.shrinkToFit = Cast.toBool(desc.get("shrink_to_fit"));
		if (!desc.isNull("decimal_place")){
			this.decimalPlace = Cast.toInt(desc.get("decimal_place"));
		}
		this.xlsFormat = (String)desc.get("xls_format");
		this.boldWidth = Cast.toFloat(desc.get("bold_width"));
		if (!desc.isNull("char_spacing")){
			this.charSpacing = Math.max(Cast.toFloat(desc.get("char_spacing")), 0);
		}else{
			this.charSpacing = Math.max(reportDesign.defaultCharSpacing, 0);
		}
		this.monospacedFont = reportDesign.monospacedFontsDesign.get(this.font);
	}

	public float getMonospacedWidth(String str){
		return getMonospacedWidth(str, font.size);
	}
	public float getMonospacedWidth(String str, float fontSize){
		float ret = fontSize / 3;
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int last = bi.first();
		while (bi.next() != BreakIterator.DONE){
			ret += monoWidth(str.substring(last, bi.current()), fontSize);
			last = bi.current();
		}
		return ret;
	}

	public float getPdfCharSpacing(String str, float fontSize){
		float cs = 0;
		int c = 0;
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int last = bi.first();
		while (bi.next() != BreakIterator.DONE){
			if (ReportUtil.isSingleChar(str.substring(last, bi.current()))){
				cs += _monoHalfWidth() - 0.5;
			}else{
				cs += _monoFullWidth() - 1.0;
			}
			last = bi.current();
			c++;
		}
		if (c > 0){
			return (cs / c) * fontSize;
		}else{
			return 0;
		}
	}

	public float getMonospacedFitFontSize(List<String> texts, float width, float minSize){
		float w = 0;
		for(String t: texts){
			w = Math.max(getMonospacedWidth(t), w);
		}
		if (w <= width){
			return font.size;
		}else{
			return Math.max(Round.roundDown(new BigDecimal(font.size * width / w), -1).floatValue(), minSize);
		}
	}

	public int getMonospacedDrawableLen(String str, float width){
		return getMonospacedDrawableLen(str, width, -1);
	}
	public int getMonospacedDrawableLen(String str, float width, int maxLen){
		float w = font.size / 3;
		BreakIterator bi = BreakIterator.getCharacterInstance(Locale.US);
		bi.setText(str);
		int last = bi.first();
		int i = 0;
		while (bi.next() != BreakIterator.DONE){
			w += monoWidth(str.substring(last, bi.current()), font.size);
			if ((i > 0 && w >= width) || (i == maxLen)){
				return i;
			}
			i++;
			last = bi.current();
		}
		return i;
	}

	private float _monoHalfWidth(){
		return monospacedFont.halfWidth * (1 + charSpacing);
	}

	private float _monoFullWidth(){
		return monospacedFont.fullWidth * (1 + charSpacing);
	}

	public float monoWidth(String c, float fontSize){
		if (ReportUtil.isSingleChar(c)){
			return _monoHalfWidth() * fontSize;
		}else{
			return _monoFullWidth() * fontSize;
		}
	}
}
