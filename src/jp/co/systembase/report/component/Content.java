package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.Report;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.scanner.IScanner;

public class Content {

	public ContentDesign design;
	public Groups groups = null;
	public List<Content> subContents = null;

	public Group parentGroup;
	public Content baseContent;

	public Content(
			ContentDesign design,
			Group parentGroup){
		this(design, parentGroup, null);
	}

	public Content(
			ContentDesign design,
			Group parentGroup,
			Content baseContent){
		this.design = design;
		this.parentGroup = parentGroup;
		this.baseContent = baseContent;
	}

	public void fill(ReportData data){
		if (this.design.groupDesign != null){
			this.groups = new Groups(
					this.design.groupDesign,
					this.getReport(),
					this);
			this.groups.fill(data);
		}
		if (this.design.subContentDesigns != null){
			this.subContents = new ArrayList<Content>();
			for(ContentDesign cd: this.design.subContentDesigns){
				Content c = new Content(cd, this.parentGroup, this);
				this.subContents.add(c);
				c.fill(data);
			}
		}
	}

	public Region scan(
			IScanner scanner,
			GroupRange groupRange,
			Region paperRegion,
			Region parentRegion,
			Region region,
			ContentState contentState,
			Evaluator evaluator){
		IScanner _scanner = scanner.beforeContent(
				this, groupRange, parentRegion, contentState);
		Region _region = region;
		if (this.getReport().customizer != null){
			_region = this.getReport().customizer.contentRegion(
					this, evaluator, _region);
		}
		if (_region != null && !contentState.groupState.blank){
			if (this.groups != null && groupRange != null){
				_region = this.groups.scan(
						_scanner, groupRange, paperRegion, _region, contentState);
			}
		}
		scanner.afterContent(
				this, groupRange, parentRegion, contentState, _region, _scanner);
		scanner.scanSubContent(
				this, parentRegion, contentState, _region, paperRegion);
		return _region;
	}

	public Content getNextContent(ContentHistory ch){
		if (ch == null){
			if (this.groups == null || this.groups.groups.isEmpty()){
				return this;
			}else{
				return this.groups.getNextContent(null);
			}
		}else{
			if (this.groups == null || this.groups.groups.isEmpty()){
				return null;
			}else{
				return this.groups.getNextContent(ch.child);
			}
		}
	}

	public int getIndex(){
		if (this.baseContent != null){
			return this.baseContent.getIndex();
		}else{
			return this.parentGroup.contents.indexOf(this);
		}
	}

	public int getSubIndex(){
		if (this.baseContent != null){
			return this.baseContent.subContents.indexOf(this);
		}else{
			return -1;
		}
	}

	public ReportData getData(){
		return this.parentGroup.data;
	}

	public Groups getParentGroups(){
		return this.parentGroup.parentGroups;
	}

	public Report getReport(){
		return this.parentGroup.parentGroups.report;
	}

	@Override
	public String toString() {
		String ret = this.parentGroup.toString();
		if (this.design.caption != null){
			ret += this.design.caption;
		}else if (this.design.id != null){
			ret += this.design.id;
		}else{
			ret += "(content)";
		}
		return ret + "[" + this.getIndex() + "]";
	}

}
