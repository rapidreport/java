package jp.co.systembase.report.renderer;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.Region;

public interface IRenderer {
	void beginReport(ReportDesign reportDesign) throws Throwable;
	void endReport(ReportDesign reportDesign) throws Throwable;
	void beginPage(ReportDesign reportDesign, int pageIndex, Region paperRegion) throws Throwable;
	void endPage(ReportDesign reportDesign) throws Throwable;
	void renderElement(ReportDesign reportDesign, Region region, ElementDesign design, Object data) throws Throwable;
}
