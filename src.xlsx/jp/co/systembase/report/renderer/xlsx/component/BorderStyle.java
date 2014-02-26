package jp.co.systembase.report.renderer.xlsx.component;

import java.math.BigDecimal;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;

public class BorderStyle {

	public enum ELineStyle{
		THIN,
		DOT,
		DASH,
		DASHDOT,
		MEDIUM,
		MEDIUM_DOT,
		MEDIUM_DASH,
		MEDIUM_DASHDOT,
		THICK,
		DOUBLE
	}
	public ELineStyle lineStyle = ELineStyle.THIN;
	public String lineColor = null;

	private BorderStyle(){};

	public static BorderStyle getInstance(ElementDesign desc, ReportDesign reportDesign){
		float lw = reportDesign.defaultLineWidth;
		if (!desc.isNull("line_width")){
			lw = ((BigDecimal)desc.get("line_width")).floatValue();
		}
		if (lw == 0f){
			return null;
		}
		BorderStyle ret = new BorderStyle();
		if (Cast.toBool(desc.get("doublet"))){
			ret.lineStyle = ELineStyle.DOUBLE;
		}else{
			String ls = "solid";
			if (!desc.isNull("line_style")){
				ls = (String)desc.get("line_style");
			}
			if (ls.equals("dot")){
				if (lw >= 2.0f){
					ret.lineStyle = ELineStyle.MEDIUM_DOT;
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
		if (!desc.isNull("color")){
			ret.lineColor = (String)desc.get("color");
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
