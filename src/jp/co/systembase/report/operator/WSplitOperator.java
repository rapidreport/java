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
		int j = 0;
		for(String t: str.split("\n")){
			t = t.replace("\r", "");
			int b = 0;
			int e = 0;
			do{
				b = e;
				e = ReportUtil.getWIndex(t, b, w);
				if (j == i){
					return ReportUtil.subString(t, b, e - b);
				}
				j++;
			}while(e < t.length());
		}
		return null;
	}

}
