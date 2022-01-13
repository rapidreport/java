package jp.co.systembase.report.operator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class MonthOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 1);
		Date d = Cast.toDate(evaluator.eval(params.get(0)));
		if (d != null){
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			return c.get(Calendar.MONTH) + 1;
		}else{
			return null;
		}
	}

}
