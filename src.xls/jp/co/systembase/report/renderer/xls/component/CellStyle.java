package jp.co.systembase.report.renderer.xls.component;

import jp.co.systembase.report.renderer.xls.XlsRenderer;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;


public class CellStyle {

	public GridStyle gridStyle = new GridStyle();
	public FieldStyle fieldStyle = null;

	public void fill(HSSFCellStyle cellStyle, XlsRenderer renderer){
		this.gridStyle.fill(cellStyle, renderer);
		if (this.fieldStyle != null){
			this.fieldStyle.fill(cellStyle, renderer);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldStyle == null) ? 0 : fieldStyle.hashCode());
		result = prime * result
				+ ((gridStyle == null) ? 0 : gridStyle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		CellStyle other = (CellStyle) obj;
		if (fieldStyle == null) {
			if (other.fieldStyle != null)
				return false;
		} else if (!fieldStyle.equals(other.fieldStyle))
			return false;
		if (gridStyle == null) {
			if (other.gridStyle != null)
				return false;
		} else if (!gridStyle.equals(other.gridStyle))
			return false;
		return true;
	}

}
