package jp.co.systembase.report.method;

import jp.co.systembase.report.Report.EEvalContext;
import jp.co.systembase.report.component.ContentState;
import jp.co.systembase.report.component.Evaluator;

public class StateMethod implements IMethod {

	public EEvalContext getAvailableContext() {
		return EEvalContext.CONTENT;
	}

	public Object exec(
			Evaluator evaluator,
			String param,
			String scope,
			String unit) throws Throwable {
		if (param == null){
			return null;
		}
		ContentState state = evaluator.contentContext.contentState.findScope(scope);
		if (param.equals("first")){
			return state.groupState.first;
		}else if (param.equals("last")){
			return state.groupState.last;
		}else if (param.equals("last2")){
			return state.groupState.last2;
		}else if (param.equals("index")){
			return state.groupState.index;
		}else if (param.equals("group_first")){
			return state.groupState.groupFirst;
		}else if (param.equals("group_last")){
			return state.groupState.groupLast;
		}else if (param.equals("group_last2")){
			return state.groupState.groupLast2;
		}else if (param.equals("group_index")){
			return state.groupState.groupIndex;
		}else if (param.equals("first_page")){
			return state.firstPage;
		}else if (param.equals("last_page")){
			return state.lastPage;
		}else if (param.equals("intrinsic")){
			return state.intrinsic;
		}else if (param.equals("blank")){
			return state.groupState.blank;
		}else if (param.equals("blank_first")){
			return state.groupState.blankFirst;
		}else if (param.equals("blank_last")){
			return state.groupState.blankLast;
		}else if (param.equals("header")){
			return state.header;
		}else if (param.equals("footer")){
			return state.footer;
		}
		return null;
	}

}
