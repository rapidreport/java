package jp.co.systembase.report.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.report.IReportLogger;
import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.GroupDesign;
import jp.co.systembase.report.component.Groups;
import jp.co.systembase.report.data.internal.SubDataSource;

public class GroupDataProvider implements IGroupDataProvider {

	public static class GroupDataMap {
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

	public GroupDataMap groupDataMap;

	private Map<String, Map<List<?>, IReportDataSource>> groupDataCache =
		new HashMap<String, Map<List<?>, IReportDataSource>>();
	private Map<GroupDesign, List<String>> keyNamesCache =
		new HashMap<GroupDesign, List<String>>();

	public GroupDataProvider(){
		this(new GroupDataMap());
	}

	public GroupDataProvider(GroupDataMap groupDataMap){
		this.groupDataMap = groupDataMap;
	}

	public IReportDataSource getGroupDataSource(Groups groups, ReportData data) {
		String groupId = groups.design.id;
		if (groupId != null && this.groupDataMap.containsKey(groupId)){
			switch(this.groupDataMap.getDataMode(groupId)){
			case NO_SPLIT:
				return this.groupDataMap.getDataSource(groupId);
			default:
				List<String> keyNames = getKeyNames(groups.design);
				if (keyNames.size() == 0){
					return this.groupDataMap.getDataSource(groupId);
				}else{
					if (!this.groupDataCache.containsKey(groupId)){
						this.groupDataCache.put(
								groupId,
								splitData(keyNames,
									this.groupDataMap.getDataSource(groupId),
									data.report.design.setting.logger));
					}
					List<?> keys = getKeys(keyNames, data);
					if (this.groupDataCache.get(groupId).containsKey(keys)){
						return this.groupDataCache.get(groupId).get(keys);
					}else{
						switch(this.groupDataMap.getDataMode(groupId)){
						case DEFAULT_DUMMY:
							return DummyDataSource.getInstance();
						default:
							return BlankDataSource.getInstance();
						}
					}
				}
			}
		}else{
			return data;
		}
	}

	private List<String> getKeyNames(GroupDesign groupDesign){
		if (!this.keyNamesCache.containsKey(groupDesign)){
			List<String> keyNames = new ArrayList<String>();
			if (groupDesign.parentContentDesign != null){
				getKeyNames_aux(groupDesign.parentContentDesign.parentGroupDesign, keyNames);
			}
			this.keyNamesCache.put(groupDesign, keyNames);
		}
		return this.keyNamesCache.get(groupDesign);
	}

	private static void getKeyNames_aux(GroupDesign groupDesign, List<String> keyNames){
		if (groupDesign.parentContentDesign != null){
			getKeyNames_aux(groupDesign.parentContentDesign.parentGroupDesign, keyNames);
		}
		if (!groupDesign.detail && groupDesign.keys != null){
			keyNames.addAll(groupDesign.keys);
		}
	}

	private static List<?> getKeys(
			List<String> keyNames,
			ReportData data){
		List<Object> ret = new ArrayList<Object>();
		ReportDataRecord r = data.getRecord();
		for(String k: keyNames){
			ret.add(ReportUtil.eqRegularize(r.get(k)));
		}
		return ret;
	}

	private static List<?> getKeys(
			List<String> keyNames,
			IReportDataSource dataSource,
			int index,
			IReportLogger logger){
		List<Object> ret = new ArrayList<Object>();
		for(String k: keyNames){
			try{
				ret.add(ReportUtil.eqRegularize(dataSource.get(index, k)));
			}catch(UnknownFieldException ex){
				if (logger != null){
					logger.unknownFieldError(ex);
				}
				ret.add(null);
			}
		}
		return ret;
	}

	private static Map<List<?>, IReportDataSource> splitData(
			List<String> keyNames,
			IReportDataSource dataSource,
			IReportLogger logger){
		Map<List<?>, IReportDataSource> ret = new HashMap<List<?>, IReportDataSource>();
		List<?> keys = null;
		int beginIndex = 0;
		for(int i = 0;i < dataSource.size();i++){
			List<?> _keys = getKeys(keyNames, dataSource, i, logger);
			if (!_keys.equals(keys)){
				if (keys != null && !ret.containsKey(keys)){
					ret.put(keys, new SubDataSource(dataSource, beginIndex, i));
				}
				keys = _keys;
				beginIndex = i;
			}
		}
		if (keys != null && !ret.containsKey(keys) && beginIndex < dataSource.size()){
			ret.put(keys, new SubDataSource(dataSource, beginIndex, dataSource.size()));
		}
		return ret;
	}

}
