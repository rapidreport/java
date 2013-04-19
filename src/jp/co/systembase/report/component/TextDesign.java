package jp.co.systembase.report.component;

import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.Report.EHAlign;
import jp.co.systembase.report.Report.EVAlign;

public class TextDesign {

	public static class FontSetting{

		public String name = "gothic";
		public float size = 10f;
		public boolean bold = false;
		public boolean italic = false;
		public boolean underline = false;

		public FontSetting(Map<?, ?> desc){
			this(new ElementDesign(desc));
		}

		public FontSetting(ElementDesign desc){
			if (!desc.isNull("name")){
				this.name = (String)desc.get("name");
			}
			if (!desc.isNull("size")){
				this.size = Cast.toFloat(desc.get("size"));
			}
			this.bold = Cast.toBool(desc.get("bold"));
			this.italic = Cast.toBool(desc.get("italic"));
			this.underline = Cast.toBool(desc.get("underline"));
		}

	}

	public FontSetting font;
	public EHAlign halign = EHAlign.LEFT;
	public EVAlign valign = EVAlign.TOP;
	public String color = null;
	public boolean vertical = false;
	public boolean distribute = false;
	public boolean wrap = false;
	public boolean shrinkToFit = false;
	public int decimalPlace = 0;
	public String xlsFormat = null;

	public TextDesign(
			ReportDesign reportDesign,
			ElementDesign desc){
		if (desc.isNull("font")){
			this.font = reportDesign.defaultFont;
		}else{
			this.font = new FontSetting(desc.child("font"));
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
	}

}
