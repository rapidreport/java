package jp.co.systembase.report.component;

public class GroupState {

	public ContentState parentState = null;
	public boolean first = false;
	public boolean last = false;
	public boolean last2 = false;
	public int index = 0;
	public boolean groupFirst = false;
	public boolean groupLast = false;
	public boolean groupLast2 = false;
	public int groupIndex = 0;
	public boolean blank = false;
	public boolean blankFirst = false;
	public boolean blankLast = false;

	public GroupState(ContentState parentState){
		this.parentState = parentState;
	}

}
