package jp.co.systembase.report.method;

import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.data.ReportData;

public class CrosstabMethod implements IMethod {

	public EEvalContext getAvailableContext() {
		return EEvalContext.ANY;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		if (param == null){
			return null;
		}
		{
			ReportData d = evaluator.basicContext.data;
			if (d.group != null && d.group.crosstabState != null){
				if (param.equals("v_index")){
					return d.group.crosstabState.vIndex;
				}else if (param.equals("h_index")){
					return d.group.crosstabState.hIndex;
				}else if (param.equals("v_last")){
					return d.group.crosstabState.vLast;
				}else if (param.equals("h_last")){
					return d.group.crosstabState.hLast;
				}
			}
		}
		return null;
	}

}
