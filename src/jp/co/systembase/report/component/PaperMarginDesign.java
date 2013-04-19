package jp.co.systembase.report.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportUtil;

public class PaperMarginDesign {

	public float top;
	public float left;
	public float bottom;
	public float right;
	public boolean oddReverse;

	public PaperMarginDesign(){
		this(new HashMap<Object, Object>());
	}

	public PaperMarginDesign(Map<?, ?>desc){
		this.top = Cast.toFloat(desc.get("top"));
		this.left = Cast.toFloat(desc.get("left"));
		this.bottom = Cast.toFloat(desc.get("bottom"));
		this.right = Cast.toFloat(desc.get("right"));
		this.oddReverse = Cast.toBool(desc.get("odd_reverse"));
	}

	public PaperMarginDesign toPoint(PaperDesign paperDesign){
		return this.toPoint(paperDesign.scaleUnit);
	}

	public PaperMarginDesign toPoint(Report.EScaleUnit scaleUnit){
		PaperMarginDesign ret = new PaperMarginDesign();
	    ret.top = ReportUtil.toPoint(scaleUnit, this.top);
	    ret.left = ReportUtil.toPoint(scaleUnit, this.left);
	    ret.bottom = ReportUtil.toPoint(scaleUnit, this.bottom);
	    ret.right = ReportUtil.toPoint(scaleUnit, this.right);
	    ret.oddReverse = this.oddReverse;
	    return ret;
	}

}
