package jp.co.systembase.report.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.Report.EPaperType;
import jp.co.systembase.report.Report.EScaleUnit;
import jp.co.systembase.report.ReportUtil;


public class PaperDesign {

	public Report.EPaperType paperType = EPaperType.A4;
	public PaperSizeDesign size = null;
	public PaperMarginDesign margin = null;
	public Report.EScaleUnit scaleUnit = EScaleUnit.POINT;
	public boolean landscape = false;

	public PaperDesign(){
		this(new HashMap<Object, Object>());
	}

	public PaperDesign(Map<?, ?> desc){
		if (desc.containsKey("scale_unit")){
			String t = (String)desc.get("scale_unit");
			if (t.equals("mm")){
				this.scaleUnit = EScaleUnit.MM;
			}else if (t.equals("inch")){
				this.scaleUnit = EScaleUnit.INCH;
			}else{
				this.scaleUnit = EScaleUnit.POINT;
			}
		}
		if (desc.containsKey("type")){
			String t = (String)desc.get("type");
			if (t.equals("a3")){
				this.paperType = EPaperType.A3;
			}else if (t.equals("a5")){
				this.paperType = EPaperType.A5;
			}else if (t.equals("b4")){
				this.paperType = EPaperType.B4;
			}else if (t.equals("b5")){
				this.paperType = EPaperType.B5;
			}else{
				this.paperType = EPaperType.A4;
			}
		}
		this.size = new PaperSizeDesign(this.scaleUnit, this.paperType);
		if (desc.containsKey("size")){
			PaperSizeDesign s = new PaperSizeDesign((Map<?, ?>)desc.get("size"));
			if (s.width > 0f && s.height > 0f){
				if (this.toPoint(s.width) > Report.PAPER_WIDTH_MAX){
					throw new IllegalArgumentException("pagesize.width too large");
				}
				if (this.toPoint(s.height) > Report.PAPER_HEIGHT_MAX){
					throw new IllegalArgumentException("pagesize.height too large");
				}
				this.paperType = EPaperType.CUSTOM;
				this.size = s;
			}
		}
		this.landscape = Cast.toBool(desc.get("landscape"));
        if (desc.containsKey("margin")){
			this.margin = new PaperMarginDesign((Map<?, ?>)desc.get("margin"));
		}else{
			this.margin = new PaperMarginDesign();
		}
	}

	public PaperSizeDesign getActualSize(){
		PaperSizeDesign ret = new PaperSizeDesign();
		if (this.landscape){
			ret.width = this.size.height;
			ret.height = this.size.width;
		}else{
			ret.width = this.size.width;
			ret.height = this.size.height;
		}
		return ret;
	}

	public Region getRegion(){
		Region ret = new Region();
		PaperSizeDesign paperSize = this.getActualSize();
		ret.top = 0;
		ret.left = 0;
		ret.bottom = paperSize.height - this.margin.top - this.margin.bottom;
		ret.right = paperSize.width - this.margin.left - this.margin.right;
		ret.maxBottom = ret.bottom;
		ret.maxRight = ret.right;
		return ret;
	}

	public float toPoint(float v){
		return ReportUtil.toPoint(this.scaleUnit, v);
	}

}
