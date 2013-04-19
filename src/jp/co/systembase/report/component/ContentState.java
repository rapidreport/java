package jp.co.systembase.report.component;

public class ContentState {

	public GroupState groupState;
	public Content content;
	public boolean intrinsic = false;
	public boolean firstPage = false;
	public boolean lastPage = false;
	public boolean header = false;
	public boolean footer = false;

	public ContentState(GroupState groupState, Content content){
		this.groupState = groupState;
		this.content = content;
	}

	public ContentState findScope(String id){
		if (id == null){
			return this;
		}else if (id.equals("")){
			if (this.groupState.parentState != null){
				return this.groupState.parentState;
			}
		}else{
			ContentState ret = this;
			while(ret != null){
				if (id.equals(ret.content.parentGroup.getDesign().id)){
					return ret;
				}
				ret = ret.groupState.parentState;
			}
		}
		throw new IllegalArgumentException(id + " is not found");
	}

}
