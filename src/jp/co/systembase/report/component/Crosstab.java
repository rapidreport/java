package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.internal.CrosstabDataSource;

public class Crosstab {

	public static class Setting{
		
		public List<String> vKeys = null;
		public List<String> hKeys = null;
		public int vCount = 0;
		public int hCount = 0;
		
		public Setting(GroupDesign groupDesign){
			this._init(groupDesign);
		}
		
		private void _init(GroupDesign groupDesign){
			switch(groupDesign.crosstabPartType){
			case VDETAIL:
				this.vKeys = groupDesign.keys;
				if (groupDesign.layout != null){
					this.vCount = groupDesign.layout.maxCount;
				}
				break;
			case HDETAIL:
				this.hKeys = groupDesign.keys;
				if (groupDesign.layout != null){
					this.hCount = groupDesign.layout.maxCount;
				}
				break;
			default:
			}
			for(ContentDesign cd: groupDesign.contentDesigns){
				if (cd.groupDesign != null){
					this._init(cd.groupDesign);
				}
				if (cd.subContentDesigns != null){
					for(ContentDesign scd: cd.subContentDesigns){
						if (scd.groupDesign != null){
							this._init(scd.groupDesign);
						}
					}
				}
			}
		}
		
		public boolean isValid(){
			return this.vKeys != null && this.hKeys != null && 
					this.vCount > 0 && this.hCount > 0; 
		}
		
	}

	public static class DataSet{
		public ReportData orgData; 
		public List<CrosstabDataSource> captions;
		public List<CrosstabDataSource> summaries;
		public List<CrosstabDataSource> vDetails;
		public Map<CrosstabDataSource, List<CrosstabDataSource>> hDetails;
	}

	public static class State{
		
		public Setting setting;
		public DataSet dataSet;
		public int vIndex;
		public int hIndex;
		public boolean vLast;
		public boolean hLast;
		
		public State(Setting setting, DataSet dataSet, int vIndex, int hIndex, boolean vLast, boolean hLast){
			this.setting = setting;
			this.dataSet = dataSet;
			this.vIndex = vIndex;
			this.hIndex = hIndex;
			this.vLast = vLast;
			this.hLast = hLast;
		}
	}

	private Crosstab() {}

	public static boolean fill(Groups groups, ReportData data){
		switch(groups.design.crosstabPartType){
		case ROOT:
			return _fill_root(groups, data);
		case CAPTION:
			return _fill_caption(groups, data);
		case VDETAIL:
			return _fill_vDetail(groups, data);
		case HDETAIL:
			return _fill_hDetail(groups, data);
		case SUMMARY:
			return _fill_summary(groups, data);
		default:
			return false;
		}
	}
	
	private static boolean _fill_root(Groups groups, ReportData data){
		Setting setting = new Setting(groups.design);
		if (!setting.isValid()){
			return false;
		}
		DataSet ds = _createDataSet(groups, setting, data);
		int vc = (ds.vDetails.size() + setting.vCount - 1) / setting.vCount;
		int hc = (ds.captions.size() + setting.hCount - 1) / setting.hCount;
		for(int v = 0;v < vc;v++){
			for(int h = 0;h < hc;h++){
				groups.addGroup(new ReportData(ds.orgData), new State(setting, ds, v, h, v == vc - 1, h == hc - 1));
			}
		}
		return true;
	}
	
	private static boolean _fill_caption(Groups groups, ReportData data){
		State state = _getState(groups);
		if (state == null){
			return false;
		}
		int b = state.hIndex * state.setting.hCount;
		for(int i = 0;i < state.setting.hCount;i++){
			int _i = i + b;
			if (_i >= state.dataSet.captions.size()){
				break;
			}
			groups.addGroup(new ReportData(state.dataSet.captions.get(_i), data.report, data.group));
		}
		return true;
	}
	
	private static boolean _fill_vDetail(Groups groups, ReportData data){
		State state = _getState(groups);
		if (state == null){
			return false;
		}
		int b = state.vIndex * state.setting.vCount;
		for(int i = 0;i < state.setting.vCount;i++){
			int _i = i + b;
			if (_i >= state.dataSet.vDetails.size()){
				break;
			}
			groups.addGroup(new ReportData(state.dataSet.vDetails.get(_i), data.report, data.group));
		}
		return true;
	}
	
	private static boolean _fill_hDetail(Groups groups, ReportData data){
		State state = _getState(groups);
		if (state == null){
			return false;
		}
		if (data.dataSource instanceof CrosstabDataSource){
			List<CrosstabDataSource> row = state.dataSet.hDetails.get(data.dataSource);
			int b = state.hIndex * state.setting.hCount;
			for(int i = 0;i < state.setting.hCount;i++){
				int _i = i + b;
				if (_i >= row.size()){
					break;
				}
				groups.addGroup(new ReportData(row.get(_i), data.report, data.group));
			}
		}
		return true;
	}
	
	private static boolean _fill_summary(Groups groups, ReportData data){
		State state = _getState(groups);
		if (state == null){
			return false;
		}
		int b = state.hIndex * state.setting.hCount;
		for(int i = 0;i < state.setting.hCount;i++){
			int _i = i + b;
			if (_i >= state.dataSet.summaries.size()){
				break;
			}
			groups.addGroup(new ReportData(state.dataSet.summaries.get(_i), data.report, data.group));
		}
		return true;
	}

	private static State _getState(Groups groups){
		if (groups.parentContent != null){
			return groups.parentContent.parentGroup.crosstabState;
		}
		return null;
	}
	
	private static DataSet _createDataSet(Groups groups, Setting setting, ReportData data){
		DataSet ret = new DataSet();
		List<String> keys = new ArrayList<String>();
		keys.addAll(setting.vKeys);
		keys.addAll(setting.hKeys);
		CrosstabDataSource vSorted = new CrosstabDataSource(data, keys);
		CrosstabDataSource hSorted = new CrosstabDataSource(data, setting.hKeys);
		List<CrosstabDataSource> hMasterList = new ArrayList<CrosstabDataSource>();
		Map<CrosstabDataSource, Integer> hMasterMap = new HashMap<CrosstabDataSource, Integer>();
		ret.orgData = data;
		{
			CrosstabDataSource master = null;
			if (groups.design.id != null){
				IReportDataSource d = groups.report.getCrosstabCaptionDataSource(groups.design.id);
				if (d != null){
					master = new CrosstabDataSource(d);
				}
			}
			if (master == null){
				master = hSorted;
			}
			ret.captions = _dataSplit(master, setting.hKeys);
			for(CrosstabDataSource d: ret.captions){
				CrosstabDataSource _d = d.dummy(setting.hKeys);
				hMasterMap.put(_d, hMasterList.size());
				hMasterList.add(_d);
			}
		}
		ret.vDetails = _dataSplit(vSorted, setting.vKeys);
		ret.hDetails = new HashMap<CrosstabDataSource, List<CrosstabDataSource>>();
		for(CrosstabDataSource d: ret.vDetails){
			List<CrosstabDataSource> row = new ArrayList<CrosstabDataSource>(hMasterList);
			for(CrosstabDataSource _d: _dataSplit(d, setting.hKeys)){
				if (hMasterMap.containsKey(_d)){
					row.set(hMasterMap.get(_d), _d);
				}
			}
			ret.hDetails.put(d, row);
		}
		ret.summaries = new ArrayList<CrosstabDataSource>(hMasterList);
		for(CrosstabDataSource d: _dataSplit(hSorted, setting.hKeys)){
			if (hMasterMap.containsKey(d)){
				ret.summaries.set(hMasterMap.get(d), d);
			}
		}
		
		return ret;
	}
	
	private static boolean _keysEq(List<String> keys, CrosstabDataSource data1, int i1, CrosstabDataSource data2, int i2){
		for(String key: keys){
			if (!ReportUtil.eq(data1.get(i1, key), data2.get(i2, key))){
				return false;
			}
		}
		return true;
	}

	private static List<CrosstabDataSource> _dataSplit(CrosstabDataSource data, List<String> keys){
		List<CrosstabDataSource> ret = new ArrayList<CrosstabDataSource>();
		int i = 0;
		while(i < data.size()){
			int j = i + 1;
			while(j < data.size()){
				if (!_keysEq(keys, data, i, data, j)){
					break;
				}
				j++;
			}
			ret.add(data.part(i, j).setKeys(keys));
			i = j;
		}
		return ret;
	}

}
