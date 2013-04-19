package jp.co.systembase.report.component;

import jp.co.systembase.report.Report;

public class ContentHistory {

	public Report report = null;
	public Group group = null;
	public Content content = null;
	public boolean groupFirst = false;
	public boolean groupLast = false;
	public boolean contentFirst = false;
	public boolean contentLast = false;
	public ContentHistory child = null;

	private ContentHistory(){};

	public static ContentHistory getInstance(Content content, Groups rootGroups){
		ContentHistory ret = null;
		Content c = content;
		while(c != null){
			if (rootGroups != null && c.groups == rootGroups){
				break;
			}
			ContentHistory t = new ContentHistory();
			t.content = c;
			t.group = c.parentGroup;
			t.report = c.parentGroup.parentGroups.report;
			if (ret == null ||
					(ret.groupFirst && c.groups.groups.get(0) == ret.group)){
				t.contentFirst = true;
			}
			if (ret == null ||
					(ret.groupLast && c.groups.groups.get(c.groups.groups.size() - 1) == ret.group)){
				t.contentLast = true;
			}
			if (t.contentFirst &&
					c.parentGroup.contents.get(0) == c){
				t.groupFirst = true;
			}
			if (t.contentLast &&
					c.parentGroup.contents.get(c.parentGroup.contents.size() - 1) == c){
				t.groupLast = true;
			}
			t.child = ret;
			ret = t;
			c = c.parentGroup.parentGroups.parentContent;
		}
		return ret;
	}

	public ContentHistory getLeaf(){
		ContentHistory ret = this;
		while(ret.child != null){
			ret = ret.child;
		}
		return ret;
	}

	@Override
	public String toString() {
		String ret = "";
		ContentHistory ch = this;
		while(ch != null){
			ret += "group: " + ch.group.toString() + " ";
			if (ch.groupFirst){
				ret += "(group first) ";
			}else{
				ret += "              ";
			}
			if (ch.groupLast){
				ret += "(group last) ";
			}else{
				ret += "             ";
			}
			ret += "\n";
			ret += "content: " + ch.content.toString() + " ";
			if (ch.contentFirst){
				ret += "(content first) ";
			}else{
				ret += "                ";
			}
			if (ch.contentLast){
				ret += "(content last) ";
			}else{
				ret += "               ";
			}
			ret += "\n";
			ch = ch.child;
		}
		return ret;
	}

}
