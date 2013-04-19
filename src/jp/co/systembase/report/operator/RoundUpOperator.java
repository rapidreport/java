package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.core.Round;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class RoundUpOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		Object o = evaluator.eval(params.get(0));
		if (o == null){
			return null;
		}
		int digit = 0;
		if (params.size() >= 2){
			digit = Cast.toInt(evaluator.eval(params.get(1)));
		}
		return Round.roundUp(Cast.toBigDecimal(o), digit);
	}

}
