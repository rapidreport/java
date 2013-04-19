package jp.co.systembase.report.renderer.xls.component;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class Cell {

	public CellStyle style = null;
	public Object data = null;
	public CellRange range = new CellRange();
	public Cell mergedCell = null;

	public Cell(int row, int col){
		this.range.row1 = row;
		this.range.col1 = col;
		this.reset();
	}

	public void reset(){
		this.data = null;
		this.range.row2 = this.range.row1 + 1;
		this.range.col2 = this.range.col1 + 1;
		this.mergedCell = this;
	}

	public void render(Page page){
		HSSFSheet s = page.renderer.sheet;
		HSSFCell hssfCell = null;
		if (this.style != null){
			HSSFCellStyle cellStyle = page.renderer.cellStylePool.get(this.style);
			hssfCell = s.getRow(page.topRow + this.range.row1).createCell(this.range.col1);
			hssfCell.setCellStyle(cellStyle);
		}
		if (this.mergedCell == this &&
			(this.range.row1 < this.range.row2 - 1 || this.range.col1 < this.range.col2 - 1)){
			s.addMergedRegion(new CellRangeAddress(
					page.topRow + this.range.row1,
					page.topRow + this.range.row2 - 1,
					this.range.col1,
					this.range.col2 - 1));
		}
		if (this.data != null){
			if (hssfCell == null){
				hssfCell = s.getRow(page.topRow + this.range.row1).createCell(this.range.col1);
			}
			if (this.data instanceof String){
				hssfCell.setCellValue((String)this.data);
			}else if (this.data instanceof BigDecimal){
				hssfCell.setCellValue(((BigDecimal)this.data).doubleValue());
			}else if (this.data instanceof Integer){
				hssfCell.setCellValue(((Integer)this.data).doubleValue());
			}else if (this.data instanceof Long){
				hssfCell.setCellValue(((Long)this.data).doubleValue());
			}else if (this.data instanceof Float){
				hssfCell.setCellValue(((Float)this.data).doubleValue());
			}else if (this.data instanceof Double){
				hssfCell.setCellValue((Double)this.data);
			}else if (this.data instanceof Boolean){
				hssfCell.setCellValue((Boolean)this.data);
			}else if (this.data instanceof Date){
				hssfCell.setCellValue((Date)this.data);
			}
		}
	}

}
