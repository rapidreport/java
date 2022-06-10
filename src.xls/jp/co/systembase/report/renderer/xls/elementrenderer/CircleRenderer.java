package jp.co.systembase.report.renderer.xls.elementrenderer;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.RenderUtil;
import jp.co.systembase.report.renderer.xls.XlsRenderer;
import jp.co.systembase.report.renderer.xls.component.Page;
import jp.co.systembase.report.renderer.xls.component.Shape;

public class CircleRenderer implements IElementRenderer {

	public void collect(
			XlsRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Region _region = region.toPointScale(reportDesign);
		Shape shape = new Shape();
		shape.renderer = new CircleShapeRenderer(design, reportDesign);
		shape.region = _region;
		renderer.currentPage.shapes.add(shape);
	}

	public static class CircleShapeRenderer implements IShapeRenderer{
		public ElementDesign design;
		public ReportDesign reportDesign;
		public CircleShapeRenderer(
				ElementDesign design,
				ReportDesign reportDesign){
			this.design = design;
			this.reportDesign = reportDesign;
		}
		public void render(Page page, Shape shape) {
			float lineWidth = this.reportDesign.defaultLineWidth;
			if (!design.isNull("line_width")){
				lineWidth = Cast.toFloat(design.get("line_width"));
				if (lineWidth == 0 && design.isNull("fill_color")){
					return;
				}
			}
			HSSFSheet s = page.renderer.sheet;
			HSSFPatriarch p = s.getDrawingPatriarch();
			HSSFSimpleShape sp = p.createSimpleShape(shape.getHSSFClientAnchor(page.topRow));
			sp.setShapeType(HSSFSimpleShape.OBJECT_TYPE_OVAL);
			if (!this.design.isNull("color")){
				short[] t = RenderUtil.getColor((String)this.design.get("color"));
				if (t != null){
					sp.setLineStyleColor(t[0], t[1], t[2]);
				}
			}
			if (!this.design.isNull("fill_color")){
				short[] t = RenderUtil.getColor((String)this.design.get("fill_color"));
				if (t != null){
					sp.setFillColor(t[0], t[1], t[2]);
				}
			}else{
				sp.setNoFill(true);
			}
			if (!design.isNull("line_width")){
				sp.setLineWidth((int)(HSSFSimpleShape.LINEWIDTH_ONE_PT * lineWidth));
			}
			if (lineWidth == 0){
				sp.setLineStyle(HSSFSimpleShape.LINESTYLE_NONE);
			}else if (!this.design.isNull("line_style")){
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
