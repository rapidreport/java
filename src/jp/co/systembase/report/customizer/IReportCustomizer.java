package jp.co.systembase.report.customizer;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportPage;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.component.Content;
import jp.co.systembase.report.component.ElementDesigns;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Region;

public interface IReportCustomizer {
	Region contentRegion(
			Content content,
			Evaluator evaluator,
			Region region);
	void pageAdded(
			Report report,
			ReportPages pages,
			ReportPage page);
	void renderContent(
			Content content,
			Evaluator evaluator,
			Region region,
			ElementDesigns elementDesigns);
}
