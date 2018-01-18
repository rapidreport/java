package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.ReportUtil;
import jp.co.systembase.report.data.ReportData;
import jp.co.systembase.report.data.internal.SplitStringDataSource;

public class GroupSplitStringDesign {

	public String key;
	public String exp;
	public int width;
	
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
	}

	public List<ReportData> split(ReportData data){
		List<ReportData> ret = new ArrayList<ReportData>();
		String t = (String)(new Evaluator(data)).evalTry(this.exp);
		if (t == null){
			ret.add(new ReportData(
					new SplitStringDataSource(data, this.key, null), data.report, data.group));
		}else{
			for(String _t: ReportUtil.splitLines(t)){
				if (this.width == 0){
					ret.add(new ReportData(
							new SplitStringDataSource(data, this.key, _t), data.report, data.group));
				}else{
					int b = 0;
					int e = 0;
					do{
						b = e;
						e = ReportUtil.getWIndex(_t, b, this.width);
						ret.add(new ReportData(
								new SplitStringDataSource(data, this.key, ReportUtil.subString(_t, b, e - b)), 
								data.report, data.group));
					}while(e < _t.length());	
				}
			}
		}
		return ret;
	}

}
