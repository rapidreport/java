package jp.co.systembase.report.expression;

import jp.co.systembase.report.component.Evaluator;

public class ImmediateExpression implements IExpression {
	public Object value;
	public ImmediateExpression(Object value){
		this.value = value;
	}
	public Object eval(Evaluator evaluator) throws Throwable {
		return this.value;
	}
}
