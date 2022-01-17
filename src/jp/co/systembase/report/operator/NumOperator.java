package jp.co.systembase.report.operator;

import java.math.BigDecimal;
import java.util.List;

import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class NumOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 1);
		Object o = evaluator.eval(params.get(0));
		if (o instanceof String){
			return new BigDecimal((String)o);
		}else{
			return null;
		}
	}

}
