package jp.co.systembase.report.operator;

import java.util.Calendar;
import java.util.List;
import java.util.Date;

import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class YearOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		Object o = evaluator.eval(params.get(0));
		if (o instanceof Date){
			Calendar c = Calendar.getInstance();
			c.setTime((Date)o);
			return c.get(Calendar.YEAR);
		}else{
			return null;
		}
	}

}
