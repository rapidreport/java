package jp.co.systembase.report.data;

import jp.co.systembase.report.component.UnknownFieldException;

public class BlankDataSource implements IReportDataSource, INoCache {

	private static BlankDataSource instance = new BlankDataSource();

	public static BlankDataSource getInstance(){
		return instance;
	}

	private BlankDataSource(){}

	public Object get(int i, String key) throws UnknownFieldException {
		return null;
	}

	public int size() {
		return 0;
	}

}