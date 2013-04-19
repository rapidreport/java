package jp.co.systembase.report.component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.expression.IExpression;


public class DataCache {

	private Map<ReportData,
                Map<String,
                  Map<Integer, Object>>> _customField =
      new HashMap<ReportData,
            Map<String,
              Map<Integer, Object>>>();

	private Map<IReportDataSource,
	          Map<String,
                Map<Integer, BigDecimal>>> _summary =
      new HashMap<IReportDataSource,
            Map<String,
              Map<Integer, BigDecimal>>>();

	private Map<IReportDataSource,
              Map<String,
                Map<Integer, Integer>>> _count =
      new HashMap<IReportDataSource,
            Map<String,
              Map<Integer, Integer>>>();

	private Map<ReportData,
              Map<String,
                Map<Integer, BigDecimal>>> _customFieldSummary =
      new HashMap<ReportData,
            Map<String,
              Map<Integer, BigDecimal>>>();

	private Map<ReportData,
              Map<String,
                Map<Integer, Integer>>> _customFieldCount =
      new HashMap<ReportData,
            Map<String,
              Map<Integer, Integer>>>();

	public Map<String, IExpression> expression =
		new HashMap<String, IExpression>();

	public Map<Integer, Object> customField(
			ReportData data,
			String key){
		if (!this._customField.containsKey(data)){
			this._customField.put(data,
					new HashMap<String,
					      Map<Integer, Object>>());
		}
		if (!this._customField.get(data).containsKey(key)){
			this._customField.get(data).put(key,
					new HashMap<Integer, Object>());
		}
		return this._customField.get(data).get(key);
	}

	public Map<Integer, BigDecimal> summary(
			IReportDataSource dataSource,
			String key){
		if (!this._summary.containsKey(dataSource)){
			this._summary.put(dataSource,
					new HashMap<String,
					      Map<Integer, BigDecimal>>());
		}
		if (!this._summary.get(dataSource).containsKey(key)){
			this._summary.get(dataSource).put(key,
					new HashMap<Integer, BigDecimal>());
		}
		return this._summary.get(dataSource).get(key);
	}

	public Map<Integer, Integer> count(
			IReportDataSource dataSource,
			String key){
		if (!this._count.containsKey(dataSource)){
			this._count.put(dataSource,
					new HashMap<String,
					      Map<Integer, Integer>>());
		}
		if (!this._count.get(dataSource).containsKey(key)){
			this._count.get(dataSource).put(key,
					new HashMap<Integer, Integer>());
		}
		return this._count.get(dataSource).get(key);
	}

	public Map<Integer, BigDecimal> customFieldSummary(
			ReportData data,
			String key){
		if (!this._customFieldSummary.containsKey(data)){
			this._customFieldSummary.put(data,
					new HashMap<String,
					      Map<Integer, BigDecimal>>());
		}
		if (!this._customFieldSummary.get(data).containsKey(key)){
			this._customFieldSummary.get(data).put(key,
					new HashMap<Integer, BigDecimal>());
		}
		return this._customFieldSummary.get(data).get(key);
	}

	public Map<Integer, Integer> customFieldCount(
			ReportData data,
			String key){
		if (!this._customFieldCount.containsKey(data)){
			this._customFieldCount.put(data,
					new HashMap<String,
					      Map<Integer, Integer>>());
		}
		if (!this._customFieldCount.get(data).containsKey(key)){
			this._customFieldCount.get(data).put(key,
					new HashMap<Integer, Integer>());
		}
		return this._customFieldCount.get(data).get(key);
	}

}
