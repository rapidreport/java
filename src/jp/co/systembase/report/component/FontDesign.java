package jp.co.systembase.report.component;

import java.util.Map;

import jp.co.systembase.core.Cast;

public class FontDesign {

	public String name = "gothic";
	public float size = 10f;
	public boolean bold = false;
	public boolean italic = false;
	public boolean underline = false;

	public FontDesign(Map<?, ?> desc){
		this(new ElementDesign(desc));
	}

	public FontDesign(ElementDesign desc){
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
