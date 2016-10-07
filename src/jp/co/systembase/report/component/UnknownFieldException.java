package jp.co.systembase.report.component;

import jp.co.systembase.report.data.IReportDataSource;

public class UnknownFieldException extends Exception {
	private static final long serialVersionUID = -5094751437773797411L;
	public IReportDataSource dataSource;
	public String key;
	public int i;
	public UnknownFieldException(IReportDataSource dataSource, int i, String key){
		super("不明な列です：" + key + " 行番号：" + i);
		this.dataSource = dataSource;
		this.key = key;
		this.i = i;
	}
}
