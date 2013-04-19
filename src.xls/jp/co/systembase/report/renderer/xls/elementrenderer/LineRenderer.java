package jp.co.systembase.report.renderer.xls.elementrenderer;

import java.math.BigDecimal;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xls.component.BorderStyle;
import jp.co.systembase.report.renderer.xls.component.ColorUtil;
import jp.co.systembase.report.renderer.xls.component.Grid;
import jp.co.systembase.report.renderer.xls.component.Page;
import jp.co.systembase.report.renderer.xls.component.RowColUtil;
import jp.co.systembase.report.renderer.xls.component.Shape;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;

public class LineRenderer implements IElementRenderer {

	public void collect(
			XlsRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Region _region = region.toPointScale(reportDesign);
		if (Math.abs(_region.getWidth()) < RowColUtil.TOLERANCE){
			BorderStyle bs = BorderStyle.getInstance(design, reportDesign);
			if (bs != null){
				Grid grid = new Grid();
				if (_region.left > RowColUtil.TOLERANCE){
					grid.style.rightBorder = bs;
				}else{
					grid.style.leftBorder = bs;
				}
				grid.region = new Region();
				grid.region.top = Math.min(_region.top, _region.bottom);
				grid.region.bottom = Math.max(_region.top, _region.bottom);
				grid.region.left = _region.left;
				grid.region.right = _region.left;
				renderer.currentPage.grids.add(grid);
			}
		}else if(Math.abs(_region.getHeight()) < RowColUtil.TOLERANCE){
			BorderStyle bs = BorderStyle.getInstance(design, reportDesign);
			if (bs != null){
				Grid grid = new Grid();
				if (_region.top > RowColUtil.TOLERANCE){
					grid.style.bottomBorder = bs;
				}else{
					grid.style.topBorder = bs;
				}
				grid.region = new Region();
				grid.region.top = _region.top;
				grid.region.bottom = _region.top;
				grid.region.left = Math.min(_region.left, _region.right);
				grid.region.right = Math.max(_region.left, _region.right);
				renderer.currentPage.grids.add(grid);
			}
		}else{
			Shape shape = new Shape();
			shape.renderer = new LineShapeRenderer(design, reportDesign);
			shape.region = _region;
			renderer.currentPage.shapes.add(shape);
		}
	}

	public static class LineShapeRenderer implements IShapeRenderer{
		public ElementDesign design;
		public ReportDesign reportDesign;
		public LineShapeRenderer(
				ElementDesign design,
				ReportDesign reportDesign){
			this.design = design;
			this.reportDesign = reportDesign;
		}
		public void render(Page page, Shape shape) {
			float lineWidth = this.reportDesign.defaultLineWidth;
			if (!design.isNull("line_width")){
				lineWidth = ((BigDecimal)design.get("line_width")).floatValue();
				if (lineWidth == 0){
					return;
				}
			}
			HSSFSheet s = page.renderer.sheet;
			HSSFPatriarch p = s.getDrawingPatriarch();
			HSSFSimpleShape sp = p.createSimpleShape(shape.getHSSFClientAnchor(page.topRow));
			sp.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
			if (!this.design.isNull("color")){
				short[] t = ColorUtil.getTriplet((String)this.design.get("color"));
				if (t != null){
					sp.setLineStyleColor(t[0], t[1], t[2]);
				}
			}
			if (!this.design.isNull("line_width")){
				sp.setLineWidth((int)(HSSFSimpleShape.LINEWIDTH_ONE_PT * lineWidth));
			}
			if (!this.design.isNull("line_style")){
				String ls = (String)this.design.get("line_style");
				if (ls.equals("dot")){
					sp.setLineStyle(HSSFSimpleShape.LINESTYLE_DOTSYS);
				}else if (ls.equals("dash")){
					sp.setLineStyle(HSSFSimpleShape.LINESTYLE_DASHSYS);
				}else if (ls.equals("dashdot")){
					sp.setLineStyle(HSSFSimpleShape.LINESTYLE_DASHDOTSYS);
				}
			}
		}
	}

}
