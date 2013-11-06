package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class ModOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 2);
		Object v1 = evaluator.eval(params.get(0));
		Object v2 = evaluator.eval(params.get(1));
		if (v1 == null || v2 == null){
			return null;
		}else{
			return Cast.toBigDecimal(v1).remainder(Cast.toBigDecimal(v2));
		}
	}

}
