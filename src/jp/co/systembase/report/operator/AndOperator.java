package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class AndOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		for(int i = 0;i < params.size();i++){
			if (!ReportUtil.condition(evaluator.eval(params.get(i)))){
				return false;
			}
		}
		return true;
	}

}
