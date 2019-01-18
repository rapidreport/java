package jp.co.systembase.report.scanner;

import jp.co.systembase.report.component.Content;
import jp.co.systembase.report.component.ContentRange;
import jp.co.systembase.report.component.ContentState;
import jp.co.systembase.report.component.Group;
import jp.co.systembase.report.component.GroupRange;
import jp.co.systembase.report.component.GroupState;
import jp.co.systembase.report.component.Groups;
import jp.co.systembase.report.component.Region;

public interface IScanner {
	public IScanner beforeContent(
			Content content,
			GroupRange groupRange,
			Region parentRegion,
			ContentState contentState);
	public void afterContent(
			Content content,
			GroupRange groupRange,
			Region parentRegion,
			ContentState contentState,
			Region region,
			IScanner scanner);
	public IScanner beforeGroups(
			Groups groups,
			GroupRange groupRange,
			Region parentRegion);
	public void afterGroups(
			Groups groups,
			GroupRange groupRange,
			Region parentRegion,
			Region region,
			boolean broken,
			IScanner scanner);
	public IScanner beforeGroup(
			Group group,
			ContentRange contentRange,
			Region parentRegion,
			GroupState groupState);
	public void afterGroup(
			Group group,
			ContentRange contentRange,
			Region parentRegion,
			GroupState groupState,
			Region region,
			IScanner scanner);
    public void scanSubContent(
    		Content content,
    		Region parentRegion,
    		ContentState contentState,
    		Region region,
            Region paperRegion,
            boolean background);
}
