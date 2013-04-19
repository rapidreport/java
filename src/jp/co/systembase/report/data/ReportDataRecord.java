package jp.co.systembase.report.data;

public class ReportDataRecord {

	public ReportData data;
	public int i;

	public ReportDataRecord(
			ReportData data,
			int i){
		this.data = data;
		this.i = i;
	}

	public Object get(String key){
		return this.data.get(this.i, key);
	}

}
