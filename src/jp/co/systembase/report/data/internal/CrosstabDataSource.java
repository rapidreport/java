package jp.co.systembase.report.data.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.UnknownFieldException;

public class CrosstabDataSource implements IReportDataSource {

	private IReportDataSource _dataSource = null;
	private List<Integer> _indexes = null;
	private List<String> _keys = null;
	private boolean _mask = false;

	public CrosstabDataSource(IReportDataSource dataSource){
		this._dataSource = dataSource;
		this._indexes = new ArrayList<Integer>();
		for(int i = 0;i < this._dataSource.size();i++){
			this._indexes.add(i);
		}
	}

	public CrosstabDataSource(IReportDataSource dataSource, List<String> keys){
		this(dataSource);
		Collections.sort(this._indexes, new RecordComparator(this._dataSource, keys, null));
	}

	private CrosstabDataSource(){}
	
	public CrosstabDataSource part(int i, int j){
		CrosstabDataSource ret = new CrosstabDataSource();
		ret._dataSource = this._dataSource;
		ret._indexes = this._indexes.subList(i, j);
		return ret;
	}

	public CrosstabDataSource dummy(List<String> keys){
		CrosstabDataSource ret = new CrosstabDataSource();
		ret._dataSource = this._dataSource;
		ret._indexes = this._indexes.subList(0, 1);
		ret.setKeys(keys);
		ret._mask = true;
		return ret;
	}

	public CrosstabDataSource setKeys(List<String> keys){
		this._keys = keys;
		return this;
	}
	
	public int size() {
		return this._indexes.size();
	}

	public Object get(int i, String key) {
		try {
			if (this._mask){
				if (!this._keys.contains(key)){
					return null;
				}
			}
			return this._dataSource.get(this._indexes.get(i), key);
		} catch (UnknownFieldException e) {
			return null;	
		}
	}

	@Override
	public int hashCode() {
		if (this._keys != null){
			int ret = 0;
			for(int i = 0;i < this._keys.size();i++){
				Object v = this.get(0, this._keys.get(i));
				if (v != null){
					ret ^= (v.hashCode() << i);
				}
			}
			return ret;
		}else{
			return super.hashCode();	
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this._keys != null){
			if (obj == this){
				return true;
			}else if (!(obj instanceof CrosstabDataSource)){
				return false;
			}else{
				for(int i = 0;i < this._keys.size();i++){
					Object v1 = this.get(0, this._keys.get(i));
					Object v2 = ((CrosstabDataSource)obj).get(0, this._keys.get(i));
					if (v1 == null){
						if (v2 != null){
							return false;
						}
					}else if (!v1.equals(v2)){
						return false;
					}
				}
			}
			return true;
		}else{
			return super.equals(obj);	
		}
	}
	
	

}
