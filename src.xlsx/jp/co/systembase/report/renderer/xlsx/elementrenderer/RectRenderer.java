package jp.co.systembase.report.renderer.xlsx.elementrenderer;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;
import jp.co.systembase.report.renderer.xlsx.component.BorderStyle;
import jp.co.systembase.report.renderer.xlsx.component.Grid;
import jp.co.systembase.report.renderer.xlsx.component.RowColUtil;

public class RectRenderer implements IElementRenderer {

	public void collect(
			XlsxRenderer renderer,
			ReportDesign reportDesign,
			Region region,
			ElementDesign design,
			Object data) throws Throwable {
		Region _region = region.toPointScale(reportDesign);
		if (_region.getWidth() <= 0 || _region.getHeight() <= 0){
			return;
		}
		Grid grid = new Grid();
		grid.region = _region;
		{
			BorderStyle bs = BorderStyle.getInstance(design, reportDesign);
			if (bs != null){
				if (!Cast.toBool(design.get("hide_top"))){
					grid.style.topBorder = bs;
				}
				if (!Cast.toBool(design.get("hide_bottom")) &&
						Math.abs(_region.getHeight()) > RowColUtil.TOLERANCE){
					grid.style.bottomBorder = bs;
				}
				if (!Cast.toBool(design.get("hide_left"))){
					grid.style.leftBorder = bs;
				}
				if (!Cast.toBool(design.get("hide_right")) &&
						Math.abs(_region.getWidth()) > RowColUtil.TOLERANCE){
					grid.style.rightBorder = bs;
				}
			}
		}
		if (!design.isNull("fill_color")){
			grid.style.fillColor = (String)design.get("fill_color");
		}
		renderer.currentPage.grids.add(grid);
	}

}
