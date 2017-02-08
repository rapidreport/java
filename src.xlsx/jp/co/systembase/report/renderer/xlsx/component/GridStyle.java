package jp.co.systembase.report.renderer.xlsx.component;

import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;


public class GridStyle {

	public BorderStyle topBorder = null;
	public BorderStyle bottomBorder = null;
	public BorderStyle leftBorder = null;
	public BorderStyle rightBorder = null;

	public String fillColor = null;

	public void fill(XSSFCellStyle cellStyle, XlsxRenderer renderer){
		if (this.leftBorder != null){
			BorderStyle bs = this.leftBorder;
			cellStyle.setBorderLeft(toHSSFBorderStyle(bs.lineStyle));
			if (bs.lineColor != null){
				XSSFColor c = ColorUtil.getColor(bs.lineColor);
				if (c != null){
					cellStyle.setLeftBorderColor(c);
				}
			}
		}
		if (this.rightBorder != null){
			BorderStyle bs = this.rightBorder;
			cellStyle.setBorderRight(toHSSFBorderStyle(bs.lineStyle));
			if (bs.lineColor != null){
				XSSFColor c = ColorUtil.getColor(bs.lineColor);
				if (c != null){
					cellStyle.setRightBorderColor(c);
				}
			}
		}
		if (this.topBorder != null){
			BorderStyle bs = this.topBorder;
			cellStyle.setBorderTop(toHSSFBorderStyle(bs.lineStyle));
			if (bs.lineColor != null){
				XSSFColor c = ColorUtil.getColor(bs.lineColor);
				if (c != null){
					cellStyle.setTopBorderColor(c);
				}
			}
		}
		if (this.bottomBorder != null){
			BorderStyle bs = this.bottomBorder;
			cellStyle.setBorderBottom(toHSSFBorderStyle(bs.lineStyle));
			if (bs.lineColor != null){
				XSSFColor c = ColorUtil.getColor(bs.lineColor);
				if (c != null){
					cellStyle.setBottomBorderColor(c);
				}
			}
		}
		if (this.fillColor != null){
			XSSFColor c = ColorUtil.getColor(this.fillColor);
			if (c != null){
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cellStyle.setFillForegroundColor(c);
			}
		}
	}

	private static org.apache.poi.ss.usermodel.BorderStyle toHSSFBorderStyle(BorderStyle.ELineStyle ls){
		switch(ls){
		case THIN:
			return org.apache.poi.ss.usermodel.BorderStyle.THIN;
		case THICK:
			return org.apache.poi.ss.usermodel.BorderStyle.THICK;
		case MEDIUM:
			return org.apache.poi.ss.usermodel.BorderStyle.MEDIUM;
		case DOT:
			return org.apache.poi.ss.usermodel.BorderStyle.DOTTED;
		case DASH:
			return org.apache.poi.ss.usermodel.BorderStyle.DASHED;
		case DASHDOT:
			return org.apache.poi.ss.usermodel.BorderStyle.DASH_DOT;
		case DOUBLE:
			return org.apache.poi.ss.usermodel.BorderStyle.DOUBLE;
		case MEDIUM_DOT:
			return org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASHED;
		case MEDIUM_DASH:
			return org.apache.poi.ss.usermodel.BorderStyle.MEDIUM_DASHED;
		case MEDIUM_DASHDOT:
			return org.apache.poi.ss.usermodel.BorderStyle.DASH_DOT;
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
