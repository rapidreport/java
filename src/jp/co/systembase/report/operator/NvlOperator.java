package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class NvlOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		evaluator.ValidateParamCount(params, 1);
		for(int i = 0;i < params.size();i++){
			Object v = evaluator.eval(params.get(i));
			if (v != null){
				return v;
			}
		}
		return null;
	}

}
