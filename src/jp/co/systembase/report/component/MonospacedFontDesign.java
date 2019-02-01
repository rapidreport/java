package jp.co.systembase.report.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.systembase.core.Cast;

public class MonospacedFontDesign {

	public ItemDesign defaultFont = null;
	public Map<String, ItemDesign> fontMap = new HashMap<String, ItemDesign>();
	
	public MonospacedFontDesign() {
		this(new ArrayList<Object>());
	}
	
	@SuppressWarnings("unchecked")
	public MonospacedFontDesign(List<?> desc) {
		for(Map<?, ?> d: (List<Map<?, ?>>)desc){
			if (d.containsKey("name")){
				this.fontMap.put((String)d.get("name"), new ItemDesign(d));
			}else{
				this.defaultFont = new ItemDesign(d);
			}
		}
	}

	public DetailDesign get(FontDesign fontDesign){
		ItemDesign item = this.defaultFont;
		if (this.fontMap.containsKey(fontDesign.name)){
			item = this.fontMap.get(fontDesign.name);
		}
		if (item == null){
			return null;
		}
		if (fontDesign.bold){
			return item.bold;
		}else{
			return item.regular;
		}
	}

	public static class ItemDesign{
		public DetailDesign regular;
		public DetailDesign bold;
		public ItemDesign(Map<?, ?> desc){
			this.regular = new DetailDesign((Map<?, ?>)desc.get("regular"));
			this.bold = new DetailDesign((Map<?, ?>)desc.get("bold"));
		}
	}

	public static class DetailDesign{
		public float halfWidth;
		public float fullWidth;
		public float rowHeight;
		public DetailDesign(Map<?, ?> desc){
			this.halfWidth = Cast.toFloat(desc.get("half_width"));
			this.fullWidth = Cast.toFloat(desc.get("full_width"));
			this.rowHeight = Cast.toFloat(desc.get("row_height"));
		}
	}

}
