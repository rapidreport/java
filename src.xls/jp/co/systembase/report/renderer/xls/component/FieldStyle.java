package jp.co.systembase.report.renderer.xls.component;

import jp.co.systembase.report.component.TextDesign;
import jp.co.systembase.report.renderer.xls.XlsRenderer;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

public class FieldStyle {

	public TextDesign textDesign;
	public FontStyle fontStyle;

	public FieldStyle(TextDesign design){
		this.fontStyle = new FontStyle(design);
		this.textDesign = design;
	}

	public void fill(HSSFCellStyle cellStyle, XlsRenderer renderer){
		cellStyle.setFont(renderer.fontPool.get(this.fontStyle));
		if (!this.textDesign.vertical && this.textDesign.distribute){
			cellStyle.setAlignment((short)7);
		}else{
			switch(this.textDesign.halign){
			case LEFT:
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				break;
			case CENTER:
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				break;
			case RIGHT:
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				break;
			}
		}
		if (this.textDesign.vertical && this.textDesign.distribute){
			cellStyle.setVerticalAlignment((short)4);
		}else{
			switch(this.textDesign.valign){
			case TOP:
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
				break;
			case CENTER:
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				break;
			case BOTTOM:
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
				break;
			}
		}
		if (this.textDesign.wrap){
			cellStyle.setWrapText(true);
		}
		if (this.textDesign.shrinkToFit){
			cellStyle.setShrinkToFit(true);
		}
		if (this.textDesign.vertical){
			cellStyle.setRotation((short)0xff);
		}
		if (this.textDesign.xlsFormat != null){
			short dataFormat = renderer.workbook.createDataFormat().getFormat(this.textDesign.xlsFormat);
			cellStyle.setDataFormat(dataFormat);
		}else{
			cellStyle.setDataFormat((short)0x31);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.fontStyle == null) ? 0 : this.fontStyle.hashCode());
		result = prime * result + ((this.textDesign.halign == null) ? 0 : this.textDesign.halign.hashCode());
		result = prime * result + ((this.textDesign.valign == null) ? 0 : this.textDesign.valign.hashCode());
		result = prime * result + (this.textDesign.shrinkToFit ? 1231 : 1237);
		result = prime * result + (this.textDesign.wrap ? 1231 : 1237);
		result = prime * result + (this.textDesign.vertical ? 1231 : 1237);
		result = prime * result + (this.textDesign.distribute ? 1231 : 1237);
		result = prime * result + ((this.textDesign.xlsFormat == null) ? 0 : this.textDesign.xlsFormat.hashCode());
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
		FieldStyle other = (FieldStyle) obj;
		if (this.fontStyle == null) {
			if (other.fontStyle != null)
				return false;
		} else if (!this.fontStyle.equals(other.fontStyle)){
			return false;
		}
		if (this.textDesign.halign == null) {
			if (other.textDesign.halign != null)
				return false;
		} else if (!this.textDesign.halign.equals(other.textDesign.halign)){
			return false;
		}
		if (this.textDesign.valign == null) {
			if (other.textDesign.valign != null)
				return false;
		} else if (!this.textDesign.valign.equals(other.textDesign.valign)){
			return false;
		}
		if (this.textDesign.shrinkToFit != other.textDesign.shrinkToFit){
			return false;
		}
		if (this.textDesign.wrap != other.textDesign.wrap){
			return false;
		}
		if (this.textDesign.vertical != other.textDesign.vertical){
			return false;
		}
		if (this.textDesign.distribute != other.textDesign.distribute){
			return false;
		}
		if (this.textDesign.xlsFormat == null) {
			if (other.textDesign.xlsFormat != null){
				return false;
			}
		} else if (!this.textDesign.xlsFormat.equals(other.textDesign.xlsFormat)){
			return false;
		}
		return true;
	}

}
