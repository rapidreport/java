package jp.co.systembase.report.scanner;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.report.component.Content;
import jp.co.systembase.report.component.ContentRange;
import jp.co.systembase.report.component.ContentState;
import jp.co.systembase.report.component.Group;
import jp.co.systembase.report.component.GroupDesign;
import jp.co.systembase.report.component.GroupLayoutDesign;
import jp.co.systembase.report.component.GroupRange;
import jp.co.systembase.report.component.GroupState;
import jp.co.systembase.report.component.Groups;
import jp.co.systembase.report.component.Region;


public class PagingScanner extends DefaultScanner {

	public Map<GroupDesign, Group> appearedGroups = new HashMap<GroupDesign, Group>();
	public Map<GroupDesign, Group> startedGroups = new HashMap<GroupDesign, Group>();
	public Map<GroupDesign, Group> finishedGroups = new HashMap<GroupDesign, Group>();
	public boolean broken = false;
	public int weight = 0;

	@Override
	public void afterContent(
			Content content,
			GroupRange groupRange,
			Region parentRegion,
			ContentState contentState,
			Region region,
			IScanner scanner) {
		if (region != null){
			if (region.isVOverflowed()){
				this.broken = true;
			}
			if (region.isHOverflowed()){
				this.broken = true;
			}
			this.weight += content.design.weight;
		}
	}

	@Override
	public void afterGroup(
			Group group,
			ContentRange contentRange,
			Region parentRegion,
			GroupState groupState,
			Region region,
			IScanner scanner) {
		GroupDesign gd = group.parentGroups.design;
		if (gd.pageBreak && this.appearedGroups.containsKey(gd)){
			this.broken = true;
		}
		if (!this.appearedGroups.containsKey(gd)){
			this.appearedGroups.put(gd, group);
		}
		if (contentRange.first.groupFirst){
			if (!this.startedGroups.containsKey(gd)){
				this.startedGroups.put(gd, group);
			}
		}
		if (contentRange.last == null || contentRange.last.groupLast){
			if (!this.finishedGroups.containsKey(gd)){
				this.finishedGroups.put(gd, group);
			}
		}
	}

	@Override
	public void afterGroups(
			Groups groups,
			GroupRange groupRange,
			Region parentRegion,
			Region region,
			boolean broken,
			IScanner scanner) {
		GroupLayoutDesign l = groups.design.layout;
		if (!l.clipOverflow && broken){
			this.broken = true;
		}
	}

}
