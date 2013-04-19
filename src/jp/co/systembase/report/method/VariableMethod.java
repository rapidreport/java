package jp.co.systembase.report.method;

import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.Evaluator;

public class VariableMethod implements IMethod {

	public EEvalContext getAvailableContext() {
		return EEvalContext.PAGE;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		return evaluator.pageContext.variables.get(param);
	}

}
