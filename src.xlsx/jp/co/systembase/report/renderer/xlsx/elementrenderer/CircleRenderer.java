package jp.co.systembase.report.renderer.xlsx.elementrenderer;

import java.math.BigDecimal;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;
import jp.co.systembase.report.renderer.xlsx.component.ColorUtil;
import jp.co.systembase.report.renderer.xlsx.component.LineStyles;
import jp.co.systembase.report.renderer.xlsx.component.Page;
import jp.co.systembase.report.renderer.xlsx.component.Shape;

import org.apache.poi.ss.usermodel.ShapeTypes;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;

public class CircleRenderer implements IElementRenderer {

	public void collect(
			XlsxRenderer renderer,
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
				lineWidth = ((BigDecimal)design.get("line_width")).floatValue();
				if (lineWidth == 0 && design.isNull("fill_color")){
					return;
				}
			}
			XSSFSheet s = page.renderer.sheet;
			XSSFDrawing d = s.createDrawingPatriarch();
			XSSFSimpleShape sp = d.createSimpleShape(shape.getXSSFClientAnchor(page.topRow));
			sp.setShapeType(ShapeTypes.ELLIPSE);
			if (lineWidth > 0){
				sp.setLineStyleColor(0, 0, 0);
				sp.setLineWidth(lineWidth);
				if (!this.design.isNull("color")){
					short[] t = ColorUtil.getTriplet((String)this.design.get("color"));
					if (t != null){
						sp.setLineStyleColor(t[0], t[1], t[2]);
					}
				}
				if (!this.design.isNull("line_style")){
					String ls = (String)this.design.get("line_style");
					if (ls.equals("dot")){
						sp.setLineStyle(LineStyles.DOT);
					}else if (ls.equals("dash")){
						sp.setLineStyle(LineStyles.DASH);
					}else if (ls.equals("dashdot")){
						sp.setLineStyle(LineStyles.DASHDOT);
					}
				}
			}
			if (!this.design.isNull("fill_color")){
				short[] t = ColorUtil.getTriplet((String)this.design.get("fill_color"));
				if (t != null){
					sp.setFillColor(t[0], t[1], t[2]);
				}
			}
		}
	}

}
