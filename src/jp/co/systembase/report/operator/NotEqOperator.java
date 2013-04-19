package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class NotEqOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		Object v = evaluator.eval(params.get(0));
		for(int i = 1;i < params.size();i++){
			if (ReportUtil.eq(v, evaluator.eval(params.get(i)))){
				return false;
			}
		}
		return true;
	}

}
