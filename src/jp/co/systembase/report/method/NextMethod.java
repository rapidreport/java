package jp.co.systembase.report.method;

import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Group;

public class NextMethod implements IMethod {

	public EEvalContext getAvailableContext() {
		return EEvalContext.CONTENT;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		if (param == null){
			return null;
		}
		Group g = evaluator.contentContext.content.parentGroup;
		if (g != null){
			if (g.index < g.parentGroups.groups.size() - 1){
				Group _g = g.parentGroups.groups.get(g.index + 1);
				return _g.data.getRecord().get(param);
			}
		}
		return null;
	}

}
