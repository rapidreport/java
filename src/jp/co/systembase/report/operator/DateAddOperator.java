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
		Date d = Cast.toDate(evaluator.eval(params.get(0)));
		String u = ReportUtil.objectToString(evaluator.eval(params.get(1)));
		int df = Cast.toInt(evaluator.eval(params.get(2)));
		if (d != null){
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			if (u.equals("y")){
				c.add(Calendar.YEAR, df);
			}else if (u.equals("M")){
				c.add(Calendar.MONTH, df);
			}else if (u.equals("d")){
				c.add(Calendar.DATE, df);
			}else if (u.equals("h")){
				c.add(Calendar.HOUR_OF_DAY, df);
			}else if (u.equals("m")){
				c.add(Calendar.MINUTE, df);
			}else if (u.equals("s")){
				c.add(Calendar.SECOND, df);
			}
			return c.getTime();
		}else{
			return null;
		}
	}

}