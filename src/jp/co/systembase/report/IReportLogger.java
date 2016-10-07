package jp.co.systembase.report;

import jp.co.systembase.report.component.ContentDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.EvalException;
import jp.co.systembase.report.component.UnknownFieldException;

public interface IReportLogger {
	void evaluateError(String exp, EvalException ex);
	void elementRenderingError(
			ContentDesign contentDesign,
			ElementDesign elementDesign,
			Throwable ex);
	void unknownFieldError(UnknownFieldException ex);
}
