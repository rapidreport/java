package jp.co.systembase.report.data;

import jp.co.systembase.report.component.Groups;

public interface IGroupDataProvider {
	IReportDataSource getGroupDataSource(Groups groups, ReportData data);
}
