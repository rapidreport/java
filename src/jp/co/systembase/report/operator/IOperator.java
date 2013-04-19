package jp.co.systembase.report.operator;

import java.util.List;

import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.expression.IExpression;

public interface IOperator {
	Object exec(Evaluator evaluator, List<IExpression> params) throws Throwable;
}
