package jp.co.systembase.report.data.internal;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;

public class WrapperDataSource implements IReportDataSource{

	public List<ReportData> dataList =
		new ArrayList<ReportData>();

	public int size() {
		return this.dataList.size();
	}

	public Object get(int i, String key) {
		return this.dataList.get(i).getRecord().get(key);
	}

}
