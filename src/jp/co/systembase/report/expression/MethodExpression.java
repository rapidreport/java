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
			throw new EvalException("'#'付きのメソッドは report.fill() の実行後にのみ呼び出すことができます");
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
			throw new EvalException("メソッド '" + this.method.getClass().getName() + "' は、このコンテキストでは呼び出すことができません");
		}
		return this.method.exec(
				evaluator,
				this.param,
				this.scope,
				this.unit);
	}
}
