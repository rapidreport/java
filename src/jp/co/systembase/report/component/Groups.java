package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.data.BlankDataSource;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.data.IReportDataSource;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.internal.SortedDataSource;
import jp.co.systembase.report.scanner.IScanner;

public class Groups {

	public GroupDesign design = null;
	public Report report = null;
	public Content parentContent = null;
	public List<Group> groups = null;
	public boolean dataOverridden = false;
	public Group dataSourceGroup = null;

	public Groups(GroupDesign design, Report report, Content parentContent){
		this.design = design;
		this.report = report;
		this.parentContent = parentContent;
	}

	public void fill(Group dataSourceGroup){
		this.dataSourceGroup = dataSourceGroup;
		this.fill(new ReportData(this.dataSourceGroup.data));
	}

	public void fill(ReportData data){
		this.groups = new ArrayList<Group>();
		ReportData _data = data;
		if (this.design.blankData){
			_data = new ReportData(BlankDataSource.getInstance(),
					_data.report, _data.group);
		}
		{
			IReportDataSource dataSource = _data;
			if (this.report.groupDataProvider != null){
				dataSource = this.report.groupDataProvider.getGroupDataSource(this, data);
			}
			if (this.design.sortKeys != null){
				dataSource = new SortedDataSource(_data, this.design.sortKeys, _data.context.getLogger());
			}
			if (dataSource != _data){
				_data = new ReportData(dataSource, data.report, data.group);
				this.dataOverridden = true;
			}
		}
		List<ReportData> dataList;
		if (this.design.splitString != null){
			dataList = this.design.splitString.split(_data);
		}else{
			dataList = this.design.dataSplit(_data);
		}
		if (!Crosstab.fill(this, _data)){
			for(ReportData d: dataList){
				this.addGroup(d);
			}
		}
	}

	public void addGroup(ReportData data){
		this.addGroup(data, null);
	}
	
	public void addGroup(ReportData data, Crosstab.State crosstabState){
		Group g = new Group(this, this.groups.size(), crosstabState);
		this.groups.add(g);
		g.fill(data);
	}

	public Region scan(
			IScanner scanner,
			GroupRange groupRange,
			Region paperRegion){
		return this.scan(scanner, groupRange, paperRegion, paperRegion, null, new Evaluator(this.report.data));
	}

	public Region scan(
			IScanner scanner,
			GroupRange groupRange,
			Region paperRegion,
			Region parentRegion,
			ContentState parentState,
			Evaluator evaluator) {
		IScanner _scanner = scanner.beforeGroups(this, groupRange, parentRegion);
		Region region = parentRegion;
		int i = 0;
		boolean isFirst = true;
		int layoutCount = this.design.layout.getCount(evaluator);
		int lastIndex;
		int lastIndex2;
		Region lastRegion = null;
		int filledCount = groupRange.getGroupCount();
		if (this.design.layout.blank && layoutCount == 0){
			layoutCount = this.getDefaultGroupCount(parentRegion);
		}
		if (parentState != null && parentState.groupState.blank) {
			if (layoutCount > 0){
				lastIndex = 0;
				lastIndex2 = this.design.layout.blank ? layoutCount : 1;
			}else{
				lastIndex = 0;
				lastIndex2 = 1;
			}
		}else{
			if (layoutCount > 0){
				lastIndex = Math.min(filledCount, layoutCount);
				lastIndex2 = this.design.layout.blank ? layoutCount : lastIndex;
			}else{
				lastIndex = filledCount;
				lastIndex2 = lastIndex;
			}
		}
		while(true){
			if (i == lastIndex2){
				break;
			}
			Group g;
			ContentRange contentRange;
			if (i < lastIndex){
				g = groupRange.getGroup(i);
				contentRange = groupRange.getSubRange(g);
			}else{
				g = this.createBlankGroup();
				contentRange = new ContentRange(g);
			}
			GroupState groupState = new GroupState(parentState);
			groupState.first = isFirst;
			groupState.last = (i == lastIndex - 1);
			groupState.last2 = (i == lastIndex2 - 1);
			groupState.index = i;
			groupState.groupFirst = (groupRange.containsFirst() && isFirst);
			groupState.groupLast = groupState.last && groupRange.containsLast();
			groupState.groupLast2 = groupState.last2 && groupRange.containsLast();
			groupState.groupIndex = g.index;
			groupState.blank = (i >= lastIndex);
			groupState.blankFirst = (i == lastIndex);
			groupState.blankLast = groupState.blank && groupState.last2;
			Region groupRegion = this.design.layout.getGroupRegion(parentRegion, lastRegion, i);
			lastRegion = g.scan(
					_scanner,
					contentRange,
					paperRegion,
					groupRegion,
					groupState);
			region = extendRegion(region, lastRegion);
			isFirst = false;
			i++;
		}
		boolean broken = (layoutCount > 0 && layoutCount < filledCount);
		scanner.afterGroups(this, groupRange, parentRegion, region, broken, _scanner);
		return region;
	}

	private Region extendRegion(Region region, Region groupRegion){
		Region ret = new Region();
		ret.top = region.top;
		ret.left = region.left;
		ret.maxBottom = region.maxBottom;
		ret.maxRight = region.maxRight;
		ret.bottom = Math.max(region.bottom, groupRegion.bottom);
		ret.right = Math.max(region.right, groupRegion.right);
		return ret;
	}

	private int getDefaultGroupCount(Region parentRegion){
		int ret = 0;
		float u = 0;
		if (this.design.contentDesigns.size() == 1){
			ContentDesign d = this.design.contentDesigns.get(0);
			if (d.groupDesign == null && !d.size.revInitial){
				u = d.size.initial;
			}
		}
		if (u > 0){
			float t = 0;
			float _t = 0;
			switch (this.design.layout.direction){
			case VERTICAL:
				t = parentRegion.getMaxHeight();
				break;
			case HORIZONTAL:
				t = parentRegion.getMaxWidth();
				break;
			}
			while(true){
				_t += u;
				if (_t <= t){
					ret++;
				}else{
					break;
				}
			}
		}
		return ret;
	}

	private Group createBlankGroup(){
		Group ret = new Group(this, -1, null);
		Group g = null;
		if (this.parentContent != null){
			g = this.parentContent.parentGroup;
		}
		ret.fill(new ReportData(DummyDataSource.getInstance(), this.report, g));
		return ret;
	}

	public Content getNextContent(ContentHistory ch){
		if (this.groups.size() == 0){
			return null;
		}
		if (ch == null){
			return this.groups.get(0).getNextContent(null);
		}else{
			Content c = ch.group.getNextContent(ch);
			if (c != null){
				return c;
			}else{
				int i = ch.group.index;
				if (i < this.groups.size() - 1){
					return this.groups.get(i + 1).getNextContent(null);
				}else{
					return null;
				}
			}
		}
	}

	public ContentHistory getNextContentHistory(ContentHistory ch){
		Content c = this.getNextContent(ch);
		if (c != null){
			return ContentHistory.getInstance(c, this);
		}else{
			return null;
		}
	}

	@Override
	public String toString() {
		String p;
		if (this.parentContent != null){
			p = this.parentContent.toString() + ":";
		}else{
			p = this.report.toString() + ":";
		}
		if (this.design.caption != null){
			return p + this.design.caption;
		}else if (this.design.id != null){
			return p + this.design.id;
		}else{
			return p + "(group)";
		}
	}

}
