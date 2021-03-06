package jp.co.systembase.report.method;

import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.Evaluator;

public class CurrentTimeMethod implements IMethod {

	@Override
	public EEvalContext getAvailableContext() {
		return EEvalContext.ANY;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		return evaluator.basicContext.report.currentTime;
	}

}
