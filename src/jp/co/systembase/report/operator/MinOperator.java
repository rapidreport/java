package jp.co.systembase.report.operator;

import java.math.BigDecimal;
import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class MinOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		BigDecimal min = null;
		for(int i = 0;i < params.size();i++){
			BigDecimal v = Cast.toBigDecimal(evaluator.eval(params.get(i)));
			if (v != null){
				if (min == null || min.compareTo(v) > 0){
					min = v;
				}
			}
		}
		return min;
	}

}
