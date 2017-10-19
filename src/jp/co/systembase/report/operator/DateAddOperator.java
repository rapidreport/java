package jp.co.systembase.report.operator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class DateAddOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 3);
		Object o = evaluator.eval(params.get(0));
		String u = ReportUtil.objectToString(evaluator.eval(params.get(1)));
		int d = Cast.toInt(evaluator.eval(params.get(2)));
		if (o instanceof Date){
			Calendar c = Calendar.getInstance();
			c.setTime((Date)o);
			if (u.equals("y")){
				c.add(Calendar.YEAR, d);
			}else if (u.equals("M")){
				c.add(Calendar.MONTH, d);
			}else if (u.equals("d")){
				c.add(Calendar.DATE, d);
			}else if (u.equals("h")){
				c.add(Calendar.HOUR_OF_DAY, d);
			}else if (u.equals("m")){
				c.add(Calendar.MINUTE, d);
			}else if (u.equals("s")){
				c.add(Calendar.SECOND, d);
			}
			return c.getTime();
		}else{
			return null;
		}
	}

}