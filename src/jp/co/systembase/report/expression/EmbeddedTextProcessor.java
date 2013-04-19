package jp.co.systembase.report.expression;

import java.util.ArrayList;
import java.util.List;

import jp.co.systembase.report.ReportDesign;
import jp.co.systembase.report.component.ElementDesign;
import jp.co.systembase.report.component.EvalException;
import jp.co.systembase.report.renderer.RenderUtil;

public class EmbeddedTextProcessor {

	private int index;

	public List<String> extractExpressions(String source) throws EvalException{
		if (source == null){
			return null;
		}
		this.index = 0;
		List<String> ret = null;
		while(true){
			this.nextText(source);
			if (this.index == source.length()){
				break;
			}
			if (ret == null){
				ret = new ArrayList<String>();
			}
			ret.add(this.nextExpression(source));
		}
		return ret;
	}

	public String embedData(
			ReportDesign reportDesign,
			ElementDesign formatterDesign,
			String source,
			List<?> data) throws EvalException{
		if (source == null){
			return null;
		}
		this.index = 0;
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while(true){
			if (this.index == source.length()){
				break;
			}
			String t = this.nextText(source);
			if (t != null){
				sb.append(t);
			}
			if (i < data.size()){
				String v = RenderUtil.format(reportDesign, formatterDesign, data.get(i));
				if (v != null){
					sb.append(v);
				}
				this.nextExpression(source);
				i += 1;
			}
		}
		return sb.toString();
	}

	private String nextText(String source){
		if (this.index == source.length()){
			return null;
		}
		int i = this.index;
		while(true){
			if (i < source.length() - 1 &&
					source.charAt(i) == '#' && source.charAt(i + 1) == '{'){
				String ret = source.substring(this.index, i);
				this.index = i + 2;
				return ret;
			}
			if (i == source.length()){
				String ret = source.substring(this.index);
				this.index = i;
				return ret;
			}
			i += 1;
		}
	}

	private String nextExpression(String source) throws EvalException{
		int i = this.index;
		boolean quoted = false;
		boolean escaped = false;
		while(true){
			if (i == source.length()){
				throw new EvalException("embedded expression isnot terminated : " + source);
			}
			if (quoted){
				if (escaped){
					escaped = false;
				}else{
					if (source.charAt(i) == '\''){
						quoted = false;
					}else if (source.charAt(i) == '\\'){
						escaped = true;
					}
				}
			}else{
				if (source.charAt(i) == '}'){
					String ret = source.substring(this.index, i);
					this.index = i + 1;
					return ret.trim();
				}else if (source.charAt(i) == '\''){
					quoted = true;
				}
			}
			i += 1;
		}
	}

}
