package jp.co.systembase.report.method;

import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.EvalException;
import jp.co.systembase.report.component.Evaluator;

public class PageGroupCountMethod implements IMethod {

	public EEvalContext getAvailableContext() {
		return EEvalContext.ANY;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		if (evaluator.GroupLayoutFilledCount < 0){
			throw new EvalException("page_group_countメソッドはgroup.layout.内でのみ呼び出すことができます。");
		}
		return evaluator.GroupLayoutFilledCount;
	}
}
