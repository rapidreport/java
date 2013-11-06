package jp.co.systembase.report.operator;

import java.math.BigDecimal;
import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class DivOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 2);
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
			}else{
				BigDecimal d = Cast.toBigDecimal(o);
				if (d.equals(new BigDecimal(0))){
					return null;
				}else{
					ret = ret.divide(
							d,
							Math.max(0, (ret.scale() - ret.precision()) - (d.scale() - d.precision()) + 16),
							BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
				}
			}
		}
		return ret;
	}

}
