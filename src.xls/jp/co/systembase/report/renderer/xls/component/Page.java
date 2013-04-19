package jp.co.systembase.report.renderer.xls.component;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xls.XlsRenderer;

public class Page {

	public XlsRenderer renderer;
	public List<Grid> grids = new ArrayList<Grid>();
	public List<Field> fields = new ArrayList<Field>();
	public List<Shape> shapes = new ArrayList<Shape>();
	public int topRow = 0;

	public Page(XlsRenderer renderer, ReportDesign reportDesign, Region paperRegion){
		this.renderer = renderer;
		Grid grid = new Grid();
		grid.region = paperRegion.toPointScale(reportDesign);
		this.grids.add(grid);
	}

}
