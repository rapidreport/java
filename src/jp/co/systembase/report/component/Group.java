package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.data.DummyDataSource;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.scanner.IScanner;

public class Group {

	public ReportData data = null;
	public List<Content> contents = null;
	public Groups parentGroups = null;
	public int index;
	public int traversalIndex = -1;

	public Map<GroupDesign, IndexRange> dataIndexRangeMap =
		new HashMap<GroupDesign, IndexRange>();

	public Crosstab.State crosstabState = null;

	public Group(Groups parentGroups, int index, Crosstab.State crosstabState){
		this.parentGroups = parentGroups;
		this.index = index;
		if (crosstabState != null){
			this.crosstabState = crosstabState;
		}else{
			if (this.parentGroups.parentContent != null){
				this.crosstabState = this.parentGroups.parentContent.parentGroup.crosstabState;
			}
		}
	}

	public void fill(ReportData data){
		this.data = data;
		this.data.setGroup(this);
		this.contents = new ArrayList<Content>();
		for(ContentDesign cd: this.getDesign().contentDesigns){
			if (cd.existenceCond != null){
				Evaluator evaluator = new Evaluator(this.getReport(), this.data, this.data.getRecord());
				if (!ReportUtil.condition(evaluator.evalTry(cd.existenceCond))){
					continue;
				}
			}
			Content c = new Content(cd, this);
			this.contents.add(c);
			c.fill(this.data);
		}
		if (this.contents.size() == 0){
			ContentDesign cd = new ContentDesign(new HashMap<Object, Object>(), this.getDesign());
			Content c = new Content(cd, this);
			this.contents.add(c);
			c.fill(new ReportData(DummyDataSource.getInstance(), this));
		}
	}

	public Region scan(
			IScanner scanner,
			ContentRange contentRange,
			Region paperRegion,
			Region parentRegion,
			GroupState groupState){
		IScanner _scanner = scanner.beforeGroup(
				this,
				contentRange,
				parentRegion,
				groupState);
		Region contentsRegion = this.getDesign().layout.getGroupInitialRegion(parentRegion);
		Region region = contentsRegion;
		boolean firstPage = false;
		boolean lastPage = false;
		if (contentRange.first != null){
			if (contentRange.first.content == this.contents.get(0) &&
					contentRange.first.contentFirst){
				firstPage = true;
			}
			if (contentRange.last == null){
				lastPage = true;
			}else if (contentRange.last.content == this.contents.get(this.contents.size() - 1) &&
						contentRange.last.contentLast){
					lastPage = true;
			}
		}
		List<Content> _contents = new ArrayList<Content>();
		List<ContentState> _contentStates = new ArrayList<ContentState>();
		{
			boolean intrinsic = false;
			for (Content content: this.contents){
				if (contentRange.isFirst(content)){
					intrinsic = true;
				}
				if (intrinsic || content.design.everyPage){
					ContentState contentState = new ContentState(groupState, content);
					contentState.intrinsic = intrinsic;
					contentState.firstPage = firstPage;
					contentState.lastPage = lastPage;
					_contents.add(content);
					_contentStates.add(contentState);
				}
				if (contentRange.isLast(content)){
					intrinsic = false;
				}
			}
			if (_contentStates.size() > 0){
				_contentStates.get(0).header = true;
				_contentStates.get(_contentStates.size() - 1).footer = true;
			}
		}
		{
			Region lastRegion = null;
			for(int i = 0;i < _contents.size();i++){
				Content content = _contents.get(i);
				ContentState contentState = _contentStates.get(i);
				Evaluator evaluator = new Evaluator(content, contentState);
				if (content.design.visibilityCond != null){
					if (!ReportUtil.condition(evaluator.evalTry(content.design.visibilityCond))){
						continue;
					}
				}
				GroupRange groupRange;
				if (contentState.intrinsic){
					groupRange = contentRange.getSubRange(content);
				}else if (content.groups != null){
					groupRange = new GroupRange(content.groups);
				}else{
					groupRange = null;
				}
				Region contentRegion = this.getDesign().layout.getContentRegion(
						content.design.size,
						contentsRegion,
						lastRegion);
				lastRegion = content.scan(
						_scanner,
						groupRange,
						paperRegion,
						contentsRegion,
						contentRegion,
						contentState,
						evaluator);
				if (lastRegion != null){
					region = lastRegion.getMergedRegion(region);
				}
			}
		}
		scanner.afterGroup(
				this,
				contentRange,
				parentRegion,
				groupState,
				region,
				_scanner);
		return region;
	}

	public Content getAggregateSrcContent(){
		for(Content c: this.contents){
			if (c.design.aggregateSrc){
				return c;
			}
		}
		return null;
	}

	public Content getNextContent(ContentHistory ch){
		if (ch == null){
			return this.contents.get(0).getNextContent(null);
		}else{
			Content c = ch.content.getNextContent(ch);
			if (c != null){
				return c;
			}else{
				int i = ch.content.getIndex();
				if (i < this.contents.size() - 1){
					return this.contents.get(i + 1).getNextContent(null);
				}else{
					return null;
				}
			}
		}
	}

	public GroupDesign getDesign(){
		return this.parentGroups.design;
	}

	public Report getReport(){
		return this.parentGroups.report;
	}

	@Override
	public String toString() {
		return this.parentGroups.toString() + "[" + this.index + "]";
	}

}
