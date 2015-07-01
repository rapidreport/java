package jp.co.systembase.report.search.searchobject;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;

public interface ISearchObject {
	String getText(ReportDesign reportDesign, ElementDesign design, Object data) throws Throwable;
}
