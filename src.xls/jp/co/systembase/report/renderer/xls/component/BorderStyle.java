package jp.co.systembase.report.renderer.xls.component;

import java.math.BigDecimal;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;

public class BorderStyle {

	public enum ELineStyle{
		THIN,
		HAIR,
		DOT,
		DASH,
		DASHDOT,
		DASHDOTDOT,
		MEDIUM,
		MEDIUM_DASH,
		MEDIUM_DASHDOT,
		MEDIUM_DASHDOTDOT,
		SLANTED_DASHDOT,
		THICK,
		DOUBLE
	}
	public ELineStyle lineStyle = ELineStyle.THIN;
	public String lineColor = null;

	private BorderStyle(){};

	public static BorderStyle getInstance(ElementDesign design, ReportDesign reportDesign){
		BorderStyle ret = null;
		if (!design.isNull("xls_line_style")){
			String ls = (String)design.get("xls_line_style"); 
			if (ls.equals("thin")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.THIN;
			}else if (ls.equals("hair")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.HAIR;
			}else if (ls.equals("dot")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.DOT;
			}else if (ls.equals("dash")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.DASH;
			}else if (ls.equals("dashdot")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.DASHDOT;
			}else if (ls.equals("medium")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.MEDIUM;
			}else if (ls.equals("medium_dash")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.MEDIUM_DASH;
			}else if (ls.equals("medium_dashdot")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.MEDIUM_DASHDOT;
			}else if (ls.equals("medium_dashdotdot")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.MEDIUM_DASHDOTDOT;
			}else if (ls.equals("slanted_dashdot")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.SLANTED_DASHDOT;
			}else if (ls.equals("thick")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.THICK;
			}else if (ls.equals("double")){
				ret = new BorderStyle();
				ret.lineStyle = ELineStyle.DOUBLE;
			}
		}
		if (ret == null){
			float lw = reportDesign.defaultLineWidth;
			if (!design.isNull("line_width")){
				lw = ((BigDecimal)design.get("line_width")).floatValue();
			}
			if (lw == 0f){
				return null;
			}
			ret = new BorderStyle();
			if (Cast.toBool(design.get("doublet"))){
				ret.lineStyle = ELineStyle.DOUBLE;
			}else{
				String ls = "solid";
				if (!design.isNull("line_style")){
					ls = (String)design.get("line_style");
				}
				if (ls.equals("dot")){
					if (lw >= 2.0f){
						ret.lineStyle = ELineStyle.MEDIUM_DASH;
					}else{
						ret.lineStyle = ELineStyle.DOT;
					}
				}else if (ls.equals("dash")){
					if (lw >= 2.0f){
						ret.lineStyle = ELineStyle.MEDIUM_DASH;
					}else{
						ret.lineStyle = ELineStyle.DASH;
					}
				}else if (ls.equals("dashdot")){
					if (lw >= 2.0f){
						ret.lineStyle = ELineStyle.MEDIUM_DASHDOT;
					}else{
						ret.lineStyle = ELineStyle.DASHDOT;
					}
				}else{
					if (lw >= 3.0f){
						ret.lineStyle = ELineStyle.THICK;
					}else if (lw >= 2.0f){
						ret.lineStyle = ELineStyle.MEDIUM;
					}else{
						ret.lineStyle = ELineStyle.THIN;
					}
				}
			}
		}
		if (!design.isNull("color")){
			ret.lineColor = (String)design.get("color");
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lineColor == null) ? 0 : lineColor.hashCode());
		result = prime * result
				+ ((lineStyle == null) ? 0 : lineStyle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BorderStyle other = (BorderStyle) obj;
		if (lineColor == null) {
			if (other.lineColor != null)
				return false;
		} else if (!lineColor.equals(other.lineColor))
			return false;
		if (lineStyle == null) {
			if (other.lineStyle != null)
				return false;
		} else if (!lineStyle.equals(other.lineStyle))
			return false;
		return true;
	}

}
