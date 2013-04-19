package jp.co.systembase.report.component;

import java.util.HashMap;
import java.util.Map;

import jp.co.systembase.core.Cast;

public class ContentSizeDesign {

	public float initial;
	public float max;
	public boolean specInitial;
	public boolean specMax;
	public boolean revInitial;
	public boolean revMax;
	public boolean notExtendable;

	public ContentSizeDesign(){
		 this(new HashMap<Object, Object>());
	}

	public ContentSizeDesign(Map<?, ?> desc){
		if (desc.containsKey("initial")){
			this.initial = Cast.toFloat(desc.get("initial"));
			this.specInitial = true;
		}else{
			this.initial = 0f;
			this.specInitial = false;
		}
		if (desc.containsKey("max")){
			this.max = Cast.toFloat(desc.get("max"));
			this.specMax = true;
		}else{
			this.max = 0f;
			this.specMax = false;
		}
		this.revInitial = Cast.toBool(desc.get("rev_initial"));
		this.revMax = Cast.toBool(desc.get("rev_max"));
		this.notExtendable = Cast.toBool(desc.get("not_extendable"));
	}

}
