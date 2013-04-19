package jp.co.systembase.report.method;

import jp.co.systembase.report.ReportPage;
import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.Evaluator;

public class ToggleMethod implements IMethod {

	public EEvalContext getAvailableContext() {
		return EEvalContext.ANY;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		ReportPage p = evaluator.pageContext.page;
		p.ToggleValue = !p.ToggleValue;
		return p.ToggleValue;
	}

}
