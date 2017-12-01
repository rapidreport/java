package jp.co.systembase.report.data.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import jp.co.systembase.report.IReportLogger;
import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.UnknownFieldException;


public class SortedDataSource implements IReportDataSource {

	private IReportDataSource _dataSource;
	private List<Integer> _indexes;
	private IReportLogger _logger;
	
	public SortedDataSource(
			IReportDataSource reportData,
			List<String> keys,
			IReportLogger logger){
		this._dataSource = reportData;
		this._logger = logger;
		this._indexes = new ArrayList<Integer>();
		for(int i = 0;i < this._dataSource.size();i++){
			this._indexes.add(i);
		}
		Collections.sort(this._indexes, new RecordComparator(this._dataSource, keys, this._logger));
	}

	public int size() {
		return this._indexes.size();
	}

	public Object get(int i, String key) {
		try{
			return this._dataSource.get(this._indexes.get(i), key);	
		}catch(UnknownFieldException ex){
			if (this._logger != null){
				this._logger.unknownFieldError(ex);
			}
			return null;
		}
	}

}
