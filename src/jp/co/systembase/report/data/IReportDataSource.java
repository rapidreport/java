package jp.co.systembase.report.data;

import jp.co.systembase.report.component.UnknownFieldException;

public interface IReportDataSource {
	Object get(int i, String key) throws UnknownFieldException;
	int size();
}
