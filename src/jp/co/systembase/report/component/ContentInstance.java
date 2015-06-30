package jp.co.systembase.report.component;

import java.util.List;
import java.util.Map;

import jp.co.systembase.core.Cast;
import jp.co.systembase.report.Report;

public class ContentInstance {
	
	public Content content;
	public Region region;
	public ContentState contentState;
	
	public ContentInstance(
			Content content,
			Region region,
			ContentState contentState){
		this.region = region;
		this.content = content;
		this.contentState = contentState;
	}
	
	public ElementDesigns getElementDesigns(Evaluator evaluator){
		ElementDesigns ret = new ElementDesigns(this.content.design);
		Report report = evaluator.basicContext.report;
		_customizeElements(ret, evaluator);
		if (report.customizer != null){
			report.customizer.renderContent(this.content, evaluator, this.region, ret);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private void _customizeElements(ElementDesigns elements, Evaluator evaluator){
		for(ElementDesign e: elements){
			if (e.base.containsKey("customize")){
				for(Map<?, ?> d: (List<Map<?, ?>>)e.base.get("customize")){
					ElementDesign ce = new ElementDesign(d);
					if (!ce.isNull("property") && !ce.isNull("cond") && !ce.isNull("exp")){
						if (Cast.toBool(evaluator.evalTry((String)ce.get("cond")))){
							ElementDesign _e = e;
							String[] ps = ((String)ce.get("property")).split("\\.");
							for(int i = 0;i < ps.length - 1;i++){
								_e = _e.child(ps[i].trim());
							}
							try{
								_e.put(ps[ps.length - 1], evaluator.eval((String)ce.get("exp")));
							}catch(Exception ex){
							}
						}
					}
				}
			}
		}
	}

}
