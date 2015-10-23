package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.systembase.report.Report;

public class ElementDesigns extends ArrayList<ElementDesign> {

	private static final long serialVersionUID = -9046835607464682453L;

	private ElementDesigns(){}

	@SuppressWarnings("unchecked")
	public ElementDesigns(ContentDesign contentDesign){
		if (contentDesign.mergeContentId != null){
			if (Report.sharedContents.containsKey(contentDesign.mergeContentId)){
				ContentDesign cd = Report.sharedContents.get(contentDesign.mergeContentId);
				if (cd.desc.containsKey("elements")){
					for(Map<?, ?> d: (List<Map<?, ?>>)cd.desc.get("elements")){
						this.add(new ElementDesign(d));
					}
				}
			}
		}
		if (contentDesign.desc.containsKey("elements")){
			for(Map<?, ?> d: (List<Map<?, ?>>)contentDesign.desc.get("elements")){
				this.add(new ElementDesign(d));
			}
		}
	}

	public ElementDesign find(String id){
		for(ElementDesign e: this){
			if (id.equals(e.get("id"))){
				return e;
			}
		}
		return null;
	}

	public ElementDesigns selectByType(String type){
		ElementDesigns ret = new ElementDesigns();
		for(ElementDesign e: this){
			if (type.equals(e.get("type"))){
				ret.add(e);
			}
		}
		return ret;
	}

}
