package jp.co.systembase.report.component;

public class EvalException extends Exception {
	private static final long serialVersionUID = 923075502067181800L;
	public EvalException(String message){
		super(message);
	}
	public EvalException(String message, Throwable cause){
		super(message, cause);
	}
}
