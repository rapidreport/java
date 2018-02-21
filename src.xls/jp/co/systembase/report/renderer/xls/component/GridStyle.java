package jp.co.systembase.report.renderer.xls.component;

import jp.co.systembase.report.renderer.xls.XlsRenderer;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;


public class GridStyle {

	public BorderStyle topBorder = null;
	public BorderStyle bottomBorder = null;
	public BorderStyle leftBorder = null;
	public BorderStyle rightBorder = null;

	public String fillColor = null;

	public void fill(HSSFCellStyle cellStyle, XlsRenderer renderer){
		if (this.leftBorder != null){
			BorderStyle bs = this.leftBorder;
			cellStyle.setBorderLeft(toNPOIBorderStyle(bs.lineStyle));
			if (bs.lineColor != null){
				short i = renderer.colorPool.getIndex(bs.lineColor);
				if (i >= 0){
					cellStyle.setLeftBorderColor(i);
				}
			}
		}
		if (this.rightBorder != null){
			BorderStyle bs = this.rightBorder;
			cellStyle.setBorderRight(toNPOIBorderStyle(bs.lineStyle));
			if (bs.lineColor != null){
				short i = renderer.colorPool.getIndex(bs.lineColor);
				if (i >= 0){
					cellStyle.setRightBorderColor(i);
				}
			}
		}
		if (this.topBorder != null){
			BorderStyle bs = this.topBorder;
			cellStyle.setBorderTop(toNPOIBorderStyle(bs.lineStyle));
			if (bs.lineColor != null){
				short i = renderer.colorPool.getIndex(bs.lineColor);
				if (i >= 0){
					cellStyle.setTopBorderColor(i);
				}
			}
		}
		if (this.bottomBorder != null){
			BorderStyle bs = this.bottomBorder;
			cellStyle.setBorderBottom(toNPOIBorderStyle(bs.lineStyle));
			if (bs.lineColor != null){
				short i = renderer.colorPool.getIndex(bs.lineColor);
				if (i >= 0){
					cellStyle.setBottomBorderColor(i);
				}
			}
		}
		if (this.fillColor != null){
			short i = renderer.colorPool.getIndex(this.fillColor);
			if (i >= 0){
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cellStyle.setFillForegroundColor(i);
			}
		}
	}

	private static org.apache.poi.ss.usermodel.BorderStyle toNPOIBorderStyle(BorderStyle.ELineStyle ls){
		switch(ls){
		case THIN:
			return org.apache.poi.ss.usermodel.BorderStyle.THIN;
		case HAIR:
			return org.apache.poi.ss.usermodel.BorderStyle.HAIR;
		case DOT:
			return org.apache.poi.ss.usermodel.BorderStyle.DOTTED;
		case DASH:
			return org.apache.poi.ss.usermodel.BorderStyle.DASHED;
		case DASHDOT:
			return org.apache.poi.ss.usermodel.BorderStyle.DASH_DOT;
		case DASHDOTDOT:
			return org.apache.poi.ss.usermodel.BorderStyle.DASH_DOT_DOT;
		case MEDIUM:
			return org.apache.poi.ss.usermodel.BorderStyle.MEDIUM;
		case MEDIUM_DASH:
			return org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASHED;
		case MEDIUM_DASHDOT:
			return org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASH_DOT;
		case MEDIUM_DASHDOTDOT:
			return org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASH_DOT_DOT;
		case SLANTED_DASHDOT:
			return org.apache.poi.ss.usermodel.BorderStyle.SLANTED_DASH_DOT;
		case THICK:
			return org.apache.poi.ss.usermodel.BorderStyle.THICK;
		case DOUBLE:
			return org.apache.poi.ss.usermodel.BorderStyle.DOUBLE;
		}
		return org.apache.poi.ss.usermodel.BorderStyle.NONE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bottomBorder == null) ? 0 : bottomBorder.hashCode());
		result = prime * result
				+ ((fillColor == null) ? 0 : fillColor.hashCode());
		result = prime * result
				+ ((leftBorder == null) ? 0 : leftBorder.hashCode());
		result = prime * result
				+ ((rightBorder == null) ? 0 : rightBorder.hashCode());
		result = prime * result
				+ ((topBorder == null) ? 0 : topBorder.hashCode());
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
		GridStyle other = (GridStyle) obj;
		if (bottomBorder == null) {
			if (other.bottomBorder != null)
				return false;
		} else if (!bottomBorder.equals(other.bottomBorder))
			return false;
		if (fillColor == null) {
			if (other.fillColor != null)
				return false;
		} else if (!fillColor.equals(other.fillColor))
			return false;
		if (leftBorder == null) {
			if (other.leftBorder != null)
				return false;
		} else if (!leftBorder.equals(other.leftBorder))
			return false;
		if (rightBorder == null) {
			if (other.rightBorder != null)
				return false;
		} else if (!rightBorder.equals(other.rightBorder))
			return false;
		if (topBorder == null) {
			if (other.topBorder != null)
				return false;
		} else if (!topBorder.equals(other.topBorder))
			return false;
		return true;
	}

}
