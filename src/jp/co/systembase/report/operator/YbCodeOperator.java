package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public class YbCodeOperator implements IOperator {

	@Override
	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 1);
		String ret = "";
		{
			Object v = evaluator.eval(params.get(0));
			if (v != null){
				String t = _convDigitNarrow(v.toString());
				ret = t.replaceAll("\\D", "");
			}
		}
		if (params.size() >= 2)
		{
			Object v = evaluator.eval(params.get(1));
			if (v != null){
				String t = _convDigitNarrow(v.toString());
				t = t.replaceAll("^\\D+|\\D+$", "");
				ret += t.replaceAll("\\D+", "-");
			}
		}
		return ret;
	}

	private String _convDigitNarrow(String t){
		return t.replaceAll("０", "0")
				.replaceAll("１", "1")
				.replaceAll("２", "2")
				.replaceAll("３", "3")
				.replaceAll("４", "4")
				.replaceAll("５", "5")
				.replaceAll("６", "6")
				.replaceAll("７", "7")
				.replaceAll("８", "8")
				.replaceAll("９", "9");
		
	}
}
