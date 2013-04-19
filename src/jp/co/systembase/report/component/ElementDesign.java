package jp.co.systembase.report.component;

import java.util.HashMap;
import java.util.Map;

public class ElementDesign {

	public Map<?, ?> base;
	public Map<String, Object> modify;
	public ElementDesign rootDesign;
	public boolean modified = false;

	public ElementDesign(String type){
		HashMap<String, Object> base = new HashMap<String, Object>();
		base.put("type", type);
		this.base = base;
		this.modify = new HashMap<String, Object>();
		this.rootDesign = this;
	}

	public ElementDesign(Map<?, ?> base){
		this.base = base;
		this.modify = new HashMap<String, Object>();
		this.rootDesign = this;
	}

	private ElementDesign(Map<?, ?> base, Map<String, Object> custom, ElementDesign rootDesign){
		this.base = base;
		this.modify = custom;
		this.rootDesign = this;
	}

	public boolean isNull(String key){
		if (this.modify.containsKey(key)){
			return (this.modify.get(key) == null);
		}
		if (this.base != null){
			return (this.base.get(key) == null);
		}
		return true;
	}

	public Object get(String key){
		if (this.modify.containsKey(key)){
			return this.modify.get(key);
		}
		if (this.base != null){
			return this.base.get(key);
		}
		return null;
	}

	public void put(String key, Object value){
		this.modify.put(key, value);
		this.rootDesign.modified = true;
	}

	@SuppressWarnings("unchecked")
	public ElementDesign child(String key){
		if (!this.modify.containsKey(key)){
			this.modify.put(key, new HashMap<String, Object>());
		}
		return new ElementDesign(
				(Map<?, ?>)this.base.get(key),
				(Map<String, Object>)this.modify.get(key),
				this.rootDesign);
	}

	public Region getRegion(Region contentRegion){
		return new ElementLayoutDesign(this.child("layout")).getRegion(contentRegion);
	}

	@Override
	public String toString() {
		String ret = "";
		if (!this.isNull("caption")){
			ret = (String)this.get("caption");
		}else if(!this.isNull("id")){
			ret = (String)this.get("id");
		}
		return ret + "(" + (String)this.get("type") + ")";
	}


}
