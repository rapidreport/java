package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class IndexOfOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 2);
		String str = ReportUtil.objectToString(evaluator.eval(params.get(0)));
		String searchStr = ReportUtil.objectToString(evaluator.eval(params.get(1)));
		if (str == null || searchStr == null){
			return -1;
		}
		return str.indexOf(searchStr);
	}

}