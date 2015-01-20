package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class WSplitOperator implements IOperator {

	@Override
	public Object exec(Evaluator evaluator, List<IExpression> params)
			throws Throwable {
		evaluator.ValidateParamCount(params, 3);
		String str = ReportUtil.objectToString(evaluator.eval(params.get(0)));
		if (str == null){
			return null;
		}
		int w = Cast.toInt(evaluator.eval(params.get(1)));
		int i = Cast.toInt(evaluator.eval(params.get(2)));
		int b = 0;
		int e = 0;
		for(int j = 0;j <= i;j++){
			b = e;
			e = ReportUtil.getWIndex(str, b, w);
		}
		return ReportUtil.subString(str, b, e - b);
	}

}
