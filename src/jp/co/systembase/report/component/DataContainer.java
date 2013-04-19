package jp.co.systembase.report.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.data.ReportData;

public class DataContainer {

	public static class PageRange{

		public int beginIndex = -1;
		public int endIndex = -1;
		public int pastIndex = -1;

		public void updateRange(ReportData data){
			if (this.beginIndex == -1){
				this.beginIndex = data.beginIndex;
			}
			this.endIndex = data.endIndex;
			this.pastIndex = this.endIndex;
		}

		public void updatePast(int index){
			this.pastIndex = index;
		}

		public int getPresentIndex(boolean prior){
			if (prior && this.beginIndex > -1){
				return this.beginIndex;
			}else{
				return this.pastIndex;
			}
		}

	}

	public Map<ReportData, PageRange> pageRangeMap =
		new HashMap<ReportData, PageRange>();

	public void initializeData(Group g){
		if (!this.pageRangeMap.containsKey(g.getReport().data)){
			this.pageRangeMap.put(g.getReport().data, new PageRange());
		}
		this.pageRangeMap.put(g.data, new PageRange());
	}

	public void updateData(Content c){
		boolean leaf = isLeafContent(c);
		boolean pre = isPriorContent(c);
		boolean past = isPosteriorContent(c);
		ReportData data = c.getData();
		ReportData _data = data;
		while(_data != null){
			if (!this.pageRangeMap.containsKey(_data)){
				break;
			}
			if (!_data.hasSameSource(data)){
				break;
			}
			if (leaf){
				this.pageRangeMap.get(_data).updateRange(data);
			}else if (pre){
				this.pageRangeMap.get(_data).updatePast(data.beginIndex);
			}else if (past){
				this.pageRangeMap.get(_data).updatePast(data.endIndex);
			}
			if (!_data.isAggregateSrc()){
				break;
			}
			_data = _data.getParentData();
		}
	}

	private static boolean isLeafContent(Content c){
		if (!c.design.aggregateSrc){
			return false;
		}
		Groups gs = c.groups;
		if (gs == null){
			return true;
		}
		if (gs.dataOverridden){
			return true;
		}
		if (gs.design.getAggregateSrcContentDesign() == null){
			return true;
		}
		return false;
	}

	private static boolean isPriorContent(Content c){
		Content ac = c.parentGroup.getAggregateSrcContent();
		return (ac != null && c.getIndex() < ac.getIndex());
	}

	private static boolean isPosteriorContent(Content c){
		Content ac = c.parentGroup.getAggregateSrcContent();
		return (ac != null && c.getIndex() > ac.getIndex());
	}

	public ReportData getPresentData(Content content, String scope){
		if ("group1".equals(content.parentGroup.getDesign().id) &&
				content.design.getIndex() == 0){
			System.out.print("");
		}
		ReportData scopeData = content.getData().findScope(scope);
		if (scopeData == null){
			throw new IllegalArgumentException("invalid scope" + (!scope.equals("") ? ": " + scope : ""));
		}
		if (!scopeData.hasSameSource(content.getData())){
			throw new IllegalArgumentException("data was overriden");
		}
		if (!this.pageRangeMap.containsKey(content.getData())){
			return ReportData.getEmptyData(scopeData);
		}
		return new ReportData(scopeData, scopeData.beginIndex,
				this.pageRangeMap.get(content.getData()).getPresentIndex(isPriorContent(content)));
	}

	public ReportData getPageData(Content content, String scope){
		ReportData scopeData = content.getData().findScope(scope);
		if (scopeData == null){
			throw new IllegalArgumentException("invalid scope" + (!scope.equals("") ? ": " + scope : ""));
		}
		if (!scopeData.hasSameSource(content.getData())){
			throw new IllegalArgumentException("data was overriden");
		}
		if (!this.pageRangeMap.containsKey(scopeData)){
			return ReportData.getEmptyData(scopeData);
		}
		PageRange pageRange = this.pageRangeMap.get(scopeData);
		return new ReportData(scopeData, pageRange.beginIndex, pageRange.endIndex);
	}

}
