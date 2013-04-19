package jp.co.systembase.report.method;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.component.Evaluator;

public interface IMethod {
	Report.EEvalContext getAvailableContext();
	Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable;
}
