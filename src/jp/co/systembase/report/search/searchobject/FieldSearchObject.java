package jp.co.systembase.report.search.searchobject;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.renderer.RenderUtil;

public class FieldSearchObject implements ISearchObject {
	@Override
	public String getText(
			ReportDesign reportDesign, 
			ElementDesign design,
			Object data) throws Throwable {
		return RenderUtil.format(reportDesign, design.child("formatter"), data);
	}
}
