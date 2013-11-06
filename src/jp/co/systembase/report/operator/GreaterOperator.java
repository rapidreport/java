package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class GreaterOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 2);
		return ReportUtil.compareTo(
				evaluator.eval(params.get(0)),
				evaluator.eval(params.get(1))) > 0;
	}

}
