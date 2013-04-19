package jp.co.systembase.report.renderer;

public class RenderException extends Exception {
	private static final long serialVersionUID = -1566898799521259792L;
	public RenderException(String message){
		super(message);
	}
	public RenderException(String message, Throwable cause){
		super(message, cause);
	}
}
