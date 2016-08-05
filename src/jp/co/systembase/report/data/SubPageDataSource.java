package jp.co.systembase.report.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.report.ReportPage;
import jp.co.systembase.report.ReportPages;
import jp.co.systembase.report.component.Group;
import jp.co.systembase.report.component.GroupDesign;

public class SubPageDataSource implements IReportDataSource {

	public List<String> subPageKeys;

	private List<ReportDataRecord> recordList = new ArrayList<ReportDataRecord>();
	private List<Map<String, Integer>> pageIndexList = new ArrayList<Map<String, Integer>>();

	private ReportDataRecord currentRecord = null;
	private Map<String, Integer> currentPageIndex = null;
	private int currentSubPageIndex = 0;

	public SubPageDataSource(ReportPages pages , String pageGroupId, String...subPageKeys){
		this.subPageKeys = Arrays.asList(subPageKeys);
		for(int i = 0;i < pages.size();i++){
			ReportPage page = pages.get(i);
			if(pageGroupId != null){
				Group g = page.findStartedGroup(pageGroupId);
				if (g != null){
					this.addBreak(g.data.getRecord());
				}
			}
			if (this.currentRecord == null){
				this.addBreak(page.report.data.getRecord());
			}
			this.addSubPage(i);
		}
	}

	public void addSubPage(int pageIndex){
		if (this.currentSubPageIndex == 0){
			this.recordList.add(this.currentRecord);
			this.pageIndexList.add(this.currentPageIndex);
		}
		this.currentPageIndex.put(this.subPageKeys.get(this.currentSubPageIndex), pageIndex);
		this.currentSubPageIndex++;
		if (this.currentSubPageIndex == this.subPageKeys.size()){
			this.addBreak(this.currentRecord);
		}
	}

	public void addBreak(ReportDataRecord record){
		this.currentRecord = record;
		this.currentPageIndex = new HashMap<String, Integer>();
		this.currentSubPageIndex = 0;
	}

	public Object get(int i, String key) {
		if (this.subPageKeys.contains(key)){
			if (this.pageIndexList.get(i).containsKey(key)){
				return this.pageIndexList.get(i).get(key);
			}else{
				return null;
			}
		}else{
			return this.recordList.get(i).get(key);
		}
	}

	public int size() {
		return this.recordList.size();
	}

	public ReportData groupData(GroupDesign groupDesign, ReportData data) {
		return data;
	}

}
