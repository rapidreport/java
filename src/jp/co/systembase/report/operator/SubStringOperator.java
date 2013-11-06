package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class SubStringOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 2);
		String str = ReportUtil.objectToString(evaluator.eval(params.get(0)));
		if (str == null){
			return null;
		}
		int b = Cast.toInt(evaluator.eval(params.get(1)));
		if (params.size() >= 3){
			int l = Cast.toInt(evaluator.eval(params.get(2)));
			return ReportUtil.subString(str, b, l);
		}else{
			return ReportUtil.subString(str, b);
		}
	}

}
