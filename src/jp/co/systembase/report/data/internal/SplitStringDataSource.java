package jp.co.systembase.report.data.internal;

import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.UnknownFieldException;

public class SplitStringDataSource implements IReportDataSource {

	private ReportData data;
	private String key;
	private String value;

	public SplitStringDataSource(ReportData data, String key, String value) {
		this.data = data;
		this.key = key;
		this.value = value;
	}

	@Override
	public Object get(int i, String key) throws UnknownFieldException {
		if (this.key.equals(key)){
			return this.value;
		}else{
			return this.data.get(i, key);
		}
	}

	@Override
	public int size() {
		return this.data.size();
	}

}
