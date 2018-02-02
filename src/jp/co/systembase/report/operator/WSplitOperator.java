package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.textsplitter.TextSplitterByWidth;
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
		boolean rule = false;
		if (params.size() >= 4){
			rule = Cast.toBool(evaluator.eval(params.get(3)));
		}
		TextSplitterByWidth sp = new TextSplitterByWidth(w, rule);
		return sp.getLine(str, i);
	}

}
