package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class OrOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 2);
		for(int i = 0;i < params.size();i++){
			if (ReportUtil.condition(evaluator.eval(params.get(i)))){
				return true;
			}
		}
		return false;
	}

}
