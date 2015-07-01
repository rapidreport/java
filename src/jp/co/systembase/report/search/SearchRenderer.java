package jp.co.systembase.report.search;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;
import jp.co.systembase.report.renderer.IRenderer;
import jp.co.systembase.report.search.searchobject.ISearchObject;

public class SearchRenderer implements IRenderer {

	public static class Result{
		public ElementDesign elementDesign;
		public Region region;
	}
	
	public String keyword;
	public List<Result> Results = new ArrayList<Result>();
	
	public SearchRenderer(String keyword){
		this.keyword = keyword;
	}
	
	@Override
	public void beginReport(ReportDesign reportDesign) throws Throwable {
	}

	@Override
	public void endReport(ReportDesign reportDesign) throws Throwable {
	}

	@Override
	public void beginPage(ReportDesign reportDesign, int pageIndex, Region paperRegion) throws Throwable {
	}

	@Override
	public void endPage(ReportDesign reportDesign) throws Throwable {
	}

	@Override
	public void renderElement(
			ReportDesign reportDesign, 
			Region region,
			ElementDesign design, 
			Object data) throws Throwable {
		if (!design.isNull("id") && design.get("id").equals("__trial__")){
			return;
		}
		ISearchObject searchObject = reportDesign.setting.getSearchObject((String)design.get("type"));
		String t = searchObject.getText(reportDesign, design, data);
		if (t != null && t.toLowerCase().indexOf(this.keyword.toLowerCase()) >= 0){
			Result r = new Result();
			r.elementDesign = design;
			r.region = region;
			this.Results.add(r);
		}
	}

}
