package jp.co.systembase.report.search.searchobject;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;

public class DefaultSearchObject implements ISearchObject {
	@Override
	public String getText(
			ReportDesign reportDesign, 
			ElementDesign design,
			Object data) throws Throwable {
		return null;
	}
}
