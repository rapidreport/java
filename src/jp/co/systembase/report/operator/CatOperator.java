package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;


public class CatOperator implements IOperator {

	public Object exec(
			Evaluator evaluator,
			List<IExpression> params) throws Throwable {
		String ret = "";
		for(int i = 0;i < params.size();i++){
			Object v = evaluator.eval(params.get(i));
			if (v != null){
				ret += v.toString();
			}
		}
		return ret;
	}

}
