package jp.co.systembase.report.renderer.xls.component;

import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xls.elementrenderer.IShapeRenderer;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;


public class Shape {

	public IShapeRenderer renderer = null;
	public Region region = null;
	public CellRange cellRange = new CellRange();

	public HSSFClientAnchor getHSSFClientAnchor(int topRow){
		return new HSSFClientAnchor(0, 0, 0, 0,
				(short)this.cellRange.col1,
				this.cellRange.row1 + topRow,
				(short)this.cellRange.col2,
				this.cellRange.row2 + topRow);
	}

}
