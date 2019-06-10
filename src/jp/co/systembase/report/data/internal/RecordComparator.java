package jp.co.systembase.report.data.internal;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import jp.co.systembase.report.IReportLogger;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.UnknownFieldException;

public class RecordComparator implements Comparator<Integer> {

	public IReportDataSource _dataSource;
	public List<String> _keys;
	public IReportLogger _logger;

	public RecordComparator(IReportDataSource dataSource, List<String> keys, IReportLogger logger){
		this._dataSource = dataSource;
		this._keys = keys;
		this._logger = logger;
	}

	public int compare(Integer i1, Integer i2) {
		for(String k: this._keys){
			Object v1 = null;
			Object v2 = null;
			try {
				v1 = ReportUtil.regularize(this._dataSource.get(i1, k));
				v2 = ReportUtil.regularize(this._dataSource.get(i2, k));
			} catch (UnknownFieldException ex) {
				if (this._logger != null){
					this._logger.unknownFieldError(ex);
				}
			}
			if (v1 == null && v2 == null){
				continue;
			}
			if (v1 == null){
				return -1;
			}
			if (v2 == null){
				return 1;
			}
			if (v1.equals(v2)){
				continue;
			}
			if (v1 instanceof Boolean){
				if ((Boolean)v1){
					return 1;
				}
				if ((Boolean)v2){
					return -1;
				}
			}else if (v1 instanceof BigDecimal){
				return ((BigDecimal)v1).compareTo((BigDecimal)v2);
			}else if (v1 instanceof String){
				return ((String)v1).compareTo((String)v2);
			}
		}
		return i1 - i2;
	}
}
