package jp.co.systembase.report.renderer.xlsx.component;

import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xlsx.elementrenderer.IShapeRenderer;

import org.apache.poi.xssf.usermodel.XSSFClientAnchor;


public class Shape {

	public IShapeRenderer renderer = null;
	public Region region = null;
	public CellRange cellRange = new CellRange();

	public XSSFClientAnchor getXSSFClientAnchor(int topRow){
		return new XSSFClientAnchor(0, 0, 0, 0,
				Math.min(this.cellRange.col1, this.cellRange.col2),
				Math.min(this.cellRange.row1, this.cellRange.row2) + topRow,
				Math.max(this.cellRange.col1, this.cellRange.col2),
				Math.max(this.cellRange.row1, this.cellRange.row2) + topRow);
	}

	public boolean isInverted(){
		return ((this.cellRange.col2 - this.cellRange.col1) *
				(this.cellRange.row2 - this.cellRange.row1)) < 0;
	}

}
