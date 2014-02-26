package jp.co.systembase.report.renderer.xlsx.component;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.xlsx.XlsxRenderer;

public class Page {

	public XlsxRenderer renderer;
	public List<Grid> grids = new ArrayList<Grid>();
	public List<Field> fields = new ArrayList<Field>();
	public List<Shape> shapes = new ArrayList<Shape>();
	public int topRow = 0;

	public Page(XlsxRenderer renderer, ReportDesign reportDesign, Region paperRegion){
		this.renderer = renderer;
		Grid grid = new Grid();
		grid.region = paperRegion.toPointScale(reportDesign);
		this.grids.add(grid);
	}

}
