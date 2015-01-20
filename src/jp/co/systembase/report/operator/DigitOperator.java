package jp.co.systembase.report.operator;

import java.math.BigDecimal;
import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class DigitOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 2);
		BigDecimal v = Cast.toBigDecimal(evaluator.eval(params.get(0)));
		int d = Cast.toInt(evaluator.eval(params.get(1)));
		if (v != null){
			String _v = v.toPlainString();
			int l = _v.length();
			int i = _v.indexOf(".");
			int j = (i < 0 ? l : i) - d - (d >= 0 ? 1 : 0);
			if (j < 0){
				return null;
			}else if (j >= l){
				return "0";
			}else{
				return _v.substring(j, j + 1);
			}			
		}else{
			return null;
		}
	}

}
