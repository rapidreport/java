package jp.co.systembase.report.data;


public interface IReportDataSource {
	Object get(int i, String key) throws UnknownFieldException;
	int size();
}
