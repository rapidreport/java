package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.component.textsplitter.TextSplitterByWidth;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.internal.SplitStringDataSource;

public class GroupSplitStringDesign {

	public String key;
	public String exp;
	public int width;
	public boolean breakRule;
	
	public GroupSplitStringDesign() {
		this(new HashMap<Object, Object>());
	}

	public GroupSplitStringDesign(Map<?, ?> desc) {
		this.key = (String)desc.get("key");
		if (desc.containsKey("exp")){
			this.exp = (String)desc.get("exp");
		}else{
			this.exp = "." + this.key;
		}
		this.width = Cast.toInt(desc.get("width"));
		this.breakRule = Cast.toBool(desc.get("break_rule"));
	}

	public List<ReportData> split(ReportData data){
		List<ReportData> ret = new ArrayList<ReportData>();
		String t = (String)(new Evaluator(data)).evalTry(this.exp);
		TextSplitterByWidth sp = new TextSplitterByWidth(this.width, this.breakRule);
		for(String _t: sp.getLines(t)){
			ret.add(new ReportData(new SplitStringDataSource(data, this.key, _t), data.report, data.group));
		}
		return ret;
	}

}
