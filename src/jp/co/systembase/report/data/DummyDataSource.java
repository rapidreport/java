package jp.co.systembase.report.data;


public class DummyDataSource implements IReportDataSource, INoCache {

	private static DummyDataSource instance = new DummyDataSource();

	public static DummyDataSource getInstance(){
		return instance;
	}

	private DummyDataSource(){}

	public Object get(int i, String key) throws UnknownFieldException {
		return null;
	}

	public int size() {
		return 1;
	}

}
