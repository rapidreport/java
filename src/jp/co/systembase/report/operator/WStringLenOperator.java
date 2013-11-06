package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class WStringLenOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 1);
		String str = ReportUtil.objectToString(evaluator.eval(params.get(0)));
		if (str == null){
			return null;
		}
		return ReportUtil.wStringLen(str);
	}

}
