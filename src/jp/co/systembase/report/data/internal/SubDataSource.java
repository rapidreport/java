package jp.co.systembase.report.data.internal;

import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.UnknownFieldException;

public class SubDataSource implements IReportDataSource{

	private IReportDataSource dataSource;
	private int beginIndex;
	private int endIndex;

	public SubDataSource(
			IReportDataSource dataSource,
			int beginIndex,
			int endIndex){
		this.dataSource = dataSource;
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	public Object get(int i, String key) throws UnknownFieldException {
		if (this.beginIndex < 0 || i < 0 || i >= this.size()){
			throw new ArrayIndexOutOfBoundsException();
		}
		return this.dataSource.get(this.beginIndex + i, key);
	}

	public int size() {
		return this.endIndex - this.beginIndex;
	}

}
