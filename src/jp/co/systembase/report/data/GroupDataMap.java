package jp.co.systembase.report.data;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.Report;

public class GroupDataMap {
	private Map<String, IReportDataSource> dataMap = new HashMap<String, IReportDataSource>();
	private Map<String, Report.EGroupDataMode> modeMap = new HashMap<String, Report.EGroupDataMode>();
	public void put(String key, IReportDataSource dataSource){
		this.put(key, dataSource, Report.EGroupDataMode.DEFAULT_BLANK);
	}
	public void put(String key, IReportDataSource dataSource, Report.EGroupDataMode mode){
		this.dataMap.put(key, dataSource);
		this.modeMap.put(key, mode);
	}
	public boolean containsKey(String key){
		return this.dataMap.containsKey(key);
	}
	public IReportDataSource getDataSource(String key){
		return this.dataMap.get(key);
	}
	public Report.EGroupDataMode getDataMode(String key){
		return this.modeMap.get(key);
	}
}
