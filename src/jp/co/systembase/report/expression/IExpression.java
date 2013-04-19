package jp.co.systembase.report.expression;

import jp.co.systembase.report.component.Evaluator;

public interface IExpression {
	Object eval(Evaluator evaluator) throws Throwable;
}