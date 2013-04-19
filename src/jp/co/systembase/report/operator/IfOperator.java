package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class IfOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		if (ReportUtil.condition(evaluator.eval(params.get(0)))){
			return evaluator.eval(params.get(1));
		}else{
			if (params.size() >= 3){
				return evaluator.eval(params.get(2));
			}else{
				return null;
			}
		}
	}

}
