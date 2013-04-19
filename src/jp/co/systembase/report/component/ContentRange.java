package jp.co.systembase.report.component;

public class ContentRange {

	public Group group = null;
	public ContentHistory first = null;
	public ContentHistory last = null;

	public ContentRange(Group group){
		this(
			group,
			ContentHistory.getInstance(
					group.getNextContent(null),
					group.parentGroups),
			null);
	}

	public ContentRange(
			Group group,
			ContentHistory first,
			ContentHistory last){
		this.group = group;
		this.first = first;
		this.last = last;
	}

	public boolean isFirst(Content c){
		return ((this.first != null) && (c == this.first.content));
	}

	public boolean isLast(Content c){
		return ((this.last != null) && (c == this.last.content));
	}

	public GroupRange getSubRange(Content c){
		if (c.groups == null){
			return null;
		}
		ContentHistory _first = null;
		ContentHistory _last = null;
		if (this.first == null){
		}else if (this.first.content.getIndex() > c.getIndex()){
		}else if (this.last != null && this.last.content.getIndex() < c.getIndex()){
		}else{
			if (this.first.content.getIndex() < c.getIndex()){
				_first = ContentHistory.getInstance(
						c.getNextContent(null), c.groups);
			}else{
				_first = this.first.child;
			}
			if (this.last != null && this.last.content == c){
				_last = this.last.child;
			}
		}
		return new GroupRange(c.groups, _first, _last);
	}

}
