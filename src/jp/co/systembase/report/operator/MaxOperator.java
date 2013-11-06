package jp.co.systembase.report.operator;

import java.math.BigDecimal;
import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class MaxOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 1);
		BigDecimal max = null;
		for(int i = 0;i < params.size();i++){
			BigDecimal v = Cast.toBigDecimal(evaluator.eval(params.get(i)));
			if (v != null){
				if (max == null || max.compareTo(v) < 0){
					max = v;
				}
			}
		}
		return max;
	}

}
