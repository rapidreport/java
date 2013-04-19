package jp.co.systembase.report.expression;

import java.util.List;

import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.operator.IOperator;

public class OperatorExpression implements IExpression {
	public IOperator operator;
	public List<IExpression> params;
	public OperatorExpression(
			IOperator operator,
			List<IExpression> params){
		this.operator = operator;
		this.params = params;
	}
	public Object eval(Evaluator evaluator) throws Throwable {
		return this.operator.exec(evaluator, params);
	}
}
