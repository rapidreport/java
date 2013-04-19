package jp.co.systembase.report.scanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.component.Content;
import jp.co.systembase.report.component.ContentInstance;
import jp.co.systembase.report.component.ContentRange;
import jp.co.systembase.report.component.ContentState;
import jp.co.systembase.report.component.DataContainer;
import jp.co.systembase.report.component.Evaluator;
import jp.co.systembase.report.component.Group;
import jp.co.systembase.report.component.GroupRange;
import jp.co.systembase.report.component.GroupState;
import jp.co.systembase.report.component.Region;

public class RenderingScanner extends DefaultScanner {

	public List<ContentInstance> contentInstances = new ArrayList<ContentInstance>();
	public DataContainer dataContainer;
	public Map<String, Group> displayedGroups;

	public RenderingScanner(){
		this.dataContainer = new DataContainer();
		this.displayedGroups = new HashMap<String, Group>();
	}

	public RenderingScanner(RenderingScanner parent){
		this.dataContainer = parent.dataContainer;
		this.displayedGroups = parent.displayedGroups;
	}

	@Override
	public IScanner beforeGroup(
			Group group,
			ContentRange contentRange,
			Region parentRegion,
			GroupState groupState) {
		this.dataContainer.initializeData(group);
		String id = group.getDesign().id;
		if (id != null && !this.displayedGroups.containsKey(id)){
			this.displayedGroups.put(id, group);
		}
		return this;
	}

	@Override
	public IScanner beforeContent(
			Content content,
			GroupRange groupRange,
			Region parentRegion,
			ContentState contentState) {
		return new RenderingScanner(this);
	}

	@Override
	public void afterContent(
			Content content,
			GroupRange groupRange,
			Region parentRegion,
			ContentState contentState,
			Region region,
			IScanner scanner) {
		if (region != null){
			this.contentInstances.add(
				new ContentInstance(
					content,
					region,
					contentState));
			{
				RenderingScanner rs = (RenderingScanner)scanner;
				this.contentInstances.addAll(rs.contentInstances);
			}
		}
		if (content.baseContent == null && contentState.intrinsic){
			this.dataContainer.updateData(content);
		}
	}

	@Override
	public void scanSubContent(
			Content content,
			Region parentRegion,
			ContentState contentState,
			Region region,
			Region paperRegion) {
		if (region != null && content.subContents != null){
			Region _region = new Region(region);
			_region.maxBottom = _region.bottom;
			_region.maxRight = _region.right;
			for(Content c: content.subContents){
				Evaluator evaluator = new Evaluator(c, contentState);
				if (c.design.visibilityCond != null){
					if (!ReportUtil.condition(evaluator.evalTry(c.design.visibilityCond))){
						continue;
					}
				}
				GroupRange gr = null;
				if (c.groups != null){
					gr = new GroupRange(c.groups);
				}
				c.scan(this, gr, paperRegion, parentRegion, _region, contentState, evaluator);
			}
		}
	}

}
