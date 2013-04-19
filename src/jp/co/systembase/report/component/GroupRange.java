package jp.co.systembase.report.component;

public class GroupRange {

	public Groups groups = null;
	public ContentHistory first = null;
	public ContentHistory last = null;

	public GroupRange(Groups groups){
		this(
			groups,
			ContentHistory.getInstance(
					groups.getNextContent(null),
					groups),
			null);
	}

	public GroupRange(
			Groups groups,
			ContentHistory first,
			ContentHistory last){
		this.groups = groups;
		this.first = first;
		this.last = last;
	}

	public ContentRange getSubRange(Group g){
		ContentHistory _first = null;
		ContentHistory _last = null;
		if (this.first == null){
		}else if (this.first.group.index > g.index){
		}else if (this.last != null && this.last.group.index < g.index){
		}else{
			if (this.first.group.index < g.index){
				_first = ContentHistory.getInstance(
						g.getNextContent(null),
						g.parentGroups);
			}else{
				_first = this.first;
			}
			if (this.last != null && this.last.group == g){
				_last = this.last;
			}
		}
		return new ContentRange(g, _first, _last);
	}

	public int getGroupCount(){
		if (this.first == null){
			return 0;
		}else if (this.last == null){
			return this.groups.groups.size() - this.first.group.index;
		}else{
			return this.last.group.index - this.first.group.index + 1;
		}
	}

	public Group getGroup(int i){
		return this.groups.groups.get(this.first.group.index + i);
	}

	public boolean containsFirst(){
		if (this.first == null){
			return false;
		}else{
			return (this.first.group.index == 0);
		}
	}

	public boolean containsLast(){
		if (this.first == null){
			return false;
		}else if (this.last == null){
			return true;
		}else{
			return (this.last.group.index == (this.groups.groups.size() - 1));
		}
	}

	public boolean unbreakable(){
		if (this.last != null){
			ContentHistory ch = this.last;
			while(ch != null){
				if (ch.contentLast && ch.content.design.unbreakable){
					return true;
				}
				ch = ch.child;
			}
		}
		return false;
	}

}
