package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class AbsOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		Object o = evaluator.eval(params.get(0));
		if (o != null){
			return Cast.toBigDecimal(o).abs();
		}else{
			return null;
		}
	}

}
