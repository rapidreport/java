package jp.co.systembase.report.expression;

import jp.co.systembase.report.component.EvalException;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.method.IMethod;

public class MethodExpression implements IExpression {
	public IMethod method;
	public String param;
	public String scope;
	public String unit;
	public MethodExpression(
			IMethod method,
			String param,
			String scope,
			String unit){
		this.method = method;
		this.param = param;
		this.scope = scope;
		this.unit = unit;
	}
	public Object eval(Evaluator evaluator) throws Throwable {
		if (this.unit != null && !evaluator.basicContext.data.isFilled()){
			throw new EvalException("a method with '#' is not available in yet unfilled report");
		}
		boolean avail = true;
		switch(method.getAvailableContext()){
		case CONTENT:
			avail = (evaluator.contentContext != null);
			break;
		case PAGE:
			avail = (evaluator.pageContext != null);
			break;
		default:
		}
		if (!avail){
			throw new EvalException("'" + this.method.getClass().getName() + "' is not available in this context");
		}
		return this.method.exec(
				evaluator,
				this.param,
				this.scope,
				this.unit);
	}
}
