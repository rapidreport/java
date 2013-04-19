package jp.co.systembase.report.operator;

import java.math.BigDecimal;
import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class SubOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		if (params.size() == 1){
			Object o = evaluator.eval(params.get(0));
			if (o != null){
				return Cast.toBigDecimal(o).negate();
			}else{
				return null;
			}
		}else{
			BigDecimal ret;
			{
				Object o = evaluator.eval(params.get(0));
				if (o == null){
					return null;
				}
				ret = Cast.toBigDecimal(o);
			}
			for(int i = 1;i < params.size();i++){
				Object o = evaluator.eval(params.get(i));
				if (o == null){
					return null;
				}
				ret = ret.subtract(Cast.toBigDecimal(o));
			}
			return ret;
		}
	}

}
