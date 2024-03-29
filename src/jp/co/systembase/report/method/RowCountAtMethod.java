package jp.co.systembase.report.method;

import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.data.ReportData;

public class RowCountAtMethod implements IMethod {

	public EEvalContext getAvailableContext() {
		return EEvalContext.PAGE;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		ReportData data = evaluator.getPresentData(scope, unit);
		if (param != null){
			return data.getCount(param);
		}else{
			return data.size();
		}
	}

}
