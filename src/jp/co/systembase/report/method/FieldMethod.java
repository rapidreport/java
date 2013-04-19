package jp.co.systembase.report.method;

import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.data.ReportData;

public class FieldMethod implements IMethod {

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
		if (scope == null && unit == null){
			return evaluator.basicContext.dataRecord.get(param);
		}else{
			ReportData data = evaluator.getData(scope, unit);
			if (!data.isEmpty()){
				return data.getRecord().get(param);
			}else{
				return null;
			}
		}
	}

}
